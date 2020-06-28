/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2014-2019. All rights reserved.
 */

package com.huawei.pps.hms.test;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    protected void init() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected int getIntExtra(String name, int defaultValue) {
        Intent intent = getIntent();
        if (intent != null) {
            try {
                return intent.getIntExtra(name, defaultValue);
            } catch (Exception e) {
                Log.e(TAG, "getIntExtra Exception");
            }
        } else {
            Log.e(TAG, "getIntExtra intent is null");
        }
        return defaultValue;
    }
}
