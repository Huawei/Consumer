package com.huawei.agconnect.apms.androiddemo;

import android.os.Bundle;

import com.huawei.agconnect.apms.APMS;
import com.huawei.util.HttpUtil;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("apmsAndroidDemo", "apms demo start.");
        Button sendNetworkRequestBtn = findViewById(R.id.btn_network);
        sendNetworkRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("apmsAndroidDemo", "send network request.");
                HttpUtil.oneRequest();
            }
        });

        findViewById(R.id.enable_apms_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "disable apms.");
                APMS.getInstance().enableCollection(false);
            }
        });

        findViewById(R.id.enable_apms_on).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "enable apms.");
                APMS.getInstance().enableCollection(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
