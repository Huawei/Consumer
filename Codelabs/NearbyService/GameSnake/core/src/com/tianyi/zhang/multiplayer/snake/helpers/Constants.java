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

public class Constants {
    public static final int TCP_PORT = 21345;
    public static final int UDP_PORT = 21346;

    public static final byte LEFT = 1;
    public static final byte RIGHT = 4;
    public static final byte UP = 2;
    public static final byte DOWN = 3;

    /**
     * Server would acknowledge inputs sent within LAG_TOLERANCE_MS
     */
    public static final int LAG_TOLERANCE_MS = 500;

    public static int MOVE_EVERY_MS = 500;
    public static final int SEARCH_TIMEOUT_MS = 2000;

    public static final long SEED = Utils.getNanoTime();

    public static final int INITIAL_SNAKE_LENGTH = 3;
    public static final int MAX_FOOD_QUANTITY = 10;
    public static final int MIN_FOOD_QUANTITY = 3;

    public static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
    public static final int WIDTH = 40, HEIGHT = 30;

    public static final float BLOCK_LENGTH = 1f;
    public static final float BLOCK_OFFSET = 8f / 24f;

    public enum GameResult {
        WON, LOST
    }

    public static final String GAME_OVER = "You Lose!";
    public static final String CONGRATS = "You Win!";

    public static final String VERSION = "v1.0.13";
}
