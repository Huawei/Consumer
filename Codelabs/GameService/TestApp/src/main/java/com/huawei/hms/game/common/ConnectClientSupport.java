
/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.hms.game.common;
import android.app.Activity;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.game.HuaweiGame;

public class ConnectClientSupport {
    private HuaweiApiClient mApiClient;
    private static final ConnectClientSupport INS = new ConnectClientSupport();

    public static ConnectClientSupport get() {
        return INS;
    }

    public interface IConnectCallBack {
        void onResult(HuaweiApiClient apiClient);
    }

    public void connect(Activity activity, final IConnectCallBack callback) {
        if (activity == null || callback == null) {
            return;
        }

        if (mApiClient != null && mApiClient.isConnected()) {
            callback.onResult(mApiClient);
            return;
        }

        mApiClient = new HuaweiApiClient.Builder(activity).addApi(HuaweiGame.GAME_API)
            .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected() {
                    callback.onResult(mApiClient);
                }

                @Override
                public void onConnectionSuspended(int i) {
                    callback.onResult(null);
                }
            })
            .addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    callback.onResult(null);
                }
            })
            .build();

        mApiClient.connect(activity);
    }

}
