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

package com.tianyi.zhang.multiplayer.snake.states.client;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.tianyi.zhang.multiplayer.snake.agents.Client;
import com.tianyi.zhang.multiplayer.snake.elements.ClientSnapshot;
import com.tianyi.zhang.multiplayer.snake.elements.Grid;
import com.tianyi.zhang.multiplayer.snake.helpers.AssetManager;
import com.tianyi.zhang.multiplayer.snake.helpers.Constants;
import com.tianyi.zhang.multiplayer.snake.helpers.Utils;
import com.tianyi.zhang.multiplayer.snake.protobuf.generated.ClientPacket;
import com.tianyi.zhang.multiplayer.snake.states.GameState;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class MainGameState extends GameState {
    private static final String TAG = MainGameState.class.getCanonicalName();
    private static final float GRID_RATIO = (float) Constants.WIDTH / Constants.HEIGHT;

    private final ScheduledExecutorService executor;
    private final int clientId;

    public ClientSnapshot getClientSnapshot() {
        return clientSnapshot;
    }

    private final ClientSnapshot clientSnapshot;

    private final OrthographicCamera camera;
    private final Stage stage;
    private final VisWindow window;
    private final Table table;
    private VisImage resultImg;

    public final AtomicReference<Constants.GameResult> gameResult;
    private final SpriteBatch spriteBatch;

    private Stage lagStage;
    private long lagDetectSeq = 1;
    private VisWindow lagWindow;
    private Table lagTable;
    private VisLabel lagLabel;
    public String lag = "9999 ms";
    private long lastDetectTime = System.currentTimeMillis();

    public MainGameState(App app, int id, long initialUpdateNanoTime, byte[] initialPacket) {
        super(app);
        clientId = id;
        clientSnapshot = new ClientSnapshot(id, initialUpdateNanoTime, Client.parseServerUpdate(initialPacket));

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
                        clientSnapshot.onClientInput(Constants.RIGHT);
                    } else {
                        clientSnapshot.onClientInput(Constants.LEFT);
                    }
                } else {
                    if (velocityY > 0) {
                        clientSnapshot.onClientInput(Constants.DOWN);
                    } else {
                        clientSnapshot.onClientInput(Constants.UP);
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
                    clientSnapshot.onClientInput(Constants.LEFT);
                } else if (keycode == Input.Keys.UP) {
                    clientSnapshot.onClientInput(Constants.UP);
                } else if (keycode == Input.Keys.RIGHT) {
                    clientSnapshot.onClientInput(Constants.RIGHT);
                } else if (keycode == Input.Keys.DOWN) {
                    clientSnapshot.onClientInput(Constants.DOWN);
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

        executor = Executors.newSingleThreadScheduledExecutor();

        Gdx.graphics.setContinuousRendering(false);
        _app.getAgent().setListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
//                if (gameResult.get() == null) {
//                    Gdx.app.postRunnable(new Runnable() {
//                        @Override
//                        public void run() {
//                            _app.gotoErrorScreen("No! We lost contact with the host!");
//                        }
//                    });
//                }
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof byte[]) {
                    //clientSnapshot.onServerUpdate(Client.parseServerUpdate(object));
                }
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
        window.add(table).row();
        stage.addActor(window);

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

        gameResult = new AtomicReference<Constants.GameResult>(null);

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if (clientSnapshot.update()) {
                        Gdx.graphics.requestRendering();
                    }
                    ClientPacket.Message message = clientSnapshot.buildMessage();
                    if (message != null) {
                        _app.getAgent().send(message.toByteArray());
                    }
                    else {
                        //Log.d("wmq", "Message is Null." );
                    }

                    long curMillis = System.currentTimeMillis();
                    if (curMillis - lastDetectTime > 180)
                    {
                        String str = "ClientLagDetection" + lagDetectSeq;
                        _app.getAgent().send(str.getBytes());
                        _app.getAgent().setLagDetecionStartTime(str, curMillis);
                        lagDetectSeq++;
                        lastDetectTime = curMillis;
                    }

                } catch (Exception e) {
                    Gdx.app.error(TAG, "Error encountered while running scheduled rendering task: ", e);
                }
            }
        }, 0, Constants.MOVE_EVERY_MS / 3, TimeUnit.MILLISECONDS);

        Gdx.app.debug(TAG, "Main game loaded");
    }

    @Override
    public void render(float delta) {
        try {
            Utils.clear();

            Grid grid = clientSnapshot.getGrid();
            Utils.renderGrid(grid, camera, spriteBatch);

            lagLabel.setText(lag);
            lagStage.act();
            lagStage.draw();

            // TODO: Store game result in grid
            if (gameResult.get() == null) {
                if (grid.isSnakeDead(clientId)) {
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

            if (gameResult.get() != null) {
                stage.act();
                stage.draw();
            }
        } catch (Exception e) {
            _app.gotoErrorScreen("Please try starting a new game.");
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
