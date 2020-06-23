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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.tianyi.zhang.multiplayer.snake.App;
import com.tianyi.zhang.multiplayer.snake.helpers.AssetManager;
import com.tianyi.zhang.multiplayer.snake.states.GameState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class BroadcastState extends GameState {
    public final Object connectionIdsLock;
    public final List<Integer> connectionIds;
    private static final String TAG = BroadcastState.class.getCanonicalName();

    // UI elements
    private final Stage stage;
    private final VisTable table;
    private final VisImage imgTitle;
    public final VisTextButton btnStart;
    public final VisLabel lblPlayerCount;

    private final AtomicBoolean success;

    public static final String WAITING_FOR_PLAYERS = "Waiting for other snakes to join the game...";
    public static final String PLAYERS_CONNECTED_FORMAT = "%d";
    private VisTextButton btnToTitleScreen;

    public BroadcastState(App app) {
        super(app);

        connectionIdsLock = new Object();
        connectionIds = new ArrayList<Integer>();

        // Set up UI element layout
        stage = new Stage();
        stage.setViewport(new ScreenViewport(stage.getCamera()));

        table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        imgTitle = new VisImage(AssetManager.INSTANCE.getTitleTexture());
        table.add(imgTitle).row();

        if (Locale.getDefault().getLanguage().equals("zh"))
        {
            VisImage waitForOtherImg = new VisImage(AssetManager.INSTANCE.PLAYER_JOINED_NUM);
            table.add(waitForOtherImg).padTop(50).row();
        }
        else {
            VisLabel playerNum = new VisLabel("Number of players joined:");
            table.add(playerNum).padTop(50).row();
        }

        lblPlayerCount = new VisLabel("0");
        lblPlayerCount.setScale(0.5f);
        table.add(lblPlayerCount).row();

        btnStart = new VisTextButton("\n        GO!        \n");
        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.debug(TAG, "START GAME button clicked");
                synchronized (connectionIdsLock) {
                    if (connectionIds.size() > 0) {
                        _app.setState(new SVMainGameState(_app, connectionIds));
                    }
                }
            }
        });
        btnStart.setDisabled(true);
        btnStart.setColor(255,255,255,1);
        table.add(btnStart).padTop(20).row();


        if (Locale.getDefault().getLanguage().equals("zh"))
        {
            VisImage btnToTitleScreenImg = new VisImage(AssetManager.INSTANCE.BACK_TO_MAIN);
            btnToTitleScreenImg.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    _app.gotoTitleScreen();
                }
            });
            table.add(btnToTitleScreenImg).padTop(100).row();
        }
        else {
            btnToTitleScreen = new VisTextButton("Return to main screen");
            btnToTitleScreen.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    _app.gotoTitleScreen();
                }
            });
            table.add(btnToTitleScreen).padTop(40).row();
        }

        Gdx.input.setInputProcessor(stage);

        success = new AtomicBoolean();

        // Start broadcasting
        try {
            _app.getAgent().broadcast(new Listener());
            success.set(true);
        } catch (IOException e) {
            Gdx.app.error(TAG, e.getMessage());
            success.set(false);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act();
        stage.draw();

        if (!success.get()) {
            _app.gotoErrorScreen("Cannot host game\nIs there someone already hosting a game?");
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
