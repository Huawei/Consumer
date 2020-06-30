
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnCanceledListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.RankingsClient;
import com.huawei.hms.jos.games.ranking.Ranking;
import com.huawei.hms.jos.games.ranking.RankingVariant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RankingMetaActivity extends BaseActivity {
    private RankingsClient rankingsClient;

    @BindView(R.id.et_ranking_id)
    EditText etRankingId;
    @BindView(R.id.sp_time_dimension)
    Spinner spIsRealTimeSpinner;

    @BindView(R.id.ranking_meta_display)
    TextView metaData;

    boolean isRealTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_meta);
        ButterKnife.bind(this);
        rankingsClient = Games.getRankingsClient(this, getAuthHuaweiId());
        initIsRealTimeSpinner();
    }

    @OnClick(R.id.btn_get_ranking_meta)
    public void onClickLoadRankingMetadata() {
        String rankingId = etRankingId.getText().toString();
        Task<Ranking> task = rankingsClient.getRankingSummary(rankingId, isRealTime);
        StringBuffer buffer = new StringBuffer("Demo call getRankingSummary(").append(getShortRankingId(rankingId));
        buffer.append(",").append(isRealTime).append(")");
        addRankingListener(task, buffer.toString());
    }

    @OnClick(R.id.btn_get_ranking_meta2)
    public void onClickLoadRankingMetadata2() {
        Task<List<Ranking>> task = rankingsClient.getRankingSummary(isRealTime);
        StringBuffer buffer = new StringBuffer();
        buffer.append("Demo call getRankingSummary(").append(isRealTime).append(")");
        addRankingBufferListener(task, buffer.toString());
    }

    @OnClick(R.id.meta_clear_bt)
    public void onClickClearLog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                metaData.setText("");
            }
        });
    }

    private String getShortRankingId(String rankingId) {
        if (rankingId.length() > 4) {
            return "*" + rankingId.substring(rankingId.length() - 4);
        }
        return rankingId;
    }

    private void initIsRealTimeSpinner() {
        String[] ctype = new String[]{"true", "false",};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //Sets the drop-down list box style.

        spIsRealTimeSpinner.setAdapter(adapter);
        spIsRealTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    isRealTime = true;
                } else {
                    isRealTime = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addRankingListener(final Task<Ranking> task, final String method) {
        // Clearing Logs
        onClickClearLog();

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                showMetaInfo(method + " failure. exception: " + e);
            }
        });
        task.addOnSuccessListener(new OnSuccessListener<Ranking>() {
            @Override
            public void onSuccess(Ranking s) {
                showMetaInfo(method + " success. ");
                showRankingTaskLog(task);
            }
        });
        task.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                showMetaInfo(method + " canceled. ");
            }
        });
    }

    private void showRankingBufferTaskLog(Task<List<Ranking>> task) {
        if (task.getResult() == null) {
            showMetaInfo("List<Ranking> result is null");
            return;
        }

        showMetaInfo("=======List<Ranking>=======");
        List<Ranking> result = task.getResult();
        if (result == null) {
            showMetaInfo("List<Ranking> is null");
            return;
        }

        for (int i = 0; i < result.size(); i++) {
            Ranking ranking = result.get(i);
            printRankingLog(ranking);
        }
        printRankingBufferTaskException(task);
    }

    private void printRankingLog(Ranking ranking) {
        showMetaInfo("-------Ranking-------");
        if (ranking == null) {
            showMetaInfo("ranking == null");
        } else {
            showMetaInfo(" DisplayName:" + ranking.getRankingDisplayName());
//            CharArrayBuffer caBuf = new CharArrayBuffer(ranking.getDisplayName().length());
//            ranking.getDisplayName(caBuf);
//            showMetaInfo(" getDisplayName:" +caBuf.toString());
            showMetaInfo(" RankingId:" + ranking.getRankingId());
            showMetaInfo(" IconImageUri:" + ranking.getRankingImageUri());
            showMetaInfo(" ScoreOrder:" + ranking.getRankingScoreOrder());

            if (ranking.getRankingVariants() != null) {
                showMetaInfo(" Variants.size:" + ranking.getRankingVariants().size());
                if (ranking.getRankingVariants().size() > 0) {
                    showRankingVariant(ranking.getRankingVariants());
                }
            }
        }
    }

    private void showRankingVariant(ArrayList<RankingVariant> list) {
        StringBuffer buffer;
        int index = 0;
        for (RankingVariant variant : list) {
            buffer = new StringBuffer();
            buffer.append("  RankingVariant size->").append(index).append("\n");
            buffer.append("   getPlayerScoreTips():").append(variant.getPlayerScoreTips()).append("\n");
            buffer.append("   getDisplayRanking():").append(variant.getDisplayRanking()).append("\n");
            buffer.append("   getPlayerDisplayScore():").append(variant.getPlayerDisplayScore()).append("\n");
            buffer.append("   getRankTotalScoreNum():").append(variant.getRankTotalScoreNum()).append("\n");
            buffer.append("   getPlayerRank():").append(variant.getPlayerRank()).append("\n");
            buffer.append("   getPlayerRawScore():").append(variant.getPlayerRawScore()).append("\n");
            buffer.append("   timeDimension():").append(variant.timeDimension()).append("\n");
            buffer.append("   hasPlayerInfo():").append(variant.hasPlayerInfo()).append("\n").append("- - - ");
            showMetaInfo(buffer.toString());
            index++;
        }
    }

    // Level.2
    private void addRankingBufferListener(final Task<List<Ranking>> task, final String method) {
        // Clearing Logs
        onClickClearLog();

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                showMetaInfo(method + " failure. exception: " + e);
            }
        });
        task.addOnSuccessListener(new OnSuccessListener<List<Ranking>>() {
            @Override
            public void onSuccess(List<Ranking> s) {
                showMetaInfo(method + " success. " );
                showRankingBufferTaskLog(task);
            }
        });
        task.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                showMetaInfo(method + " canceled. ");
            }
        });
    }

    // Level.0
    private void printRankingBufferTaskException(Task<List<Ranking>> task) {
        if (task.getException() != null) {
            showMetaInfo(task.getException().getLocalizedMessage());
            showMetaInfo(task.getException().getMessage());
            showMetaInfo("task.getException().getCause() " + task.getException().getCause());
            showMetaInfo("task.getException().getStackTrace() " + task.getException().getStackTrace().toString());
            showMetaInfo("task.getException().getClass()" + task.getException().getClass());
        }
    }

    // Level.1
    private void showRankingTaskLog(Task<Ranking> task) {
        if (task.getResult() == null) {
            showMetaInfo("Ranking result is null");
            return;
        }

        Ranking ranking = task.getResult();
        printRankingLog(ranking);
        printRankingTaskException(task);
    }

    private void printRankingTaskException(Task<Ranking> task) {
        if (task.getException() != null) {
            showLog(task.getException().getLocalizedMessage());
            showLog(task.getException().getMessage());
            showLog("task.getException().getCause() " + task.getException().getCause());
            showLog("task.getException().getStackTrace() " + task.getException().getStackTrace().toString());
            showLog("task.getException().getClass()" + task.getException().getClass());
        }
    }

    private void showMetaInfo(final String info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuffer buffer = new StringBuffer();
                buffer.append(metaData.getText().toString()).append("\n");
                buffer.append(info);
                metaData.setText(buffer.toString());
            }
        });
    }
}
