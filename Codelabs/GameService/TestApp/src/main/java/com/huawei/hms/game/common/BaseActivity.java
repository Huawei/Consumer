
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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.R;
import com.huawei.hms.game.MainActivity;
import com.huawei.hms.game.achievement.AchievementActivity;
import com.huawei.hms.game.archive.ArchiveActivity;
import com.huawei.hms.game.event.EventActivity;
import com.huawei.hms.game.gamesummary.GameSummaryActivity;
import com.huawei.hms.game.playerstats.PlayerStatsActivity;
import com.huawei.hms.game.ranking.RankingActivity;
import com.huawei.hms.jos.games.archive.ArchiveSummary;
import com.huawei.hms.jos.games.gamesummary.GameSummary;
import com.huawei.hms.jos.games.player.Player;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.OnClick;


public abstract class BaseActivity extends Activity {
    StringBuffer sbLog = new StringBuffer();

    public HuaweiIdAuthParams getHuaweiIdParams() {
        return new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME).createParams();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @OnClick(R.id.btn_main)
    public void clickMainMenu() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

    @OnClick(R.id.btn_achievement)
    public void clickAchievementMenu() {
        startActivity(new Intent(this, AchievementActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

    @OnClick(R.id.btn_ranking)
    public void clickRankingMenu() {
        startActivity(new Intent(this, RankingActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    @OnClick(R.id.btn_game_save)
    public void clickGameSave() {
        startActivity(new Intent(this, ArchiveActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

    @OnClick(R.id.btn_event)
    public void clickEventMenu() {
        startActivity(new Intent(this, EventActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

    @OnClick(R.id.btn_GameSummary)
    public void clickGameSummaryMenu() {
        startActivity(new Intent(this, GameSummaryActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

    @OnClick(R.id.btn_PlayerStat)
    public void clickPlayerStatsMenu() {
        startActivity(new Intent(this, PlayerStatsActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    public void showLog(String logLine) {
        show(logLine);
    }

    protected void show(String logLine) {
        DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.ENGLISH);
        String time = format.format(new Date());

        sbLog.append(time).append(":").append(logLine);
        sbLog.append('\n');
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final TextView vText = (TextView) findViewById(R.id.tv_log);
                vText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vText.setText("");
                        sbLog = new StringBuffer();
                    }
                });
                vText.setText(sbLog.toString());
                View vScrool = findViewById(R.id.sv_log);
                if (vScrool instanceof ScrollView) {
                    ScrollView svLog = (ScrollView) vScrool;
                    svLog.fullScroll(View.FOCUS_DOWN);
                }
            }
        });
    }

    protected AuthHuaweiId getAuthHuaweiId() {
        return SignInCenter.get().getAuthHuaweiId();
    }

    public void guideToAgreeDriveProtocol() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle("Turn On Drive First");
        builder.setMessage("Use game save, you need to turn On Drive First");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("https://lfcloudtestbedportal.hwcloudtest.cn:18447/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BaseActivity.this, "reject drive protocol", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public void showPlayerAndGame(ArchiveSummary archiveSummary) {
        Player player = archiveSummary.getGamePlayer();
        if (player != null) {
            showLog("Player");
            showLog("PlayerId:" + player.getPlayerId());
            showLog("displayName:" + player.getDisplayName());
            showLog("playerLevel:" + player.getLevel());
            showLog("iconImageUri:" + player.getIconImageUri());
            showLog("hiResImageUri:" + player.getHiResImageUri());
            showLog("signTs:" + player.getSignTs());
        } else {
            showLog("Player:null");
        }

        GameSummary gameSummary = archiveSummary.getGameSummary();
        if (gameSummary != null) {
            showLog("gameSummary");
            showLog("achievementTotalCount:" + gameSummary.getAchievementCount());
            showLog("rankingCount:" + gameSummary.getRankingCount());
            showLog("appId:" + gameSummary.getAppId());
            showLog("game displayName:" + gameSummary.getGameName());
            showLog("primaryCategory:" + gameSummary.getFirstKind());
            showLog("secondaryCategory:" + gameSummary.getSecondKind());
            showLog("Description:" + gameSummary.getDescInfo());
            showLog("iconImageUri:" + gameSummary.getGameIconUri());
            showLog("hiResImageUri:" + gameSummary.getGameHdImgUri());
        } else {
            showLog("gameSummary:null");
        }
    }
}
