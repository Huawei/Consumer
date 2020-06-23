package com.huawei.shareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        textView.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));

        findViewById(R.id.button1).setOnClickListener(view -> {
            String text = textView.getText().toString();
            if (text.length() > 0) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(text));
                startActivity(intent);
            }
        });

        EditText editText = findViewById(R.id.editText);
        findViewById(R.id.button2).setOnClickListener(view -> {
            String text = editText.getText().toString();
            if (text.length() > 0) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(text));
                startActivity(intent);
            }
        });
    }
}
