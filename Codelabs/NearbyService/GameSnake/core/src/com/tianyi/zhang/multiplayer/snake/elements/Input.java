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
import com.tianyi.zhang.multiplayer.snake.protobuf.generated.ClientPacket;

import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class Input implements Comparable<Input> {
    public final int direction;
    public final int id;
    public final long timestamp;
    public final int step;

    public static final Comparator<Input> comparator = new InputComparator();
    private static long STEP_LENGTH = TimeUnit.MILLISECONDS.toNanos(Constants.MOVE_EVERY_MS);

    public Input(int direction, int id, long timestamp) {
        this.direction = direction;
        this.id = id;
        this.timestamp = timestamp;
        STEP_LENGTH = TimeUnit.MILLISECONDS.toNanos(Constants.MOVE_EVERY_MS);
        this.step = (int) (timestamp / STEP_LENGTH);
    }

    public static Input fromProtoInput(ClientPacket.Message.PInput pInput) {
        return new Input(pInput.getDirection(), pInput.getId(), pInput.getTimestamp());
    }

    public ClientPacket.Message.PInput.Builder toProtoInput() {
        return ClientPacket.Message.PInput.newBuilder().setId(id).setDirection(direction).setTimestamp(timestamp);
    }

    public boolean isValidNewInput(Input newInput) {
        if (newInput == null) {
            return false;
        }

        if (this.direction == newInput.direction || this.id >= newInput.id || this.step > newInput.step || this.timestamp >= newInput.timestamp) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(Input input) {
        if (this.id < input.id) {
            return -1;
        } else if (this.id > input.id) {
            return 1;
        } else {
            return 0;
        }
    }

    private static final class InputComparator implements Comparator<Input> {
        @Override
        public int compare(Input input, Input t1) {
            return input.compareTo(t1);
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (o instanceof Input) {
            return this.id == ((Input) o).id;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String str = String.format("Input: ID %d, direction %d, step %d", id, direction, step);
        return str;
    }
}
