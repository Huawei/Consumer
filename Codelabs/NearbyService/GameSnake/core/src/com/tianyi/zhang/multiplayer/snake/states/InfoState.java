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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.*;
import com.tianyi.zhang.multiplayer.snake.App;
import com.tianyi.zhang.multiplayer.snake.helpers.AssetManager;
import com.tianyi.zhang.multiplayer.snake.helpers.Constants;

import java.util.Locale;

public class InfoState extends GameState {
    private static final String TAG = InfoState.class.getCanonicalName();

    private final Stage stage;
    private final VisTable table;
    private final VisImage imgTitle;
    private final VisLabel lblGameInfo, lblAcknowledgements, lblModifyInfo;
    private final LinkLabel lkGitHub, lkLibGdx, lkKryonet, lkProtoBuf, lkNearby;
    private VisTextButton btnToTitleScreen;

    public InfoState(App app) {
        super(app);

        stage = new Stage();
        stage.setViewport(new ScreenViewport(stage.getCamera()));
        table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        imgTitle = new VisImage(AssetManager.INSTANCE.getTitleTexture());
        table.add(imgTitle).row();
        lblGameInfo = new VisLabel(Constants.VERSION + "\nA game by Tianyi Zhang\nThe sources to this game can be retrieved at");
        lblGameInfo.setAlignment(Align.center);
        table.add(lblGameInfo).row();
        lkGitHub = new LinkLabel("github.com/tonyzhang617/multiplayer-snake", "https://github.com/tonyzhang617/multiplayer-snake");
        table.add(lkGitHub).row();
        lblAcknowledgements = new VisLabel("Acknowledgements: ");
        table.add(lblAcknowledgements).row();
        lkLibGdx = new LinkLabel("libGDX - APLv2", "http://www.apache.org/licenses/LICENSE-2.0.html");
        table.add(lkLibGdx).row();
        lkKryonet = new LinkLabel("Kryonet - Copyright 2008 Nathan Sweet - BSD-3-Clause", "https://github.com/EsotericSoftware/kryonet/blob/master/license.txt");
        table.add(lkKryonet).row();
        lkProtoBuf = new LinkLabel("Protobuf - Copyright 2008 Google Inc. - BSD-3-Clause", "https://github.com/google/protobuf/blob/master/LICENSE");
        table.add(lkProtoBuf).row();
        lblModifyInfo = new VisLabel("The game is modified by Mingqi Wang. Replaced Kryonet with HMS Nearby Service: ");
        table.add(lblModifyInfo).row();
        lkNearby = new LinkLabel("HMS Nearby Service", "https://developer.huawei.com/consumer/en/codelab/HUAWEINearbyConnectionKit/index.html#0");
        table.add(lkNearby).row();

        if (Locale.getDefault().getLanguage().equals("zh")) {
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

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
