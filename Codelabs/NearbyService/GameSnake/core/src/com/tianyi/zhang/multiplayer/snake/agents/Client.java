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
import com.esotericsoftware.kryonet.Listener;
import com.huawei.hms.nearby.StatusCode;
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
import com.tianyi.zhang.multiplayer.snake.App;
import com.tianyi.zhang.multiplayer.snake.elements.ClientSnapshot;
import com.tianyi.zhang.multiplayer.snake.helpers.Constants;
import com.tianyi.zhang.multiplayer.snake.helpers.Utils;
import com.tianyi.zhang.multiplayer.snake.states.GameState;
import com.tianyi.zhang.multiplayer.snake.states.client.LookForServerState;
import com.tianyi.zhang.multiplayer.snake.states.client.MainGameState;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Client extends IAgent {
    private com.esotericsoftware.kryonet.Client client;
    private Listener listener;
    private static final String TAG = Client.class.getCanonicalName();
    private TransferEngine mTransferEngine = null;
    private DiscoveryEngine mDiscoveryEngine = null;
    private final int randomEndpointName = (int) System.currentTimeMillis() % 1000 + 1000;
    private String myNameStr = "" + randomEndpointName;
    private String myServiceId = "NearbySnakeServiceid";
    private String mRemoteEndpointId;
    private App _app;
    private Map<String, Long> lagMap = new HashMap<String, Long>();

    public Client() {
        client = new com.esotericsoftware.kryonet.Client();
        client.getKryo().register(byte[].class);
        client.start();
    }

    public Client(TransferEngine mTransferEngine, DiscoveryEngine mDiscoveryEngine, App app) {
        this.mTransferEngine = mTransferEngine;
        this.mDiscoveryEngine = mDiscoveryEngine;
        this._app = app;

//        client = new com.esotericsoftware.kryonet.Client();
//        client.getKryo().register(byte[].class);
//        client.start();
    }

    @Override
    public void setListener(Listener l) {
//        client.addListener(l);
//        if (this.listener != null) {
//            client.removeListener(this.listener);
//        }
//        this.listener = l;
    }

    @Override
    public void broadcast(Listener listener) {

    }

    @Override
    public void lookForServer(Listener listener, final Runnable errorCallback) {

        Log.d("nearby", "Start Nearby scan. ");
        try {
            doStartScan();
        } catch (RemoteException e) {
            Gdx.app.error(TAG, e.getMessage());
            Gdx.app.postRunnable(errorCallback);
        }
        Log.d("nearby", "Leave Nearby scan. ");
//        setListener(listener);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                InetAddress serverAddress = client.discoverHost(Constants.UDP_PORT, Constants.SEARCH_TIMEOUT_MS);
//                if (serverAddress != null) {
//                    Gdx.app.debug(TAG, "Server discovered at " + serverAddress.getHostAddress());
//                    try {
//                        client.connect(Constants.SEARCH_TIMEOUT_MS, serverAddress, Constants.TCP_PORT, Constants.UDP_PORT);
//                    } catch (IOException e) {
//                        Gdx.app.error(TAG, e.getMessage());
//                        Gdx.app.postRunnable(errorCallback);
//                    }
//                } else {
//                    Gdx.app.debug(TAG, "No server found");
//                    Gdx.app.postRunnable(errorCallback);
//                }
//            }
//        }).start();



    }

    @Override
    public void send(byte[] packet) {
        //client.sendUDP(packet);
        Data data = Data.fromBytes(packet);
        Log.d("wmq", "Client sent data.to id:" + mRemoteEndpointId + " Data id: " + data.getId());
        mTransferEngine.sendData(mRemoteEndpointId, data);
    }

    @Override
    public void destroy() {
//        if (listener != null) {
//            client.removeListener(listener);
//            listener = null;
//        }
//        client.close();
        mDiscoveryEngine.stopScan();
        mDiscoveryEngine.disconnectAll();
    }

    @Override
    public void setLagDetecionStartTime(String seq, long ms) {
        lagMap.put(seq, ms);
    }

    public void doStartScan() throws RemoteException {
        ScanOption.Builder discBuilder = new ScanOption.Builder();
        discBuilder.setPolicy(Policy.POLICY_STAR);
        mDiscoveryEngine.startScan(myServiceId, mDiscCb, discBuilder.build());
    }

    private ConnectCallback mConnCb =
            new ConnectCallback() {
                @Override
                public void onEstablish(String endpointId, ConnectInfo connectionInfo) {
                    mDiscoveryEngine.acceptConnect(endpointId, mDataCb);
                    Log.d("wmq", "Nearby Client accept connection from:" + endpointId);
                    Log.d("wmq", "Scanner " + connectionInfo.getEndpointName() );
                    if (connectionInfo.getEndpointName().equals("Speed500"))
                    {
                        Constants.MOVE_EVERY_MS = 500;
                    }
                    else if (connectionInfo.getEndpointName().equals("Speed333"))
                    {
                        Constants.MOVE_EVERY_MS = 333;
                    }
                    else if (connectionInfo.getEndpointName().equals("Speed250"))
                    {
                        Constants.MOVE_EVERY_MS = 250;
                    }
                }

                @Override
                public void onResult(String endpointId, ConnectResult result) {
                    switch (result.getStatus().getStatusCode()) {
                        case StatusCode.STATUS_SUCCESS:
                            mDiscoveryEngine.stopScan();
                            GameState state = _app.getCurState();
                            if (state instanceof LookForServerState) {
                                LookForServerState lookforServerState = (LookForServerState) state;
                                /* The connection was established successfully, we can exchange data. */
                                Log.d("wmq", "Nearby connection was established successfully. Remote ID:" + endpointId);
                                //lookforServerState.lblInfo.setText("Joined successfully\nWaiting for host to start - game...");
                                lookforServerState.lblInfo.setText("OK!");
                                String str = new String("Hello");
                                send(str.getBytes());
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
                    if (state instanceof  LookForServerState)
                    {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                _app.gotoErrorScreen("No! We lost contact with the host!");
                            }
                        });
                    }
                    else if (state instanceof MainGameState)
                    {
                        MainGameState mainGameState = (MainGameState) state;
                        if (mainGameState.gameResult.get() == null) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    _app.gotoErrorScreen("No! We lost contact with the host!");
                                }
                            });
                        }
                    }
                }
            };

    private ScanEndpointCallback mDiscCb =
            new ScanEndpointCallback() {
                @Override
                public void onFound(String endpointId, ScanEndpointInfo discoveryEndpointInfo) {
                    mRemoteEndpointId = endpointId;
                    mDiscoveryEngine.requestConnect(myNameStr, mRemoteEndpointId, mConnCb);
                    Log.d("wmq", "Nearby Client found Server and request connection. Server id:" + endpointId);
                }

                @Override
                public void onLost(String endpointId) {
                    Log.d("wmq", "Nearby Lost endpoint: " + endpointId);
                }
            };

    private DataCallback mDataCb =
            new DataCallback() {
                @Override
                public void onReceived(String endpointId, Data data) {
                    //receiveMessage(data);
                    int value;
                    byte[] src = data.asBytes();
                    value = (int) ((src[0] & 0xFF)
                            | ((src[1] & 0xFF)<<8)
                            | ((src[2] & 0xFF)<<16)
                            | ((src[3] & 0xFF)<<24));
                    Log.d("wmq", "Client received data. from : " + endpointId + " Data id:" + data.getId());
                    GameState state = _app.getCurState();
                    if (state instanceof MainGameState)
                    {
                        String str = new String(data.asBytes());
                        MainGameState mainGameState = (MainGameState) state;
                        if (str.contains("ServerLagDetection"))
                        {
                            send(data.asBytes());
                        }
                        else if (str.contains("ClientLagDetection"))
                        {
                            long starttime = lagMap.get(str);
                            mainGameState.lag = ""+(System.currentTimeMillis() - starttime) + " ms";
                            Log.d("wmqLAG", mainGameState.lag);
                        }
                        else {
                            byte[] bytes = data.asBytes();
                            ClientSnapshot clientSnapshot = mainGameState.getClientSnapshot();
                            clientSnapshot.onServerUpdate(parseServerUpdate(bytes));
                        }
                    }

                    else if (state instanceof LookForServerState)
                    {
                        Log.d("wmq", "Client received data. Current State Look for server" );
                        LookForServerState lookforServerState = (LookForServerState)state;
                        Log.d("wmq", "gameStarted ? : " + lookforServerState.gameStarted.get() );

                        //should not receive lag detection here unless packet disorder
                        String str = new String(data.asBytes());
                        if (str.contains("ServerLagDetection"))
                        {
                            return;
                        }

                        if (!lookforServerState.gameStarted.get()) {
                            lookforServerState.gameStarted.set(true);
                            Log.d("wmq", "set gameStarted: true");
                            // The MainGameState instance must be created by Gdx, because it initializes an OpenGL renderer,
                            // which must be created on the thread that has an OpenGL context.
                            final int id = randomEndpointName;
                            final long receivedNanoTime = Utils.getNanoTime();
                            final byte[] update = data.asBytes();
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("wmq", "before setState. Cur state:" + _app.getCurState().toString());
                                    _app.setState(new MainGameState(_app, id, receivedNanoTime, update));
                                    Log.d("wmq", "after setState. Cur state:" + _app.getCurState().toString());
                                }
                            });
                        }
                    }
                }
                @Override
                public void onTransferUpdate(String string, TransferStateUpdate update) {
                }
            };
}
