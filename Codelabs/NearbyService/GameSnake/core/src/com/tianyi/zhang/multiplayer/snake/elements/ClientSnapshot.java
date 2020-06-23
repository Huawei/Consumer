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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClientSnapshot extends Snapshot {
    private static final String TAG = ClientSnapshot.class.getCanonicalName();

    private final long startTimestamp;
    private final int clientId;
    public static long SNAKE_MOVE_EVERY_NS = TimeUnit.MILLISECONDS.toNanos(Constants.MOVE_EVERY_MS);
    private static final long LAG_TOLERANCE_NS = TimeUnit.MILLISECONDS.toNanos(Constants.LAG_TOLERANCE_MS);

    private final AtomicInteger serverUpdateVersion;

    private final AtomicLong nextRenderTime;

    private final Object lock;
    /**
     * Makes up the last game step, guarded by stateLock
     */
    private final List<Snake> snakes;
    private final Foods foods;
    private long stateTime;
    private int nextInputId;
    private final List<Input> unackInputs;
    private long lastForceUpdateTime = 0;

    public ClientSnapshot(int clientId, long initialUpdateNanoTime, ServerPacket.Update initialUpdate) {
        this.clientId = clientId;
        this.lock = new Object();
        this.nextInputId = 1;
        this.unackInputs = new LinkedList<Input>();
        this.stateTime = 0;
        this.serverUpdateVersion = new AtomicInteger(initialUpdate.getVersion());
        this.nextRenderTime = new AtomicLong(0);
        this.startTimestamp = initialUpdateNanoTime - initialUpdate.getTimestamp();

        Gdx.app.debug(TAG, String.format("startTimestamp: %,d", startTimestamp));

        List<ServerPacket.Update.PSnake> pSnakes = initialUpdate.getSnakesList();
        List<Snake> snakes = new ArrayList<Snake>(pSnakes.size());
        for (ServerPacket.Update.PSnake pSnake : pSnakes) {
            snakes.add(Snake.fromProtoSnake(pSnake));
        }
        this.snakes = new ArrayList<Snake>(snakes);
        this.foods = new Foods();
        this.foods.setLocations(initialUpdate.getFoodLocationsList());
    }

    /**
     *
     * @return true if a new frame should be rendered, false otherwise
     */
    @Override
    public boolean update() {
        long currentNs = Utils.getNanoTime() - startTimestamp;
        if (currentNs >= nextRenderTime.get()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClientInput(int direction) {
        synchronized (lock) {
            long tmpNs = Utils.getNanoTime() - startTimestamp;
            Input input = new Input(direction, nextInputId++, tmpNs);
            Input lastInput = (unackInputs.isEmpty() ? snakes.get(clientId).getLastInput() : unackInputs.get(unackInputs.size()-1));
            if (lastInput.isValidNewInput(input)) {
                unackInputs.add(input);
            }
        }
    }

    @Override
    public void onServerUpdate(ServerPacket.Update update) {
        boolean needForceUpdate = false;
        //force update every 500ms
//        if (update.getTimestamp() - lastForceUpdateTime > 1000000000 )
//        {
//            needForceUpdate = true;
//            lastForceUpdateTime = update.getTimestamp();
//            Log.d("wmq", "Client force update "  );
//        }
        Log.d("clientSnapshot", "Server Update received. update version: " +
                update.getVersion() + "local version: " + serverUpdateVersion.get());
        if (update.getVersion() > serverUpdateVersion.get() ) {
            //Log.d("clientSnapshot", "Server Update accepted. ");
            Gdx.app.debug(TAG, "Server update version " + update.getVersion() + " received.");
            Gdx.app.debug(TAG, update.toString());
            serverUpdateVersion.set(update.getVersion());
            synchronized (lock) {
                List<ServerPacket.Update.PSnake> pSnakes = update.getSnakesList();
                for (int i = 0; i < pSnakes.size(); ++i) {
                    ServerPacket.Update.PSnake pSnake = pSnakes.get(i);
                    Snake newSnake = Snake.fromProtoSnake(pSnake);
                    snakes.set(newSnake.id, newSnake);

                    stateTime = update.getTimestamp();

                    if (newSnake.id == clientId) {
                        int lastAckInputId = newSnake.getLastInput().id;
//                        while (!unackInputs.isEmpty() && lastAckInputId <= unackInputs.get(0).id) {
//                            unackInputs.remove(0);
//                            Log.d("wmq", "removing unack Inputs 2.");
//                        }
                    }
                }

                List<Integer> foodLocations = update.getFoodLocationsList();
                foods.setLocations(foodLocations);
            }
        } else {
            synchronized (lock) {
                if (!unackInputs.isEmpty()) {
//                    Log.d("clientSnapshot", "Server Update rejected. update version: " +
//                           update.getVersion() + "local version: " + serverUpdateVersion.get());
//                    while (!unackInputs.isEmpty() && update.getTimestamp() - unackInputs.get(0).timestamp > LAG_TOLERANCE_NS * 2) {
//                        unackInputs.remove(0);
//                        Log.d("wmq", "removing unack Inputs. 1." );
//                    }
                }
            }
        }
    }

    public ClientPacket.Message buildMessage() {
        ClientPacket.Message.Builder builder = ClientPacket.Message.newBuilder();
        synchronized (lock) {
            if (unackInputs.size() == 0) {
                return null;
            }

            for (com.tianyi.zhang.multiplayer.snake.elements.Input tmpInput : unackInputs) {
                builder.addInputs(tmpInput.toProtoInput());
            }
        }
        return builder.build();
    }

    @Override
    public Grid getGrid() {
        SNAKE_MOVE_EVERY_NS = TimeUnit.MILLISECONDS.toNanos(Constants.MOVE_EVERY_MS);
        long currentTime = Utils.getNanoTime() - startTimestamp;
        int currentStep = (int) (currentTime / SNAKE_MOVE_EVERY_NS);

        List<Snake> resultSnakes;
        Foods resultFoods;
        synchronized (lock) {
            resultSnakes = new ArrayList<Snake>(snakes.size());
            for (int i = 0; i < snakes.size(); ++i) {
                resultSnakes.add(new Snake(snakes.get(i)));
            }
            resultFoods = new Foods(foods);

            int stateStep = (int) (stateTime / SNAKE_MOVE_EVERY_NS);
            int stepDiff = currentStep - stateStep;

            int inputIndex = 0;
            for (int i = 0; i <= stepDiff; ++i) {
                long upper = (i == stepDiff ? currentTime : SNAKE_MOVE_EVERY_NS * (stateStep + i + 1));
                for (int j = 0; j < resultSnakes.size(); ++j) {
                    Snake currSnake = resultSnakes.get(j);
                    if (currSnake.isDead()) continue;
                    if (j == clientId) {
                        Input tmpInput;
                        while (unackInputs.size() > inputIndex && (tmpInput = unackInputs.get(inputIndex)).timestamp < upper) {
                            currSnake.handleInput(tmpInput);
                            inputIndex += 1;
                        }
                    }
                    if (i != stepDiff) {
                        currSnake.forward();
                        resultFoods.consumedBy(currSnake);
                    }
                }
            }

            nextRenderTime.set(SNAKE_MOVE_EVERY_NS * (currentStep + 1));
        }
        return new Grid(Utils.getNanoTime(), resultSnakes, clientId, resultFoods);
    }
}
