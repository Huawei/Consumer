package com.huawei.applinkingdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.applinking.AGConnectAppLinking;
import com.huawei.agconnect.applinking.AppLinking;

public class MainActivity extends AppCompatActivity {

    private TextView shortTextView;
    private TextView longTextView;
    private static final String DOMAIN_URI_PREFIX = "https://applinkingtest.drcn.agconnect.link";
    private static final String DEEP_LINK = "https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView deeplinktext = findViewById(R.id.deepLink);
        deeplinktext.setText(DEEP_LINK);
        shortTextView = findViewById(R.id.shortLinkText);
        longTextView = findViewById(R.id.longLinkText);

        //creatButton
        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAppLinking();
            }
        });
        //shareButton
        findViewById(R.id.shareLong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareLink((String) longTextView.getText());
            }
        });
        findViewById(R.id.shareShort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareLink((String) shortTextView.getText());
            }
        });
        //init AppLinking
        AGConnectAppLinking.getInstance();
    }
    private void createAppLinking() {
        AppLinking.Builder builder = new AppLinking.Builder().setUriPrefix(DOMAIN_URI_PREFIX)
                .setDeepLink(Uri.parse(DEEP_LINK)).setAndroidLinkInfo(new AppLinking.AndroidLinkInfo.Builder().build());
        longTextView.setText(builder.buildAppLinking().getUri().toString());

        builder.buildShortAppLinking().addOnSuccessListener(shortAppLinking -> {
            shortTextView.setText(shortAppLinking.getShortUrl().toString());
        }).addOnFailureListener(e -> {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void shareLink(String agcLink) {
        if (agcLink != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, agcLink);
            startActivity(intent);
        }
    }
}

