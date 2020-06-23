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

package com.tianyi.zhang.multiplayer.snake.elements;

import com.tianyi.zhang.multiplayer.snake.helpers.Utils;

import java.util.Arrays;
import java.util.List;

import static com.tianyi.zhang.multiplayer.snake.helpers.Constants.WIDTH;
import static com.tianyi.zhang.multiplayer.snake.helpers.Constants.HEIGHT;

public class Grid {
    public final long timestamp;
    public final List<Snake> snakes;
    public final Foods foods;

    private final int aliveCount;

    public enum Block {
        GROUND(1 << 0), CRATE(1 << 1), FOOD(1 << 2), SNAKE_BODY(1 << 3),
        PLAYER_SNAKE_BODY(1 << 4), DEAD_SNAKE_BODY(1 << 5), DEAD_PLAYER_SNAKE_BODY(1 << 6);

        public final int mask;

        Block(int mask) {
            this.mask = mask;
        }
    }

    private final int[] blockArr;

    public Grid(long timestamp, List<Snake> snakes, int playerIndex, Foods foods) {
        this.timestamp = timestamp;
        this.snakes = snakes;
        this.foods = foods;

        int size = HEIGHT * WIDTH;
        blockArr = new int[size];
        Arrays.fill(blockArr, Block.GROUND.mask);
        for (int i = 0; i < HEIGHT; ++i) {
            blockArr[Utils.positionFromXy(0, i)] |= Block.CRATE.mask;
            blockArr[Utils.positionFromXy(WIDTH-1, i)] |= Block.CRATE.mask;
        }
        for (int i = 1; i < WIDTH - 1; ++i) {
            blockArr[Utils.positionFromXy(i, 0)] |= Block.CRATE.mask;
            blockArr[Utils.positionFromXy(i, HEIGHT-1)] |= Block.CRATE.mask;
        }

        List<Integer> positions = foods.getLinearPositions();
        for (int i = 0; i < positions.size(); ++i) {
            blockArr[positions.get(i)] |= Block.FOOD.mask;
        }

        int count = 0;
        for (int i = 0; i < snakes.size(); ++i) {
            if (!snakes.get(i).isDead()) {
                count += 1;
            }
            positions = snakes.get(i).getLinearPositions();
            boolean dead = snakes.get(i).isDead();
            if (i != playerIndex) {
                if (!dead) {
                    for (int j = 0; j < positions.size(); ++j) {
                        blockArr[positions.get(j)] |= Block.SNAKE_BODY.mask;
                    }
                } else {
                    for (int j = 0; j < positions.size(); ++j) {
                        blockArr[positions.get(j)] |= Block.DEAD_SNAKE_BODY.mask;
                    }
                }
            } else {
                if (!dead) {
                    for (int j = 0; j < positions.size(); ++j) {
                        blockArr[positions.get(j)] |= Block.PLAYER_SNAKE_BODY.mask;
                    }
                } else {
                    for (int j = 0; j < positions.size(); ++j) {
                        blockArr[positions.get(j)] |= Block.DEAD_PLAYER_SNAKE_BODY.mask;
                    }
                }
            }
        }
        aliveCount = count;
    }

    public boolean checkCoordinate(int x, int y, Block blockType) {
        return (blockArr[Utils.positionFromXy(x, y)] & blockType.mask) > 0;
    }

    public int getType(int x, int y) {
        return blockArr[Utils.positionFromXy(x, y)];
    }

    public boolean isSnakeDead(int index) {
        return snakes.get(index).isDead();
    }

    public int getAliveCount() {
        return aliveCount;
    }
}
