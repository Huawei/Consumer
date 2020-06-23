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

package com.tianyi.zhang.multiplayer.snake.states;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.tianyi.zhang.multiplayer.snake.App;
import com.tianyi.zhang.multiplayer.snake.helpers.AssetManager;
import com.tianyi.zhang.multiplayer.snake.helpers.Constants;
import com.tianyi.zhang.multiplayer.snake.states.client.LookForServerState;
import com.tianyi.zhang.multiplayer.snake.states.server.BroadcastState;

import java.util.Locale;

public class TitleScreenState extends GameState {
    private static final String TAG = TitleScreenState.class.getCanonicalName();

    private final Stage stage;
    private final VisTable table;
    private final VisImage imgTitle;
    private  VisTextButton btnHost;
    private  VisImage btnHostImg;
    private  VisTextButton btnJoin;
    private  VisImage btnJoinImg;
    private  VisTextButton btnAcknowledgements;
    private  VisImage btnAcknowledgementsImg;
    public Slider speedSlider;

    public TitleScreenState(App app) {
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
            btnHostImg = new VisImage(AssetManager.INSTANCE.CREATE_GAME);
            btnHostImg.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("wmq", "Clicked Host a Game.");
                            _app.initAgent(true);
                            _app.pushState(new BroadcastState(_app));
                        }
                    });
                }
            });
            table.add(btnHostImg).padTop(20).row();

            btnJoinImg = new VisImage(AssetManager.INSTANCE.JOIN_GAME);
            btnJoinImg.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            _app.initAgent(false);
                            _app.pushState(new LookForServerState(_app));
                        }
                    });
                }
            });
            table.add(btnJoinImg).padTop(40).row();

            VisImage btnIntroduction = new VisImage(AssetManager.INSTANCE.INTRO_BTN);
            btnIntroduction.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    _app.pushState(new IntroductinoState(_app));
                }
            });
            table.add(btnIntroduction).padTop(40).row();

            btnAcknowledgementsImg = new VisImage(AssetManager.INSTANCE.ABOUT);
            btnAcknowledgementsImg.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    _app.pushState(new InfoState(_app));
                }
            });
            table.add(btnAcknowledgementsImg).padTop(40).row();

            VisImage gamespeed = new VisImage(AssetManager.INSTANCE.GAME_SPEED);
            table.add(gamespeed).padTop(40).row();
        }
        else {
            btnHost = new VisTextButton("Create a Game");
            btnHost.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("wmq", "Clicked Host a Game.");
                            _app.initAgent(true);
                            _app.pushState(new BroadcastState(_app));
                        }
                    });
                }
            });
            table.add(btnHost).padTop(20).row();

            btnJoin = new VisTextButton("Join a Game");
            btnJoin.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            _app.initAgent(false);
                            _app.pushState(new LookForServerState(_app));
                        }
                    });
                }
            });
            table.add(btnJoin).padTop(20).row();


            btnAcknowledgements = new VisTextButton("About");
            btnAcknowledgements.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    _app.pushState(new InfoState(_app));
                }
            });
            table.add(btnAcknowledgements).padTop(20).row();

            VisLabel gamespeed = new VisLabel("Game Speed:");
            table.add(gamespeed).padTop(40).row();
        }

        speedSlider = new Slider(0, 2, 1, false, VisUI.getSkin());
        speedSlider.setValue(0);
        Constants.MOVE_EVERY_MS = 500;
        speedSlider.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Log.d("wmq", "speed change to: " + speedSlider.getValue());
                if (speedSlider.getValue() == 0.0f)
                {
                    Constants.MOVE_EVERY_MS = 500;
                }
                else if (speedSlider.getValue() == 1.0f)
                {
                    Constants.MOVE_EVERY_MS = 333;
                }
                else if (speedSlider.getValue() == 2.0f)
                {
                    Constants.MOVE_EVERY_MS = 250;
                }
            }
        });
        table.add(speedSlider).size(400,100).row();

        Gdx.input.setInputProcessor(stage);
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
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
