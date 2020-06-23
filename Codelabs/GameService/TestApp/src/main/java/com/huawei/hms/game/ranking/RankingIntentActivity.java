
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

package com.huawei.hms.game.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.RankingsClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RankingIntentActivity extends BaseActivity {

    @BindView(R.id.et_ranking_id)
    EditText etRankingId;
    @BindView(R.id.sp_time_dimension)
    Spinner timeSpinner;

    private ArrayAdapter<String> adapter;
    private int choosedTimeDimension = 0;
    private RankingsClient rankingsClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_intent);
        ButterKnife.bind(this);
        initTimeDimensionSpinner();
        rankingsClient = Games.getRankingsClient(this, getAuthHuaweiId());
    }

    private void initTimeDimensionSpinner() {
        String[] ctype = new String[]{"day", "week", "all", "default", "invalid value"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeSpinner.setAdapter(adapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosedTimeDimension = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.btn_get_all_ranking)
    public void onClickGetAllIntent() {
        Task<Intent> allIntentTask = rankingsClient.getTotalRankingsIntent();
        allIntentTask.addOnSuccessListener(new OnSuccessListener<Intent>() {
            @Override
            public void onSuccess(Intent intent) {
                try {
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    showLog("startActivityForResult Exception");
                }
            }
        });
        allIntentTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                }
            }
        });
    }

    @OnClick(R.id.btn_get_ranking)
    public void onClickGetIntent() {
        String rankingId = etRankingId.getText().toString();
        Task<Intent> allIntentTask = rankingsClient.getRankingIntent(rankingId, choosedTimeDimension);
        allIntentTask.addOnSuccessListener(new OnSuccessListener<Intent>() {
            @Override
            public void onSuccess(Intent intent) {
                try {
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    showLog("startActivityForResult Exception");
                }
            }
        });
        allIntentTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                }
            }
        });
    }

    @OnClick(R.id.btn_get_ranking_2)
    public void onClickGetIntent2() {
        String rankingId = etRankingId.getText().toString();
        Task<Intent> allIntentTask = rankingsClient.getRankingIntent(rankingId);
        allIntentTask.addOnSuccessListener(new OnSuccessListener<Intent>() {
            @Override
            public void onSuccess(Intent intent) {
                try {
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    showLog("startActivityForResult Exception");
                }
            }
        });
        allIntentTask.addOnFailureListener(new OnFailureListener() {
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
