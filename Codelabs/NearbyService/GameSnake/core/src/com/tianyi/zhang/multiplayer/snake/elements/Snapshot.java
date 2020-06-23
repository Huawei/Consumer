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
import com.tianyi.zhang.multiplayer.snake.protobuf.generated.ClientPacket;
import com.tianyi.zhang.multiplayer.snake.protobuf.generated.ServerPacket;

import java.util.*;

public abstract class Snapshot {
    public abstract boolean update();

    public void onClientInput(int direction) {

    }

    public void onServerInput(int direction) {

    }

    public void onClientMessage(int clientId, ClientPacket.Message message) {

    }

    public void onServerUpdate(ServerPacket.Update update) {

    }

    /**
     *
     * @param snakes
     * @return whether some snake has collided with itself, other snakes or wooden crates
     */
    public boolean collisionDetection(List<Snake> snakes) {
        SortedMap<Integer, Integer> positions = new TreeMap<Integer, Integer>();
        boolean result = false;
        for (int i = 0; i < snakes.size(); ++i) {
            if (!snakes.get(i).isDead()) {
                List<Integer> coordinates = snakes.get(i).getCoordinates();
                for (int j = 0; j < coordinates.size(); j += 2) {
                    Integer position = Utils.positionFromXy(coordinates.get(j), coordinates.get(j+1));
                    Integer count = positions.get(position);
                    if (count != null) {
                        positions.put(position, count + 1);
                    } else {
                        positions.put(position, 1);
                    }
                }
            }
        }
        for (int i = 0; i < snakes.size(); ++i) {
            if (!snakes.get(i).isDead()) {
                int headX = snakes.get(i).getHeadX(), headY = snakes.get(i).getHeadY();
                if (headX <= 0 || headX >= Constants.WIDTH - 1 || headY <= 0 || headY >= Constants.HEIGHT - 1) {
                    snakes.get(i).die();
                    result = true;
                    continue;
                }

                Integer headPosition = Utils.positionFromXy(headX, headY);
                Integer count = positions.get(headPosition);
                if (count > 1) {
                    snakes.get(i).die();
                    result = true;
                }
            }
        }
        return result;
    }

    public abstract Grid getGrid();
}
