
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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.huawei.hmf.tasks.OnCanceledListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.RankingsClient;
import com.huawei.hms.jos.games.ranking.Ranking;
import com.huawei.hms.jos.games.ranking.RankingScore;
import com.huawei.hms.jos.games.ranking.RankingVariant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RankingDataActivity extends BaseActivity {

    @BindView(R.id.et_ranking_id)
    EditText etRankingId;
    @BindView(R.id.sp_time_dimension)
    Spinner timeSpinner;
    @BindView(R.id.ed_offsetPlayerRank)
    EditText etOffsetPlayerRank;
    @BindView(R.id.ed_maxResults)
    EditText etMaxResults;
    @BindView(R.id.ed_pageDirection)
    EditText etPageDirection;
    @BindView(R.id.sp_isRealTime)
    Spinner spIsRealTimeSpinner;

    private ArrayAdapter<String> adapter;
    private RankingsClient rankingsClient;

    private int getMaxResults() {
        try {
            return Integer.parseInt(etMaxResults.getText().toString());
        } catch (Exception ex) {
            return -1;
        }
    }

    private long getOffsetPlayerRank() {
        try {
            return Long.parseLong(etOffsetPlayerRank.getText().toString());
        } catch (Exception ex) {
            return -1;
        }
    }

    public int getPageDirection() {
        try {
            return Integer.parseInt(etPageDirection.getText().toString());
        } catch (Exception ex) {
            return -1;
        }
    }

    private boolean isRealTime() {
        int position = spIsRealTimeSpinner.getSelectedItemPosition();
        return position == 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_data);
        ButterKnife.bind(this);
        rankingsClient = Games.getRankingsClient(this, getAuthHuaweiId());
        initTimeDimensionSpinner();
        initIsRealTimeSpinner();
    }

    private void initTimeDimensionSpinner() {
        String[] ctype = new String[]{"day", "week", "all", "default", "invalid value"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
    }

    private void initIsRealTimeSpinner() {
        String[] ctype = new String[]{"true", "false",};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIsRealTimeSpinner.setAdapter(adapter);
    }

    @OnClick(R.id.bnt_current_player_ranking_score)
    public void loadCurrentPlayerRankingScore() {
        String rankingId = etRankingId.getText().toString();
        int timeDimension = timeSpinner.getSelectedItemPosition();
        StringBuffer buffer = new StringBuffer();
        buffer.append("getCurrentPlayerRankingScore(").append(getShortRankingId(rankingId));
        buffer.append(",").append(timeDimension).append(")\n");

        Task<RankingScore> task = rankingsClient.getCurrentPlayerRankingScore(rankingId, timeDimension);
        addRankingScoreListener(task, buffer.toString());
    }

    @OnClick(R.id.bnt_loadMoreScores)
    public void loadMoreScores() {
        String rankingId = etRankingId.getText().toString();
        int timeDimension = timeSpinner.getSelectedItemPosition();
        int maxResults = getMaxResults();
        long offsetPlayerRank = getOffsetPlayerRank();
        int pageDirection = getPageDirection();
        StringBuffer buffer = new StringBuffer();
        buffer.append("getMoreRankingScores(").append(getShortRankingId(rankingId));
        buffer.append(",").append(offsetPlayerRank);
        buffer.append(",").append(maxResults);
        buffer.append(",").append(pageDirection);
        buffer.append(",").append(timeDimension).append(")\n");

        Task<RankingsClient.RankingScores> task
                = rankingsClient.getMoreRankingScores(rankingId, offsetPlayerRank, maxResults, pageDirection, timeDimension);
        addClientRankingScoresListener(task, buffer.toString());
    }

    @OnClick(R.id.bnt_loadPlayerCenteredScores_suppor_cache)
    public void loadPlayerCenteredScoresSupporCache() {
        String rankingId = etRankingId.getText().toString();
        int timeDimension = timeSpinner.getSelectedItemPosition();
        int maxResults = getMaxResults();
        boolean bRealTime = isRealTime();

        StringBuffer buffer = new StringBuffer();
        buffer.append("loadPlayerCenteredScores(").append(getShortRankingId(rankingId));
        buffer.append(",").append(timeDimension);
        buffer.append(",").append(maxResults);
        buffer.append(",").append(bRealTime).append(")\n");

        Task<RankingsClient.RankingScores> task
                = rankingsClient.getPlayerCenteredRankingScores(rankingId, timeDimension, maxResults, bRealTime);
        addClientRankingScoresListener(task, buffer.toString());
    }

    @OnClick(R.id.bnt_loadPlayerCenteredScores)
    public void loadPlayerCenteredScores() {
        String rankingId = etRankingId.getText().toString();
        int timeDimension = timeSpinner.getSelectedItemPosition();
        int maxResults = getMaxResults();
        long offsetPlayerRank = getOffsetPlayerRank();
        int pageDirection = getPageDirection();
        StringBuffer buffer = new StringBuffer();
        buffer.append("loadPlayerCenteredScores(").append(getShortRankingId(rankingId));
        buffer.append(",").append(timeDimension);
        buffer.append(",").append(maxResults);
        buffer.append(",").append(offsetPlayerRank);
        buffer.append(",").append(pageDirection).append(")\n");

        Task<RankingsClient.RankingScores> task
                = rankingsClient.getPlayerCenteredRankingScores(rankingId, timeDimension, maxResults, offsetPlayerRank, pageDirection);
        addClientRankingScoresListener(task, buffer.toString());
    }

    @OnClick(R.id.bnt_loadTopScores_support_cache)
    public void loadTopScoresSupportCache() {
        String rankingId = etRankingId.getText().toString();
        int timeDimension = timeSpinner.getSelectedItemPosition();
        int maxResults = getMaxResults();
        boolean bRealTime = isRealTime();
        Task<RankingsClient.RankingScores> task
                = rankingsClient.getRankingTopScores(rankingId, timeDimension, maxResults, bRealTime);
        StringBuffer buffer = new StringBuffer();
        buffer.append("getRankingTopScores(").append(getShortRankingId(rankingId));
        buffer.append(",").append(timeDimension);
        buffer.append(",").append(maxResults);
        buffer.append(",").append(bRealTime).append(")\n");

        addClientRankingScoresListener(task, buffer.toString());

    }

    @OnClick(R.id.btn_loadTopScores)
    public void loadTopScores() {
        String rankingId = etRankingId.getText().toString();
        int timeDimension = timeSpinner.getSelectedItemPosition();
        int maxResults = getMaxResults();
        long offsetPlayerRank = getOffsetPlayerRank();
        int pageDirection = getPageDirection();
        Task<RankingsClient.RankingScores> task
                = rankingsClient.getRankingTopScores(rankingId, timeDimension, maxResults, offsetPlayerRank, pageDirection);
        StringBuffer buffer = new StringBuffer();
        buffer.append("getRankingTopScores(").append(getShortRankingId(rankingId));
        buffer.append(",").append(timeDimension);
        buffer.append(",").append(maxResults);
        buffer.append(",").append(offsetPlayerRank);
        buffer.append(",").append(pageDirection).append(")\n");
        addClientRankingScoresListener(task, buffer.toString());
    }

    private String getShortRankingId(String rankingId) {
        if (rankingId.length() > 4) {
            return "*" + rankingId.substring(rankingId.length() - 4);
        }
        return rankingId;
    }

    // Level.2
    private void addClientRankingScoresListener(final Task<RankingsClient.RankingScores> task, final String method) {

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                showLog(method + " failure. exception: " + e);
            }
        });
        task.addOnSuccessListener(new OnSuccessListener<RankingsClient.RankingScores>() {
            @Override
            public void onSuccess(RankingsClient.RankingScores s) {
                showLog(method + " success. ");
                showClientScoresTaskLog(task);
            }
        });
        task.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                showLog(method + " canceled. ");
            }
        });

    }

    // Level.2
    private void addRankingScoreListener(Task<RankingScore> task, final String method) {

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                showLog(method + " failure. exception: " + e);
            }
        });
        task.addOnSuccessListener(new OnSuccessListener<RankingScore>() {
            @Override
            public void onSuccess(RankingScore s) {
                showLog(method + " success. " + s);
            }
        });
        task.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                showLog(method + " canceled. ");
            }
        });
    }

    // Level.1
    private void showScoreTaskLog(Task<RankingScore> task) {
        if (task.getResult() == null) {
            showLog("RankingScore result is null");
            return;
        }

        showLog("=======RankingScore=======");
        RankingScore s = task.getResult();
        printRankingScoreLog(s, 0);
        printScoreTaskException(task);
    }

    // Level.1
    private void showClientScoresTaskLog(Task<RankingsClient.RankingScores> task) {
        if (task.getResult() == null) {
            showLog("RankingsClient.RankingScores result is null");
            return;
        }

        showLog("=======RankingsClient.RankingScores=======");
        Ranking ranking = task.getResult().getRanking();
        List<RankingScore> scoresBuffer = task.getResult().getRankingScores();

        printRankingLog(ranking);
        if (scoresBuffer == null) {
            showLog("scoresBuffer == null");
            return;
        }
        showLog("scoresBuffer.getCount():" + scoresBuffer.size());
        for (int i = 0; i < scoresBuffer.size(); i++) {
            printRankingScoreLog(scoresBuffer.get(i), i);
        }

        printClientRankingScoresTaskException(task);
    }

    // Level.0
    private void printRankingScoreLog(RankingScore s, int index) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("------RankingScore " + index + "------\n");
        if (s == null) {
            buffer.append("rankingScore is null\n");
            return;
        }

        String displayScore = s.getRankingDisplayScore();
        buffer.append("    DisplayScore: " + displayScore).append("\n");

        buffer.append("    TimeDimension: " + s.getTimeDimension()).append("\n");
        buffer.append("    RawPlayerScore: " + s.getPlayerRawScore()).append("\n");
        buffer.append("    PlayerRank: " + s.getPlayerRank()).append("\n");

        String displayRank = s.getDisplayRank();
        buffer.append("    getDisplayRank: " + displayRank).append("\n");

        buffer.append("    ScoreTag: " + s.getScoreTips()).append("\n");
        buffer.append("    updateTime: " + s.getScoreTimestamp()).append("\n");

        String playerDisplayName = s.getScoreOwnerDisplayName();
        buffer.append("    PlayerDisplayName: " + playerDisplayName).append("\n");
        buffer.append("    PlayerHiResImageUri: " + s.getScoreOwnerHiIconUri()).append("\n");
        buffer.append("    PlayerIconImageUri: " + s.getScoreOwnerIconUri()).append("\n\n");
        showLog(buffer.toString());
    }

    // Level.0
    private void printRankingLog(Ranking ranking) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("-------Ranking-------\n");
        if (ranking == null) {
            buffer.append("ranking == null").append("\n");
        } else {
            buffer.append("DisplayName:" + ranking.getRankingDisplayName()).append("\n");

            buffer.append("RankingId:" + ranking.getRankingId()).append("\n");
            buffer.append("IconImageUri:" + ranking.getRankingImageUri()).append("\n");
            buffer.append("ScoreOrder:" + ranking.getRankingScoreOrder()).append("\n");
            if (ranking.getRankingVariants() != null) {
                buffer.append("Variants.size:" + ranking.getRankingVariants().size()).append("\n");
                printRankingVariantList(ranking.getRankingVariants(), buffer);
            }
        }
        showLog(buffer.toString());
    }

    private void printRankingVariantList(ArrayList<RankingVariant> list, StringBuffer buffer) {
        if (list.size() == 0) {
            return;
        }
        int index = 0;
        for (RankingVariant variant : list) {
            if (variant != null) {
                buffer.append("---RankingVariant ").append(index).append("---\n");
                buffer.append("   getDisplayRanking:").append(variant.getDisplayRanking()).append("\n");
                buffer.append("   getPlayerDisplayScore:").append(variant.getPlayerDisplayScore()).append("\n");
                buffer.append("   getRankTotalScoreNum:").append(variant.getRankTotalScoreNum()).append("\n");
                buffer.append("   getPlayerRank:").append(variant.getPlayerRank()).append("\n");
                buffer.append("   getPlayerScoreTips:").append(variant.getPlayerScoreTips()).append("\n");
                buffer.append("   getPlayerRawScore:").append(variant.getPlayerRawScore()).append("\n");
                buffer.append("   timeDimension:").append(variant.timeDimension()).append("\n");
                buffer.append("   hasPlayerInfo:").append(variant.hasPlayerInfo()).append("\n");
            } else {
                buffer.append("---RankingVariant ").append(index).append(" is null ----\n");
            }
            index++;
        }
    }

    // Level.0
    private void printClientRankingScoresTaskException(Task<RankingsClient.RankingScores> task) {
        if (task.getException() != null) {
            showLog(task.getException().getLocalizedMessage());
            showLog(task.getException().getMessage());
            showLog("task.getException().getCause() " + task.getException().getCause());
            showLog("task.getException().getStackTrace() " + task.getException().getStackTrace().toString());
            showLog("task.getException().getClass()" + task.getException().getClass());
        }
    }

    // Level.0
    private void printScoreTaskException(Task<RankingScore> task) {
        if (task.getException() != null) {
            showLog(task.getException().getLocalizedMessage());
            showLog(task.getException().getMessage());
            showLog("task.getException().getCause() " + task.getException().getCause());
            showLog("task.getException().getStackTrace() " + task.getException().getStackTrace().toString());
            showLog("task.getException().getClass()" + task.getException().getClass());
        }
    }
}
