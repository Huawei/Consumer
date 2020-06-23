/*
Copyright 2018 Tianyi Zhang

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


    2020.3.15 - Replaced Kryonet by HMS Nearby Service.
    2020.3.15 - Add Chinese version.
    2020.3.15 - Add Game speed selection.
                Huawei Technologies Co.,Ltd. <nearbyservice@huawei.com>
 */

package com.tianyi.zhang.multiplayer.snake.states.server;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.tianyi.zhang.multiplayer.snake.App;
import com.tianyi.zhang.multiplayer.snake.agents.Server;
import com.tianyi.zhang.multiplayer.snake.elements.Grid;
import com.tianyi.zhang.multiplayer.snake.elements.ServerSnapshot;
import com.tianyi.zhang.multiplayer.snake.elements.Snake;
import com.tianyi.zhang.multiplayer.snake.helpers.AssetManager;
import com.tianyi.zhang.multiplayer.snake.helpers.Constants;
import com.tianyi.zhang.multiplayer.snake.helpers.Utils;
import com.tianyi.zhang.multiplayer.snake.protobuf.generated.ServerPacket;
import com.tianyi.zhang.multiplayer.snake.states.GameState;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SVMainGameState extends GameState {
    private static final String TAG = SVMainGameState.class.getCanonicalName();
    private static final float GRID_RATIO = (float) Constants.WIDTH / Constants.HEIGHT;

    public ServerSnapshot getServerSnapshot() {
        return serverSnapshot;
    }

    public final ServerSnapshot serverSnapshot;
    private final ScheduledExecutorService executor;

    private final OrthographicCamera camera;
    private final SpriteBatch spriteBatch;
    private final Stage stage;
    private Stage lagStage;
    private final VisWindow window;
    private final Table table;
    private final VisLabel lblHosting;
    private VisImage resultImg;
    private long lagDetectSeq = 1;
    private long lastDetectTime = System.currentTimeMillis();
    private VisWindow lagWindow;
    private Table lagTable;
    private VisLabel lagLabel;
    public String lag = "9999 ms";

    private final AtomicReference<Grid> cachedGrid;
    private final AtomicReference<Constants.GameResult> gameResult;
    public List<Integer> mConnectionIds;

    /**
     *
     * @param app a reference to App
     * @param connectionIds a sorted, ascending list of integers representing client IDs
     */
    public SVMainGameState(App app, List<Integer> connectionIds) {
        super(app);
        mConnectionIds = connectionIds;
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {
                        serverSnapshot.onServerInput(Constants.RIGHT);
                    } else {
                        serverSnapshot.onServerInput(Constants.LEFT);
                    }
                } else {
                    if (velocityY > 0) {
                        serverSnapshot.onServerInput(Constants.DOWN);
                    } else {
                        serverSnapshot.onServerInput(Constants.UP);
                    }
                }
                return true;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }

            @Override
            public void pinchStop() {

            }
        }));
        inputMultiplexer.addProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    serverSnapshot.onServerInput(Constants.LEFT);
                } else if (keycode == Input.Keys.UP) {
                    serverSnapshot.onServerInput(Constants.UP);
                } else if (keycode == Input.Keys.RIGHT) {
                    serverSnapshot.onServerInput(Constants.RIGHT);
                } else if (keycode == Input.Keys.DOWN) {
                    serverSnapshot.onServerInput(Constants.DOWN);
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        Gdx.graphics.setContinuousRendering(false);

        int[] tmpIds = new int[connectionIds.size()+1];
        tmpIds[0] = 0;
        for (int i = 0; i < connectionIds.size(); i++) {
            tmpIds[i+1] = connectionIds.get(i);
        }

        executor = Executors.newSingleThreadScheduledExecutor();

        serverSnapshot = new ServerSnapshot(Utils.getNanoTime(), tmpIds);
        cachedGrid = new AtomicReference<Grid>(serverSnapshot.getGrid());

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Grid grid = serverSnapshot.getGrid();
                    cachedGrid.set(grid);
                    ServerPacket.Update.Builder builder = ServerPacket.Update.newBuilder();
                    builder.setVersion(serverSnapshot.getVersion()).setTimestamp(grid.timestamp);
                    for (Snake snake : grid.snakes) {
                        builder.addSnakes(snake.toProtoSnake());
                    }
                    builder.addAllFoodLocations(grid.foods.getLinearPositions());
                    _app.getAgent().send(builder.build().toByteArray());
                    Log.d("wmq", "Server Sent update version: " + serverSnapshot.getVersion() );

                    long curMillis = System.currentTimeMillis();
                    if (curMillis - lastDetectTime > 180)
                    {
                        String str = "ServerLagDetection" + lagDetectSeq;
                        _app.getAgent().send(str.getBytes());
                        Log.d("wmqLAG", "Server Sent LagDetect Seq: " + lagDetectSeq );
                        _app.getAgent().setLagDetecionStartTime(str, curMillis);
                        lagDetectSeq++;
                        lastDetectTime = curMillis;
                    }



                    Gdx.graphics.requestRendering();
                } catch (Exception e) {
                    Gdx.app.error(TAG, "Error encountered inside scheduled task: ", e);
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            _app.gotoErrorScreen("Oops, something went wrong...\nPlease try starting a new game");
                        }
                    });
                }
            }
        }, 0, Constants.MOVE_EVERY_MS / 2, TimeUnit.MILLISECONDS);

        _app.getAgent().setListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
                //serverSnapshot.onClientDisconnected(connection.getID());
            }

            @Override
            public void received(Connection connection, Object object) {
//                if (object instanceof byte[]) {
//                    serverSnapshot.onClientMessage(connection.getID(), Server.parseClientMessage(object));
//                }
            }
        });

        float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight(), ratio = w / h;
        camera = new OrthographicCamera();
        if (ratio >= GRID_RATIO) {
            // Black bars on both sides
            camera.viewportWidth = Constants.HEIGHT * ratio;
            camera.viewportHeight = Constants.HEIGHT;
        } else {
            // Black bars on top and bottom
            camera.viewportWidth = Constants.WIDTH;
            camera.viewportHeight = Constants.WIDTH / ratio;
        }
        camera.position.set(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 0);
        camera.update();
        spriteBatch = new SpriteBatch();

        stage = new Stage();
        stage.setViewport(new ScreenViewport(stage.getCamera()));
        window = new VisWindow(" ", false);
        window.setColor(0, 1f, 0, 0.64f);
        window.setSize(w * 0.9f, h * 0.3f);
        window.setPosition((w - window.getWidth()) / 2f, (h - window.getHeight()) / 2f);
        table = new VisTable(true);

        lblHosting = new VisLabel("Still hosting the game. Please do not exit.");
        table.add(lblHosting).row();
        window.add(table).row();
        stage.addActor(window);

        gameResult = new AtomicReference<Constants.GameResult>(null);

        lagStage = new Stage();
        stage.setViewport(new ScreenViewport(stage.getCamera()));
        lagWindow = new VisWindow("Lag:", false);
        lagWindow.setColor(0, 1f, 0, 0.64f);
        lagWindow.setSize(w * 0.1f, h * 0.1f);
        lagWindow.setPosition((w - lagWindow.getWidth()), (h - lagWindow.getHeight()));
        lagTable = new VisTable(true);
        lagLabel = new VisLabel("999 ms");
        lagTable.add(lagLabel).row();
        lagWindow.add(lagTable).row();
        lagStage.addActor(lagWindow);

        Gdx.app.debug(TAG, "Server main game loaded");
    }

    @Override
    public void render(float delta) {
        Grid grid = cachedGrid.get();

        Utils.clear();
        Utils.renderGrid(grid, camera, spriteBatch);
        lagLabel.setText(lag);
        lagStage.act();
        lagStage.draw();

        if (gameResult.get() == null) {
            if (grid.isSnakeDead(0)) {
                // Game over... GG
                gameResult.set(Constants.GameResult.LOST);
                //lblResult.setText(Constants.GAME_OVER);
                if (Locale.getDefault().getLanguage().equals("zh"))
                {
                    resultImg = new VisImage(AssetManager.INSTANCE.LOSE_WORD);
                    table.add(resultImg).padTop(20).row();
                    VisImage btnToTitleScreenImg = new VisImage(AssetManager.INSTANCE.BACK_TO_MAIN);
                    btnToTitleScreenImg.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            _app.gotoTitleScreen();
                        }
                    });
                    table.add(btnToTitleScreenImg).padTop(50).row();
                }
                else {
                    VisLabel lbl = new VisLabel(Constants.GAME_OVER);
                    table.add(lbl).padTop(20).row();
                    VisTextButton btnToTitleScreen = new VisTextButton("Return to main screen");
                    btnToTitleScreen.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            _app.gotoTitleScreen();
                        }
                    });
                    table.add(btnToTitleScreen).padTop(40).row();
                }

                Gdx.input.setInputProcessor(stage);
            } else if (grid.getAliveCount() == 1) {
                // You are the last snake alive
                gameResult.set(Constants.GameResult.WON);
                //lblResult.setText(Constants.CONGRATS);
                if (Locale.getDefault().getLanguage().equals("zh"))
                {
                    resultImg = new VisImage(AssetManager.INSTANCE.WIN_WORD);
                    table.add(resultImg).padTop(20).row();
                    VisImage btnToTitleScreenImg = new VisImage(AssetManager.INSTANCE.BACK_TO_MAIN);
                    btnToTitleScreenImg.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            _app.gotoTitleScreen();
                        }
                    });
                    table.add(btnToTitleScreenImg).padTop(50).row();
                }
                else {
                    VisLabel lbl = new VisLabel(Constants.CONGRATS);
                    table.add(lbl).padTop(20).row();
                    VisTextButton btnToTitleScreen = new VisTextButton("Return to main screen");
                    btnToTitleScreen.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            _app.gotoTitleScreen();
                        }
                    });
                    table.add(btnToTitleScreen).padTop(40).row();
                }

                Gdx.input.setInputProcessor(stage);
            }
        }

        Constants.GameResult result = gameResult.get();
        if (result != null) {
            if (result == Constants.GameResult.WON || (result == Constants.GameResult.LOST && grid.getAliveCount() <= 1)) {
                serverSnapshot.version += 1;
                table.removeActor(lblHosting);
            }

            stage.act();
            stage.draw();
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        float ratio = (float) width / height;
        if (ratio >= GRID_RATIO) {
            // Black bars on both sides
            camera.viewportWidth = Constants.HEIGHT * ratio;
            camera.viewportHeight = Constants.HEIGHT;
        } else {
            // Black bars on top and bottom
            camera.viewportWidth = Constants.WIDTH;
            camera.viewportHeight = Constants.WIDTH / ratio;
        }
        camera.position.set(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 0);
        camera.update();

        stage.getViewport().update(width, height, true);
        window.setSize(width * 0.9f, height * 0.3f);
        window.setPosition((width - window.getWidth()) / 2f, (height - window.getHeight()) / 2f);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        executor.shutdown();
        stage.dispose();
        Gdx.graphics.setContinuousRendering(true);
    }
}
