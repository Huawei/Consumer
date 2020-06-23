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

package com.tianyi.zhang.multiplayer.snake.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tianyi.zhang.multiplayer.snake.elements.Grid;

import java.util.HashMap;
import java.util.Map;

import static com.tianyi.zhang.multiplayer.snake.elements.Grid.Block.*;
import static com.tianyi.zhang.multiplayer.snake.elements.Grid.Block.CRATE;

public enum AssetManager {
    INSTANCE;

    private final Map<Grid.Block, Sprite> spriteMap;
    private final Texture title;

    private final Color backgroundColor;

    public final Texture CREATE_GAME = Utils.newTextureWithLinearFilter("CREATE_GAME.png");
    public final Texture JOIN_GAME = Utils.newTextureWithLinearFilter("JOIN_GAME.png");
    public final Texture ABOUT = Utils.newTextureWithLinearFilter("ABOUT.png");
    public final Texture START_GAME = Utils.newTextureWithLinearFilter("START_GAME.png");
    public final Texture BACK_TO_MAIN = Utils.newTextureWithLinearFilter("BACK_TO_MAIN.png");
    public final Texture WAIT_FOR_JOIN = Utils.newTextureWithLinearFilter("WAIT_FOR_JOIN.png");
    public final Texture PLAYER_JOINED_NUM = Utils.newTextureWithLinearFilter("PLAYER_JOINED_NUM.png");
    public final Texture LOOK_FOR_HOST = Utils.newTextureWithLinearFilter("LOOK_FOR_HOST.png");
    public final Texture PLAYER_JOINED = Utils.newTextureWithLinearFilter("PLAYER_JOINED.png");
    public final Texture JOIN_SUCCESS = Utils.newTextureWithLinearFilter("JOIN_SUCCESS.png");
    public final Texture WIN_WORD = Utils.newTextureWithLinearFilter("WIN_WORD.png");
    public final Texture LOSE_WORD = Utils.newTextureWithLinearFilter("LOSE_WORD.png");
    public final Texture GAME_SPEED = Utils.newTextureWithLinearFilter("GAME_SPEED.png");
    public final Texture INTRO_BTN = Utils.newTextureWithLinearFilter("INTRO_BTN.png");
    public final Texture INTRO_DETAIL = Utils.newTextureWithLinearFilter("INTRO_DETAIL.png");
    public final Texture INTRO_PIC = Utils.newTextureWithLinearFilter("INTRO_PIC.png");

    private AssetManager() {
        Sprite playerSnakeBody = new Sprite(Utils.newTextureWithLinearFilter("player_snake_body.png"));
        Sprite snakeBody = new Sprite(Utils.newTextureWithLinearFilter("snake_body.png"));
        Sprite food = new Sprite(Utils.newTextureWithLinearFilter("cake.png"));
        Sprite ground = new Sprite(Utils.newTextureWithLinearFilter("ground.png"));
        Sprite crate = new Sprite(Utils.newTextureWithLinearFilter("wooden_crate.png"));
        Sprite deadPlayerSnakeBody = new Sprite(Utils.newTextureWithLinearFilter("dead_player_snake_body.png"));
        Sprite deadSnakeBody = new Sprite(Utils.newTextureWithLinearFilter("dead_snake_body.png"));

        spriteMap = new HashMap<Grid.Block, Sprite>();
        spriteMap.put(PLAYER_SNAKE_BODY, playerSnakeBody);
        spriteMap.put(SNAKE_BODY, snakeBody);
        spriteMap.put(FOOD, food);
        spriteMap.put(GROUND, ground);
        spriteMap.put(CRATE, crate);
        spriteMap.put(DEAD_PLAYER_SNAKE_BODY, deadPlayerSnakeBody);
        spriteMap.put(DEAD_SNAKE_BODY, deadSnakeBody);

        title = Utils.newTextureWithLinearFilter("game_title.png");

        backgroundColor = new Color(0x124E10ff);
    }

    public Sprite getSpriteByType(Grid.Block type) {
        if (type == null) return null;
        return spriteMap.get(type);
    }

    public Texture getTitleTexture() {
        return title;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
}
