
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
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.RankingsClient;
import com.huawei.hms.jos.games.ranking.ScoreSubmissionInfo;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RankingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_get_ranking_intent)
    public void onClickGetRankingIntent() {
        startActivity(new Intent(this, RankingIntentActivity.class));
    }

    @OnClick(R.id.btn_get_ranking_data)
    public void getRankingData() {
        startActivity(new Intent(this, RankingDataActivity.class));
    }

    @OnClick(R.id.btn_get_ranking_metadata)
    public void getRankingMetaData() {
        startActivity(new Intent(this, RankingMetaActivity.class));
    }

    @OnClick(R.id.btn_get_ranking_switch)
    public void onClickGetStatus() {
        RankingsClient rankingsClient = Games.getRankingsClient(this, getAuthHuaweiId());
        Task<Integer> task = rankingsClient.getRankingSwitchStatus();
        task.addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                show("getRankingSwitchStatus success gameActivities:" + integer);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                }
            }
        });
    }

    @OnClick(R.id.btn_set_ranking_switch)
    public void onClickSetStatus() {
        EditText editText = findViewById(R.id.ranking_status_input);
        RankingsClient rankingsClient = Games.getRankingsClient(this, getAuthHuaweiId());
        String numText = editText.getText().toString();
        if (TextUtils.isEmpty(numText)) {
            Toast.makeText(this, "Demo Input empty", Toast.LENGTH_SHORT).show();
            return;
        }
        int stateValue = 0;
        try {
            stateValue = Integer.parseInt(numText);
        } catch (Exception e) {
            Toast.makeText(this, "Demo Input error", Toast.LENGTH_SHORT).show();
            return;
        }

        Task<Integer> task = rankingsClient.setRankingSwitchStatus(stateValue);
        task.addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                show("setRankingSwitchStatus.onSuccess:" + integer);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                }
            }
        });
    }

    @OnClick(R.id.btn_submitScore)
    public void onClickSubmitScore() {
        String rankingId = getRankingId();
        int score = getScores();
        RankingsClient rankingsClient = Games.getRankingsClient(this, getAuthHuaweiId());
        StringBuffer buffer = new StringBuffer();
        buffer.append("Demo call submitScore(").append(getShortRankingId(rankingId)).append(",").append(score).append(")");
        showLog(buffer.toString());
        rankingsClient.submitRankingScore(rankingId, score);
    }

    private String getShortRankingId(String rankingId) {
        if (rankingId.length() > 4) {
            return "*" + rankingId.substring(rankingId.length() - 4);
        }
        return rankingId;
    }

    @OnClick(R.id.btn_submitScoreTag)
    public void onClickSubmitScoreTag() {
        String rankingId = getRankingId();
        int score = getScores();
        String scoreTag = getScoreTag();
        RankingsClient rankingsClient = Games.getRankingsClient(this, getAuthHuaweiId());
        StringBuffer buffer = new StringBuffer();
        buffer.append("Demo call submitScore(").append(getShortRankingId(rankingId)).append(",").append(score).append(",").append(scoreTag).append(")");
        showLog(buffer.toString());
        rankingsClient.submitRankingScore(rankingId, score, scoreTag);
    }

    @OnClick(R.id.btn_submitScoreImmediate)
    public void onClickSubmitScoreImmediate() {
        String rankingId = getRankingId();
        int score = getScores();
        RankingsClient rankingsClient = Games.getRankingsClient(this, getAuthHuaweiId());
        StringBuffer buffer = new StringBuffer();
        buffer.append("Demo call submitScoreImmediate(").append(getShortRankingId(rankingId)).append(",").append(score).append(")");
        showLog(buffer.toString());

        Task<ScoreSubmissionInfo> submissionDataTask = rankingsClient.submitScoreWithResult(rankingId, score);
        submissionDataTask.addOnSuccessListener(new OnSuccessListener<ScoreSubmissionInfo>() {
            @Override
            public void onSuccess(ScoreSubmissionInfo scoreSubmissionData) {
                showScoreSubmissionData(scoreSubmissionData);
            }
        });
        submissionDataTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                }
            }
        });
    }

    @OnClick(R.id.btn_submitScoreImmediateTag)
    public void onClickSubmitScoreImmediateTag() {
        String rankingId = getRankingId();
        int score = getScores();
        String scoreTag = getScoreTag();
        RankingsClient rankingsClient = Games.getRankingsClient(this, getAuthHuaweiId());
        StringBuffer buffer = new StringBuffer();
        buffer.append("Demo call submitScoreImmediate(").append(getShortRankingId(rankingId)).append(",").append(score).append(",").append(scoreTag).append(")");
        showLog(buffer.toString());

        Task<ScoreSubmissionInfo> submissionDataTask = rankingsClient.submitScoreWithResult(rankingId, score, scoreTag);
        submissionDataTask.addOnSuccessListener(new OnSuccessListener<ScoreSubmissionInfo>() {
            @Override
            public void onSuccess(ScoreSubmissionInfo scoreSubmissionData) {
                showScoreSubmissionData(scoreSubmissionData);
            }
        });
        submissionDataTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                }
            }
        });
    }

    private void showScoreSubmissionData(ScoreSubmissionInfo scoreSubmissionData) {
        StringBuffer buf = new StringBuffer();
        buf.append("getPlayerId():").append(scoreSubmissionData.getPlayerId()).append("\n");
        buf.append("getRankingId():").append(scoreSubmissionData.getRankingId()).append("\n");
        ScoreSubmissionInfo.Result result;
        for (int i = 0; i < 3; i++) {
            result = scoreSubmissionData.getSubmissionScoreResult(i);
            if (result != null) {
                buf.append("ScoreSubmissionInfo.Result->").append(i).append("\n");
                buf.append("displayScore:").append(result.displayScore).append(",isBest:").append(result.isBest);
                buf.append(",playerRawScore:").append(result.playerRawScore).append(",scoreTips:").append(result.scoreTips).append("\n");
            } else {
                buf.append("ScoreSubmissionInfo.Result->").append(i).append(" is null");
            }
        }
        showLog(buf.toString());
    }

    private int getScores() {
        EditText editText = findViewById(R.id.ranking_score_input);
        String scoreText = editText.getText().toString();
        int score = 0;
        try {
            score = Integer.parseInt(scoreText);
        } catch (Exception e) {

        }
        return score;
    }

    private String getRankingId() {
        EditText editText = findViewById(R.id.ranking_id_input);
        return editText.getText().toString();
    }

    private String getScoreTag() {
        EditText editText = findViewById(R.id.ranking_score_tag_input);
        return editText.getText().toString();
    }
}
