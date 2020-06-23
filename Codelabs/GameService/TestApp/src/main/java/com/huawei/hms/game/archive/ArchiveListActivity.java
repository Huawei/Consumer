
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

package com.huawei.hms.game.archive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.game.common.SignInCenter;
import com.huawei.hms.jos.games.ArchivesClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.GamesStatusCodes;
import com.huawei.hms.jos.games.archive.ArchiveSummary;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArchiveListActivity extends BaseActivity implements ArchiveListAdapter.OnBtnClickListener {

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    private ArrayList<ArchiveSummary> archiveSummaries = new ArrayList<>();
    private ArchivesClient client;
    private ArchiveListAdapter adapter;

    private synchronized ArchivesClient getClient() {
        if (client == null) {
            client = Games.getArchiveClient(this, SignInCenter.get().getAuthHuaweiId());
        }
        return client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_list);
        ButterKnife.bind(this);
        requestData();

        recyclerView.requestLayout();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(1);
        adapter = new ArchiveListAdapter(this, archiveSummaries, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private synchronized void requestData() {
        boolean isRealTime = getIntent().getBooleanExtra("isRealTime", false);
        Task<List<ArchiveSummary>> task = getClient().getArchiveSummaryList(isRealTime);
        task.addOnSuccessListener(new OnSuccessListener<List<ArchiveSummary>>() {
            @Override
            public void onSuccess(List<ArchiveSummary> buffer) {
                archiveSummaries.clear();
                if (buffer == null){
                    showLog("archives is null");
                    adapter.notifyDataSetChanged();
                    return;
                }

                for (ArchiveSummary archiveSummary : buffer) {
                    archiveSummaries.add(archiveSummary);
                }

                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    showLog(result);
                    if (((ApiException) e).getStatusCode() == GamesStatusCodes.GAME_STATE_ARCHIVE_NO_DRIVE) {
                        guideToAgreeDriveProtocol();
                    }
                }
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void backHome() {
        finish();
    }

    private long lastClickTime;

    @OnClick(R.id.iv_refresh)
    public synchronized void refresh() {
        if (lastClickTime == 0) {
            lastClickTime = System.currentTimeMillis();
            requestData();
        } else {
            if (System.currentTimeMillis() - lastClickTime > 1000) {
                requestData();
                lastClickTime = System.currentTimeMillis();
            }
        }
    }


    @Override
    public synchronized  void onItemClick(int position) {
        Intent intent = new Intent(this, ArchiveMetadataDetailActivity.class);
        if (archiveSummaries.size() > 0) {
            ArchiveSummary archiveSummary = archiveSummaries.get(position);
            Bundle data = new Bundle();
            data.putParcelable("archiveSummary", archiveSummary);
            intent.putExtras(data);
            startActivityForResult(intent, 8000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
