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

import com.tianyi.zhang.multiplayer.snake.helpers.Constants;
import com.tianyi.zhang.multiplayer.snake.helpers.Utils;
import com.tianyi.zhang.multiplayer.snake.protobuf.generated.ServerPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Snake {
    public final int id;
    private final List<Integer> coords;
    private int lastDirection;
    private Input lastInput;
    private boolean isDead;

    public Snake(int id, int[] coords, int lastDirection, Input input) {
        this.id = id;
        this.coords = new LinkedList<Integer>();
        for (int i = 0; i < coords.length; ++i) {
            this.coords.add(new Integer(coords[i]));
        }
        this.lastDirection = lastDirection;
        this.lastInput = input;
        this.isDead = false;
    }

    public Snake(int id, List<Integer> coords, int lastDirection, Input input, boolean isDead) {
        this.id = id;
        this.coords = new LinkedList<Integer>(coords);
        this.lastDirection = lastDirection;
        this.lastInput = input;
        this.isDead = isDead;
    }

    public Snake(int id, int headX, int headY, int length, Input input) {
        this.id = id;
        this.lastInput = input;

        this.coords = new LinkedList<Integer>();
        int x = headX, y = headY;
        for (int i = 0; i < length; ++i) {
            coords.add(Integer.valueOf(x));
            coords.add(Integer.valueOf(y));
            switch (input.direction) {
                case Constants.LEFT:
                    x += 1;
                    break;
                case Constants.UP:
                    y -= 1;
                    break;
                case Constants.RIGHT:
                    x -= 1;
                    break;
                case Constants.DOWN:
                    y += 1;
                    break;
            }
        }
        this.lastDirection = input.direction;
        this.isDead = false;
    }

    public static Snake ghostSnake(int id) {
        return new Snake(id, new ArrayList<Integer>(0), Constants.RIGHT, new Input(Constants.RIGHT, 0, 0), true);
    }

    public static Snake fromProtoSnake(ServerPacket.Update.PSnake pSnake) {
        Input input = new Input(pSnake.getInputDirection(), pSnake.getInputId(), pSnake.getInputTimestamp());
        List<Integer> positions = pSnake.getCoordsList();
        List<Integer> xyCoords = new ArrayList<Integer>(positions.size() * 2);
        for (int i = 0; i < positions.size(); ++i) {
            xyCoords.add(Utils.xFromPosition(positions.get(i)));
            xyCoords.add(Utils.yFromPosition(positions.get(i)));
        }
        Snake snake = new Snake(pSnake.getId(), xyCoords, pSnake.getLastDirection(), input, pSnake.getIsDead());
        return snake;
    }

    public ServerPacket.Update.PSnake.Builder toProtoSnake() {
        // TODO: convert x/y coordinates to linear coordinates to save bandwidth
        ServerPacket.Update.PSnake.Builder snakeBuilder = ServerPacket.Update.PSnake.newBuilder();
        snakeBuilder.setId(id).setLastDirection(lastDirection).setIsDead(isDead).addAllCoords(getLinearPositions())
                .setInputId(lastInput.id).setInputDirection(lastInput.direction).setInputTimestamp(lastInput.timestamp);
        return snakeBuilder;
    }



    public Snake(Snake snake) {
        this.id = snake.id;
        this.coords = new LinkedList<Integer>(snake.coords);
        this.lastInput = snake.lastInput;
        this.lastDirection = snake.lastDirection;
        this.isDead = snake.isDead;
    }

    public void forward() {
        if (!isDead) {
            lastDirection = lastInput.direction;

            int size = coords.size();
            coords.remove(size - 1);
            coords.remove(size - 2);
            int x0 = coords.get(0).intValue(), y0 = coords.get(1).intValue();
            switch (lastInput.direction) {
                case Constants.LEFT:
                    --x0;
                    break;
                case Constants.UP:
                    ++y0;
                    break;
                case Constants.RIGHT:
                    ++x0;
                    break;
                case Constants.DOWN:
                    --y0;
                    break;
            }
            coords.add(0, y0);
            coords.add(0, x0);
        }
    }

    public void grow() {
        if (!isDead) {
            int size = coords.size();
            coords.add(coords.get(size - 2));
            coords.add(coords.get(size - 1));
        }
    }

    public int getHeadX() {
        return coords.get(0);
    }

    public int getHeadY() {
        return coords.get(1);
    }

    public List<Integer> getCoordinates() {
        return Collections.unmodifiableList(coords);
    }

    public List<Integer> getLinearPositions() {
        List<Integer> positions = new ArrayList<Integer>(coords.size() / 2);
        for (int i = 0; i < coords.size(); i += 2) {
            int x = coords.get(i), y = coords.get(i+1);
            if (x >= 0 && x < Constants.WIDTH && y >= 0 && y < Constants.HEIGHT) {
                positions.add(Utils.positionFromXy(x, y));
            }
        }
        return positions;
    }

    public void die() {
        this.isDead = true;
    }

    public boolean isDead() {
        return this.isDead;
    }

    public void handleInput(Input input) {
        if (!isDead && lastInput.isValidNewInput(input) && lastDirection + input.direction != 5) {
            this.lastInput = input;
        }
    }

    public Input getLastInput() {
        return lastInput;
    }

    @Override
    public String toString() {
        String str = String.format("%s snake %d, direction %d, last input ID %d, head coordinates (%d, %d).",
                isDead ? "Dead" : "Live", id, lastInput.direction, lastInput.id, coords.get(0), coords.get(1));
        return str;
    }
}
