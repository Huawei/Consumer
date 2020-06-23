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


package com.tianyi.zhang.multiplayer.snake.agents;

import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.tianyi.zhang.multiplayer.snake.App;
import com.tianyi.zhang.multiplayer.snake.elements.ServerSnapshot;
import com.tianyi.zhang.multiplayer.snake.helpers.Constants;
import com.huawei.hms.nearby.Nearby;
import com.huawei.hms.nearby.StatusCode;
import com.huawei.hms.nearby.discovery.BroadcastOption;
import com.huawei.hms.nearby.discovery.ConnectCallback;
import com.huawei.hms.nearby.discovery.ConnectInfo;
import com.huawei.hms.nearby.discovery.ConnectResult;
import com.huawei.hms.nearby.discovery.DiscoveryEngine;
import com.huawei.hms.nearby.discovery.Policy;
import com.huawei.hms.nearby.discovery.ScanEndpointCallback;
import com.huawei.hms.nearby.discovery.ScanEndpointInfo;
import com.huawei.hms.nearby.discovery.ScanOption;
import com.huawei.hms.nearby.transfer.Data;
import com.huawei.hms.nearby.transfer.DataCallback;
import com.huawei.hms.nearby.transfer.TransferEngine;
import com.huawei.hms.nearby.transfer.TransferStateUpdate;
import com.tianyi.zhang.multiplayer.snake.states.GameState;
import com.tianyi.zhang.multiplayer.snake.states.server.BroadcastState;
import com.tianyi.zhang.multiplayer.snake.states.server.SVMainGameState;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Server extends IAgent {
    private static final String TAG = Server.class.getCanonicalName();
    private com.esotericsoftware.kryonet.Server server;
    private Listener listener;
    private TransferEngine mTransferEngine = null;
    private DiscoveryEngine mDiscoveryEngine = null;
    private String myNameStr;
    private String myServiceId = "NearbySnakeServiceid";
    private Map<String, String> remoteIdNameMap = new HashMap<String, String>();
    private String mRemoteEndpointId;
    private App _app;
    private Map<String, Long> lagMap = new HashMap<String, Long>();

    public Server() {
        server = new com.esotericsoftware.kryonet.Server();
        server.getKryo().register(byte[].class);
        server.getKryo().register(FrameworkMessage.Ping.class);
        server.start();

    }

    public Server(TransferEngine mTransferEngine, DiscoveryEngine mDiscoveryEngine, App app) {
        this.mTransferEngine = mTransferEngine;
        this.mDiscoveryEngine = mDiscoveryEngine;
        _app = app;

//        server = new com.esotericsoftware.kryonet.Server();
//        server.getKryo().register(byte[].class);
//        server.getKryo().register(FrameworkMessage.Ping.class);
//        server.start();
    }

    @Override
    public void setListener(Listener l) {
//        server.addListener(l);
//        if (this.listener != null) {
//            server.removeListener(this.listener);
//        }
//        this.listener = l;
    }

    @Override
    public void broadcast(Listener listener) throws IOException {

        Log.d("wmq", "Start Nearby broadcast.");
        try {
            doStartBroadcast();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d("wmq", "leave Nearby broadcast.");

        //setListener(listener);
        //server.bind(Constants.TCP_PORT, Constants.UDP_PORT);

    }

    @Override
    public void lookForServer(Listener listener, Runnable errorCallback) {

    }

    @Override
    public void send(byte[] packet) {

        //server.sendToAllUDP(packet);
        Data data = Data.fromBytes(packet);
        Log.d("wmq", "Server sent data to id:" + mRemoteEndpointId + " Data id: " + data.getId());

        for (String endpointId : remoteIdNameMap.keySet()) {
            mTransferEngine.sendData(endpointId, data);
        }

    }

    @Override
    public void destroy() {
//        if (listener != null) {
//            server.removeListener(listener);
//            listener = null;
//        }
//        server.close();
        mDiscoveryEngine.stopBroadcasting();
        mDiscoveryEngine.disconnectAll();
    }

    @Override
    public void setLagDetecionStartTime(String seq, long ms) {
        lagMap.put(seq, ms);
    }

    public void doStartBroadcast() throws RemoteException {
        BroadcastOption.Builder advBuilder = new BroadcastOption.Builder();
        advBuilder.setPolicy(Policy.POLICY_STAR);
        myNameStr = "Speed" + Constants.MOVE_EVERY_MS;
        Log.d("wmq", "Broadcast " + myNameStr);
        mDiscoveryEngine.startBroadcasting(myNameStr, myServiceId, mConnCb, advBuilder.build());
    }

    private ConnectCallback mConnCb =
            new ConnectCallback() {
                @Override
                public void onEstablish(String endpointId, ConnectInfo connectionInfo) {
                    mDiscoveryEngine.acceptConnect(endpointId, mDataCb);
                    remoteIdNameMap.put(endpointId, connectionInfo.getEndpointName());
                    Log.d("wmq", "Nearby Server accept connection from: Id" + endpointId + "Name" + connectionInfo.getEndpointName());
                }

                @Override
                public void onResult(String endpointId, ConnectResult result) {
                    switch (result.getStatus().getStatusCode()) {
                        case StatusCode.STATUS_SUCCESS:
                            /* The connection was established successfully, we can exchange data. */
                            Log.d("wmq", "Nearby connection was established successfully. Remote ID:" + endpointId);
                            //TODO remove at Version 1.1.0
                            //mDiscoveryEngine.stopBroadcasting();
                            GameState state = _app.getCurState();
                            if (state instanceof BroadcastState)
                            {
                                BroadcastState broadcastState = (BroadcastState) state;
                                synchronized (broadcastState.connectionIdsLock) {
                                    String remoteEndpointNane = remoteIdNameMap.get(endpointId);
                                    broadcastState.connectionIds.add(Integer.valueOf(remoteEndpointNane));
                                    if (broadcastState.connectionIds.size() == 0) {
                                        broadcastState.btnStart.setDisabled(true);
                                        broadcastState.lblPlayerCount.setText("0");
                                    } else {

                                        broadcastState.lblPlayerCount.setText(String.format(broadcastState.PLAYERS_CONNECTED_FORMAT, broadcastState.connectionIds.size()));
                                        for (int i = 5; i > 0; i--)
                                        {
                                            String str = "1 other Snake has joined the game.\nYou can start game after " +   i   + " seconds.";
                                            broadcastState.btnStart.setText(String.format("\n          %d         \n", i));
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        broadcastState.btnStart.setText("\n        GO!        \n");
                                        broadcastState.btnStart.setDisabled(false);

                                    }
                                }
                            }

                            break;
                        case StatusCode.STATUS_CONNECT_REJECTED:
                            /* The Connection was rejected. */
                            break;
                        default:
                            /* other unknown status code. */
                    }
                    mRemoteEndpointId = endpointId;
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.d("wmq", "Nearby Client Disconnected from:" + endpointId);
                    GameState state = _app.getCurState();
                    if (state instanceof BroadcastState)
                    {
                        Log.d("wmq", "disconnect at broadcast state");
                        BroadcastState broadcastState = (BroadcastState) state;
                        synchronized (broadcastState.connectionIdsLock) {
                            String remoteEndpointName = remoteIdNameMap.get(endpointId);
                            broadcastState.connectionIds.remove(Integer.valueOf(remoteEndpointName));
                            if (broadcastState.connectionIds.size() == 0) {
                                Log.d("wmq", "no snakes left after disconnect");
                                broadcastState.btnStart.setDisabled(true);
                                broadcastState.lblPlayerCount.setText("0");
                                //TODO remove at Version 1.1.0
//                                try {
//                                    doStartBroadcast();
//                                } catch (RemoteException e) {
//                                    e.printStackTrace();
//                                }
                            } else {
                                Log.d("wmq", broadcastState.connectionIds.size() + " snakes left after disconnect");
                                broadcastState.btnStart.setDisabled(false);
                                broadcastState.lblPlayerCount.setText(String.format(broadcastState.PLAYERS_CONNECTED_FORMAT, broadcastState.connectionIds.size()));
                            }
                        }
                    }
                    else if (state instanceof SVMainGameState)
                    {
                        SVMainGameState svMainGameState = (SVMainGameState) state;
                        svMainGameState.serverSnapshot.onClientDisconnected(Integer.valueOf(endpointId));
                    }
                }
            };

    private DataCallback mDataCb =
            new DataCallback() {
                @Override
                public void onReceived(String endpointId, Data data) {

                    GameState state = _app.getCurState();
                    Log.d("wmq", "Server received data.from" + endpointId + " DataID: " + data.getId());
                    if (state instanceof SVMainGameState)
                    {
                        SVMainGameState mainGameState = (SVMainGameState) state;

                        String str = new String(data.asBytes());
                        if (str.contains("ServerLagDetection"))
                        {
                            long starttime = lagMap.get(str);
                            mainGameState.lag = ""+(System.currentTimeMillis() - starttime) + " ms";
                            Log.d("wmqLAG", mainGameState.lag);
                        }
                        else if(str.contains("ClientLagDetection"))
                        {
                            send(data.asBytes());
                        }
                        else {
                            byte[] bytes = data.asBytes();
                            ServerSnapshot serverSnapshot = ((SVMainGameState) state).getServerSnapshot();
                            String remoteEndpointNane = remoteIdNameMap.get(endpointId);
                            serverSnapshot.onClientMessage(Integer.valueOf(remoteEndpointNane), Server.parseClientMessage(bytes));
                        }
                    }
                }
                @Override
                public void onTransferUpdate(String string, TransferStateUpdate update) {
                }
            };
}
