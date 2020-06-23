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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.tianyi.zhang.multiplayer.snake.App;
import com.tianyi.zhang.multiplayer.snake.helpers.AssetManager;
import com.tianyi.zhang.multiplayer.snake.helpers.Utils;
import com.tianyi.zhang.multiplayer.snake.states.GameState;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class LookForServerState extends GameState {
    private static final String TAG = LookForServerState.class.getCanonicalName();

    private final Stage stage;
    private final VisTable table;
    private final VisImage imgTitle;
    public final VisLabel lblInfo;
    private VisTextButton btnToTitleScreen;

    public final AtomicBoolean gameStarted;

    public LookForServerState(App app) {
        super(app);

        stage = new Stage();
        stage.setViewport(new ScreenViewport(stage.getCamera()));

        table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        imgTitle = new VisImage(AssetManager.INSTANCE.getTitleTexture());
        table.add(imgTitle).row();

        if (Locale.getDefault().getLanguage().equals("zh"))
        {
            VisImage btnImg = new VisImage(AssetManager.INSTANCE.LOOK_FOR_HOST);
            table.add(btnImg).pad(20).row();
        }
        else {
            VisLabel lbl = new VisLabel("Looking for host.");
            table.add(lbl).pad(20).row();
        }

        lblInfo = new VisLabel("......");
        lblInfo.setAlignment(Align.center);
        table.add(lblInfo).row();

        Gdx.input.setInputProcessor(stage);

        gameStarted = new AtomicBoolean(false);

        _app.getAgent().lookForServer(new Listener() , new Runnable() {
            @Override
            public void run() {
                _app.gotoErrorScreen("Having trouble finding a host\nIs there someone hosting?\n");
            }
        });


        if (Locale.getDefault().getLanguage().equals("zh"))
        {
            VisImage btnToTitleScreenImg = new VisImage(AssetManager.INSTANCE.BACK_TO_MAIN);
            btnToTitleScreenImg.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    _app.gotoTitleScreen();
                }
            });
            table.add(btnToTitleScreenImg).padTop(40).row();
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

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act();
        stage.draw();
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
