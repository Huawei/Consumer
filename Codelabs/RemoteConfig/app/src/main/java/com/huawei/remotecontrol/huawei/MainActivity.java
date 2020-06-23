package com.huawei.remotecontrol.huawei;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.agconnect.remoteconfig.AGConnectConfig;
import com.huawei.agconnect.remoteconfig.ConfigValues;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity Demo";

    private static final String GREETING_KEY = "GREETING_KEY";
    private static final String SET_BOLD_KEY = "SET_BOLD_KEY";
    private static final String IMAGE_URL_KEY = "IMAGE_URL_KEY";

    private AGConnectConfig config;

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.greeting);
        imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.fetch_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchAndApply();
            }
        });
        config = AGConnectConfig.getInstance();
        // Set the local default configuration to the current application configuration.
        config.applyDefault(R.xml.default_config);
        textView.setText(config.getString(GREETING_KEY));
        imageView.setImageDrawable(getDrawable(R.drawable.huawei_logo_1));
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    /**
     * Obtain the network configuration and set it to the current application configuration.
     */
    private void fetchAndApply() {
        config.fetch(0).addOnSuccessListener(new OnSuccessListener<ConfigValues>() {
            @Override
            public void onSuccess(ConfigValues configValues) {
                // Apply the network configuration to the current configuration.
                config.apply(configValues);
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                textView.setText("fetch setting failed: " + e.getMessage());
            }
        });
    }


    private void updateUI() {
        String text = config.getString(GREETING_KEY);
        Boolean isBold = config.getBoolean(SET_BOLD_KEY);
        textView.setText(text);
        String imageUrl = config.getString(IMAGE_URL_KEY);
        Log.i(TAG, "image url: " + imageUrl);
        Bitmap bitmap = getBitmap(imageUrl);
        imageView.setImageBitmap(bitmap);
        if (isBold){
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    public Bitmap getBitmap(String imageURL) {
        try {
            URL url = new URL(imageURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "bitmap error, " + e.getMessage());
        }
        return null;
    }
}
