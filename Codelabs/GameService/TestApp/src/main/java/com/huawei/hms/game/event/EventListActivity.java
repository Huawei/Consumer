
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

package com.huawei.hms.game.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.jos.games.EventsClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.event.Event;
import com.huawei.hms.game.achievement.AchievementDetailActivity;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventListActivity extends Activity implements EventListAdapter.OnBtnClickListener {

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    public TextView tvTitle;

    private ArrayList<Event> events = new ArrayList<>();
    private EventsClient client;
    private EventListActivity mContext;
    AuthHuaweiId AuthHuaweiId =null;
    private boolean forceReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_list);
        mContext = this;
        ButterKnife.bind(mContext);
        tvTitle.setText("events list");
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        forceReload = intent.getBooleanExtra("forceReload", false);
        String mSignString =intent.getStringExtra("mSign");
        String idsString =intent.getStringExtra("idsString");

        try {
            AuthHuaweiId =AuthHuaweiId.fromJson(mSignString);
        } catch (JSONException e) {
        }
        client = Games.getEventsClient(this, AuthHuaweiId);
        Task<List<Event>> task =null;
        if (TextUtils.isEmpty(idsString)) {
            task = client.getEventList(forceReload);
        }else {
            String[] eventIds = idsString.split(",");
            task = client.getEventListByIds(forceReload, eventIds);
        }
        addResultListener(task);
    }

    private void addResultListener(Task<List<Event>> task) {
        if (task == null){ return;}
        task.addOnSuccessListener(new OnSuccessListener<List<Event>>() {
            @Override
            public void onSuccess(List<Event> data) {
                if (data == null) {
                    showLog("eventBuffer is null");
                    return;
                }

                Iterator<Event> iterator = data.iterator();
                events.clear();
                while (iterator.hasNext()) {
                    Event event = iterator.next();
                    events.add(event);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                recyclerView.setLayoutManager(layoutManager);
                EventListAdapter adapter = new EventListAdapter(mContext, events, mContext);
                recyclerView.setAdapter(adapter);

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

    @OnClick(R.id.iv_back)
    public void backHome() {
        finish();
    }

    private void showLog(String result) {
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int postion) {
        Intent intent = new Intent(this, AchievementDetailActivity.class);
        Event event = events.get(postion);
        intent.putExtra("achievementName",event.getName());
        intent.putExtra("achievementDes",event.getDescription());
        intent.putExtra("unlockedImageUri",event.getThumbnailUri());
        startActivity(intent);

    }

    @Override
    public void reportEvent(String eventId) {
        client.grow(eventId, 1);
    }
}
