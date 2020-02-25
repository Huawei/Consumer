package com.example.scankitdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;


public class DefinedActivity extends Activity {

    private static final String TAG = "DefinedActivity";

    //declare RemoteView instance
    private RemoteView remoteView;
    //declare the key ,used to get the value returned from scankit
    public static final String SCAN_RESULT = "scanResult";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_defined);

        //set scanning area
        Rect rect = new Rect();
        rect.top = 100;
        rect.bottom = 2000;

        //initialize RemoteView instance, and set calling back for scanning result
        remoteView = new RemoteView.Builder().setContext(this).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build();
        remoteView.onCreate(savedInstanceState);
        remoteView.setOnResultCallback(new OnResultCallback() {
            @Override
            public void onResult(HmsScan[] result) {
                //judge the result is effective
                if (result != null && result.length > 0 && result[0] != null && !TextUtils.isEmpty(result[0].getOriginalValue())) {
                    Intent intent = new Intent();
                    intent.putExtra(SCAN_RESULT, result[0]);
                    setResult(RESULT_OK, intent);
                    DefinedActivity.this.finish();
                }
            }
        });

        //add remoteView to framelayout
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        FrameLayout frameLayout = findViewById(R.id.rim);
        frameLayout.addView(remoteView, params);

        //set back button listener
        ImageView backBtn = findViewById(R.id.back_img);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefinedActivity.this.finish();
            }
        });

    }

    //manage remoteView lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        remoteView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        remoteView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        remoteView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteView.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        remoteView.onStop();
    }

}
