
package com.huawei.locationdemo.logger;

import com.huawei.locationdemo.LogFragment;
import com.huawei.locationdemo.R;

import android.app.Activity;

/**
 * util
 */
public class LoggerActivity extends Activity {

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        initializeLogging();
    }

    private void initializeLogging() {
        LogFragment logFragment = (LogFragment) getFragmentManager().findFragmentById(R.id.framelog);

        LogCatWrapper logcat = new LogCatWrapper();
        logcat.setNext(logFragment.getLogView());

        LocationLog.setLogNode(logcat);
    }
}
