
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

package com.huawei.hms.game.playerstats;

import android.os.Bundle;
import android.widget.CheckBox;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.jos.games.playerstats.GamePlayerStatistics;
import com.huawei.hms.jos.games.playerstats.GamePlayerStatisticsClient;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerStatsActivity extends BaseActivity {
    private static boolean ISREALTIME = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get_player_stats)
    public void getCurrentPlayerStats() {
        initIsRealTime();
        GamePlayerStatisticsClient playerStatsClient = Games.getGamePlayerStatsClient(this, getAuthHuaweiId());
        Task<GamePlayerStatistics> task = playerStatsClient.getGamePlayerStatistics(ISREALTIME);
        task.addOnSuccessListener(new OnSuccessListener<GamePlayerStatistics>() {
            @Override
            public void onSuccess(GamePlayerStatistics gamePlayerStatistics) {
                if (gamePlayerStatistics == null) {
                    showLog("playerStatsAnnotatedData is null, inner error");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("IsRealTime:" + ISREALTIME);
                sb.append("\n---AverageSessionLength: " + gamePlayerStatistics.getAverageOnLineMinutes() + "\n---");
                sb.append("DaysSinceLastPlayed: " + gamePlayerStatistics.getDaysFromLastGame() + "\n---");
                sb.append("NumberOfPurchases: " + gamePlayerStatistics.getPaymentTimes() + "\n---");
                sb.append("NumberOfSessions: " + gamePlayerStatistics.getOnlineTimes() + "\n---");
                sb.append("TotalPurchasesAmountRange: " + gamePlayerStatistics.getTotalPurchasesAmountRange());
                showLog(sb.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                }
            }
        });
    }

    private void initIsRealTime() {
        CheckBox checkBox = findViewById(R.id.is_real_time_checkbox);
        ISREALTIME = checkBox.isChecked();
    }

    @OnClick(R.id.btn_get_player_id)
    public void getPlayerId() {
        PlayersClient client = Games.getPlayersClient(this, getAuthHuaweiId());
        Task<String> task = client.getCachePlayerId();
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String playerId) {
                showLog(playerId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                }
            }
        });
    }
}