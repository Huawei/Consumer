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

package com.tianyi.zhang.multiplayer.snake;

import android.content.Context;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.minlog.Log;
import com.huawei.hms.nearby.Nearby;
import com.huawei.hms.nearby.discovery.DiscoveryEngine;
import com.huawei.hms.nearby.transfer.TransferEngine;
import com.kotcrab.vis.ui.VisUI;
import com.tianyi.zhang.multiplayer.snake.agents.Client;
import com.tianyi.zhang.multiplayer.snake.agents.IAgent;
import com.tianyi.zhang.multiplayer.snake.agents.Server;
import com.tianyi.zhang.multiplayer.snake.helpers.Constants;
import com.tianyi.zhang.multiplayer.snake.states.ErrorState;
import com.tianyi.zhang.multiplayer.snake.states.GameState;
import com.tianyi.zhang.multiplayer.snake.states.TitleScreenState;

import java.util.Stack;

public class App extends Game {
    protected Stack<GameState> stateStack;
    protected IAgent agent;
    private TransferEngine mTransferEngine = null;
    private DiscoveryEngine mDiscoveryEngine = null;
    private Context mContext;

//    public App(TransferEngine mTransferEngine, DiscoveryEngine mDiscoveryEngine) {
//        this.mTransferEngine = mTransferEngine;
//        this.mDiscoveryEngine = mDiscoveryEngine;
//    }

    public App(Context context) {
        mContext = context;
        //        mDiscoveryEngine =
//        mTransferEngine =
        this.mTransferEngine = Nearby.getTransferEngine(context);;
        this.mDiscoveryEngine =  Nearby.getDiscoveryEngine(context);
    }

    public IAgent getAgent() {
        return agent;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_NONE);
        Log.set(Log.LEVEL_NONE);
        VisUI.load(VisUI.SkinScale.X2);
        VisUI.setDefaultTitleAlign(Align.center);

        stateStack = new Stack<GameState>();
        pushState(new TitleScreenState(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        destroyAgent();
        while (!stateStack.empty()) {
            stateStack.pop().dispose();
        }
        android.util.Log.d("wmq", "VISUI dispose");
        VisUI.dispose();
    }

    public void pushState(GameState gameState) {
        if (!stateStack.isEmpty()) {
            stateStack.peek().pause();
        }
        stateStack.push(gameState);
        setScreen(gameState);
    }

    public void popState() {
        stateStack.pop().dispose();
        setScreen(stateStack.peek());
        stateStack.peek().resume();
    }

    public void setState(GameState gameState) {
        stateStack.pop().dispose();
        pushState(gameState);
        android.util.Log.d("wmq", "Set game state to: " + gameState.toString() );
    }

    public GameState getCurState()
    {
        return stateStack.peek();
    }

    public void gotoTitleScreen() {
        while (stateStack.size() > 1) {
            popState();
        }
        if (!stateStack.empty())
        {
            GameState state = stateStack.peek();
            if (state instanceof TitleScreenState)
            {

                if (Constants.MOVE_EVERY_MS == 500)
                {
                    ((TitleScreenState) state).speedSlider.setValue(0);
                }
                else if (Constants.MOVE_EVERY_MS == 333)
                {
                    ((TitleScreenState) state).speedSlider.setValue(1);
                }
                else if (Constants.MOVE_EVERY_MS == 250)
                {
                    ((TitleScreenState) state).speedSlider.setValue(2);
                }
            }
        }
        destroyAgent();
    }

    public void gotoErrorScreen(String errorMessage) {
        while (stateStack.size() > 1) {
            popState();
        }
        destroyAgent();
        pushState(new ErrorState(this, errorMessage));
    }

    public void initAgent(boolean isServer) {
        if (isServer) {
            agent = new Server(mTransferEngine, mDiscoveryEngine, this);
        } else {
            agent = new Client(mTransferEngine, mDiscoveryEngine, this);
        }
    }

    public void destroyAgent() {
        if (agent != null) {
            agent.destroy();
        }
    }
}
