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

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.tianyi.zhang.multiplayer.snake.helpers.Constants;
import com.tianyi.zhang.multiplayer.snake.helpers.Utils;
import com.tianyi.zhang.multiplayer.snake.protobuf.generated.ClientPacket;
import com.tianyi.zhang.multiplayer.snake.protobuf.generated.ServerPacket;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ServerSnapshot extends Snapshot {
    private static final String TAG = ServerSnapshot.class.getCanonicalName();
    private final long startTimestamp;
    private final int serverId;
    public static  long SNAKE_MOVE_EVERY_NS = TimeUnit.MILLISECONDS.toNanos(Constants.MOVE_EVERY_MS);
    private static final long LAG_TOLERANCE_NS = TimeUnit.MILLISECONDS.toNanos(Constants.LAG_TOLERANCE_MS);

    private final AtomicReference<ServerPacket.Update.Builder> lastPacket;

    private final Object lock;
    /**
     * Makes up the last game step, guarded by lock
     */
    private final List<Snake> snakes;
    private final Foods foods;
    private final List<SortedSet<Input>> inputBuffers;
    private long stateTime;
    private int nextInputId;
    public int version;

    public ServerSnapshot(long startTimestamp, int[] snakeIds) {
        this.startTimestamp = startTimestamp;
        Gdx.app.debug(TAG, String.format("startTimestamp: %,d", startTimestamp));
        serverId = 0;
        lock = new Object();
        stateTime = 0;
        nextInputId = 1;
        version = 0;
        int arraySize = snakeIds[snakeIds.length - 1] + 1;
        snakes = new ArrayList<Snake>(arraySize);
        foods = new Foods();
        inputBuffers = new ArrayList<SortedSet<Input>>(arraySize);

        int interval = (Constants.HEIGHT - 2 * Constants.INITIAL_SNAKE_LENGTH) / (snakeIds.length - 1);
        int evenSnakeIdHeadX = Constants.INITIAL_SNAKE_LENGTH * 2 - 1;
        int oddSnakeIdHeadX = Constants.WIDTH - 2 * Constants.INITIAL_SNAKE_LENGTH;

        int index = 0;
        for (int i = 0; i < arraySize; ++i) {
            if (snakeIds[index] == i) {
                if (index % 2 == 0) {
                    snakes.add(new Snake(i, evenSnakeIdHeadX, Constants.INITIAL_SNAKE_LENGTH + index * interval - 1, Constants.INITIAL_SNAKE_LENGTH, new Input(Constants.RIGHT, 0, 0)));
                } else {
                    snakes.add(new Snake(i, oddSnakeIdHeadX, Constants.INITIAL_SNAKE_LENGTH + index * interval - 1, Constants.INITIAL_SNAKE_LENGTH, new Input(Constants.LEFT, 0, 0)));
                }
                index += 1;
            } else {
                snakes.add(Snake.ghostSnake(i));
            }
            inputBuffers.add(new TreeSet<Input>(Input.comparator));
        }
        foods.generate(snakes);
        lastPacket = new AtomicReference<ServerPacket.Update.Builder>(null);
    }

    @Override
    public boolean update() {
        return true;
    }

    @Override
    public void onServerInput(int direction) {
        synchronized (lock) {
            long inputTime = Utils.getNanoTime() - startTimestamp;
            Input newInput = new Input(direction, nextInputId++, inputTime);
            SortedSet<Input> serverInputBuffer = inputBuffers.get(serverId);
            Input lastInput = serverInputBuffer.isEmpty() ? snakes.get(serverId).getLastInput() : serverInputBuffer.last();
            if (lastInput.isValidNewInput(newInput)) {
                serverInputBuffer.add(newInput);
                version += 1;
            }
        }
    }

    @Override
    public void onClientMessage(int clientId, ClientPacket.Message message) {
        long currentTime = Utils.getNanoTime() - startTimestamp;

        List<ClientPacket.Message.PInput> newPInputs = message.getInputsList();
        List<Input> newInputs = new ArrayList<Input>(newPInputs.size());
        for (ClientPacket.Message.PInput pInput : newPInputs) {
//            if (currentTime > pInput.getTimestamp() && currentTime - pInput.getTimestamp() <= LAG_TOLERANCE_NS) {
//                newInputs.add(Input.fromProtoInput(pInput));
//                Log.e("wmq", "onClientMessage.Server accepted input. input timestamp:"  +  pInput.getTimestamp() + "  server timestamp:" + currentTime);
//            } else {
//                Gdx.app.debug(TAG, "Rejected input " + pInput.getId() + " from client " + clientId);
//                Log.e("wmq", "onClientMessage.Server rejected input. input timestamp:"  +  pInput.getTimestamp() + "  server timestamp:" + currentTime);
//            }
            newInputs.add(Input.fromProtoInput(pInput));
        }

        if (!newInputs.isEmpty()) {
            synchronized (lock) {
                if (inputBuffers.get(clientId).addAll(newInputs)) {
                    Gdx.app.debug(TAG, "New update received from client " + clientId);
                    Gdx.app.debug(TAG, message.toString());
                    version += 1;
                }
            }
        }
    }

    public void onClientDisconnected(int clientId) {
        synchronized (lock) {
            if (clientId > 0 && clientId < snakes.size()) {
                snakes.get(clientId).die();
                version += 1;
            }
        }
    }

    public int getVersion() {
        synchronized (lock) {
            return version;
        }
    }

    @Override
    public Grid getGrid() {
        SNAKE_MOVE_EVERY_NS = TimeUnit.MILLISECONDS.toNanos(Constants.MOVE_EVERY_MS);
        long currentTime = Utils.getNanoTime() - startTimestamp;
        int currentStep = (int) (currentTime / SNAKE_MOVE_EVERY_NS);
        long updatedStateTime = (currentTime - LAG_TOLERANCE_NS) > stateTime ? currentTime - LAG_TOLERANCE_NS : stateTime;
        int updatedStateStep = (int) (updatedStateTime / SNAKE_MOVE_EVERY_NS);
        List<Snake> resultSnakes;
        Foods resultFoods;

        synchronized (lock) {
            int stateStep = (int) (stateTime / SNAKE_MOVE_EVERY_NS);
            int stepDiff = updatedStateStep - stateStep;
            // Update the game state
            for (int i = 0; i <= stepDiff; ++i) {
                for (int j = 0; j < snakes.size(); ++j) {
                    Snake currSnake = snakes.get(j);
                    if (currSnake.isDead()) continue;
                    Input tmpInput;
                    long upper = (i == stepDiff ? updatedStateTime : SNAKE_MOVE_EVERY_NS * (stateStep + i + 1));
                    while (!inputBuffers.get(j).isEmpty() && (tmpInput = inputBuffers.get(j).first()).timestamp < upper) {
                        currSnake.handleInput(tmpInput);
                        inputBuffers.get(j).remove(tmpInput);
                    }
                    if (i != stepDiff) {
                        currSnake.forward();
                        foods.consumedBy(currSnake);
                        if (collisionDetection(snakes)) {
                            version += 1;
                        }
                    }
                }
            }

            if (foods.shouldGenerate()) {
                foods.generate(snakes);
                version += 1;
            }

            // Make a copy of the game state
            resultSnakes = new ArrayList<Snake>(snakes.size());
            for (int i = 0; i < snakes.size(); ++i) {
                resultSnakes.add(new Snake(snakes.get(i)));
            }
            resultFoods = new Foods(foods);

            stepDiff = currentStep - updatedStateStep;
            Queue<Input>[] inputQueues = new Queue[inputBuffers.size()];
            for (int i = 0; i < inputQueues.length; ++i) {
                inputQueues[i] = new ArrayDeque<Input>(inputBuffers.get(i));
            }
            // Update the copied game state
            for (int i = 0; i <= stepDiff; ++i) {
                for (int j = 0; j < resultSnakes.size(); ++j) {
                    Snake currSnake = resultSnakes.get(j);
                    if (currSnake.isDead()) continue;
                    long upper = (i == stepDiff ? currentTime : SNAKE_MOVE_EVERY_NS * (updatedStateStep + i + 1));
                    while (!inputQueues[j].isEmpty() && inputQueues[j].peek().timestamp < upper) {
                        currSnake.handleInput(inputQueues[j].poll());
                    }
                    if (i != stepDiff) {
                        currSnake.forward();
                        resultFoods.consumedBy(currSnake);
                    }
                }
            }
            stateTime = updatedStateTime;
        }

        return new Grid(currentTime, resultSnakes, serverId, resultFoods);
    }
}
