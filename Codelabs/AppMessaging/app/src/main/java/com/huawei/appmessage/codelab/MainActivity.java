package com.huawei.appmessage.codelab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.agconnect.appmessaging.AGConnectAppMessaging;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingCallback;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingOnClickListener;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingOnDismissListener;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingOnDisplayListener;
import com.huawei.agconnect.appmessaging.model.AppMessage;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.aaid.entity.AAIDResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AppMessaging";
    private AGConnectAppMessaging appMessaging;
    private Button addView;
    private Button rmView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addView = findViewById(R.id.add_custum_view);
        rmView = findViewById(R.id.remove_custum_view);
        textView = findViewById(R.id.textview);

        //get your device's aaid for testing asynchronously
        HmsInstanceId inst  = HmsInstanceId.getInstance(this);
        Task<AAIDResult> idResult =  inst.getAAID();
        idResult.addOnSuccessListener(new OnSuccessListener<AAIDResult>() {
            @Override
            public void onSuccess(AAIDResult aaidResult) {
                String aaid = aaidResult.getId();
                textView.setText(aaid);
                Log.d(TAG, "getAAID success:" + aaid );

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "getAAID failure:" + e);
            }
        });

        appMessaging = AGConnectAppMessaging.getInstance();
        // setForceFetch for testing
        AGConnectAppMessaging.getInstance().setForceFetch();
        appMessaging.addOnDisplayListener(new AGConnectAppMessagingOnDisplayListener() {
            @Override
            public void onMessageDisplay(AppMessage appMessage) {
                Toast.makeText(MainActivity.this, "Message showed", Toast.LENGTH_LONG).show();
            }
        });
        appMessaging.addOnClickListener(new AGConnectAppMessagingOnClickListener() {
            @Override
            public void onMessageClick(AppMessage appMessage) {
                Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_LONG).show();
            }
        });
        appMessaging.addOnDismissListener(new AGConnectAppMessagingOnDismissListener() {
            @Override
            public void onMessageDismiss(AppMessage appMessage, AGConnectAppMessagingCallback.DismissType dismissType) {
                Toast.makeText(MainActivity.this, "Message Dismiss, dismiss type: " + dismissType, Toast.LENGTH_LONG).show();
            }
        });

        // apply custom view
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomView customView = new CustomView(MainActivity.this);
                appMessaging.addCustomView(customView);
            }
        });

        // restore initial view
        rmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appMessaging.removeCustomView();
            }
        });
    }
}
