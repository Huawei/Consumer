package com.huawei.appmessage.codelab;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huawei.agconnect.appmessaging.AGConnectAppMessagingCallback;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingDisplay;
import com.huawei.agconnect.appmessaging.model.AppMessage;

public class CustomView implements AGConnectAppMessagingDisplay {
    private static final String TAG = "CustomView";
    MainActivity activity;

    public CustomView(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void displayMessage(@NonNull AppMessage appMessage, @NonNull AGConnectAppMessagingCallback callback) {
        Log.d(TAG, appMessage.getId() + "");
        showDialog(appMessage, callback);
    }

    private void showDialog(@NonNull final AppMessage appMessage, @NonNull final AGConnectAppMessagingCallback callback) {
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_view, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(activity).setView(view).create();
        Button click = view.findViewById(R.id.click);
        Button dismiss = view.findViewById(R.id.dismiss);
        TextView id = view.findViewById(R.id.id);
        id.setText("MessageID: " + appMessage.getId());
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set button callback
                callback.onMessageClick(appMessage);
                callback.onMessageDismiss(appMessage, AGConnectAppMessagingCallback.DismissType.CLICK);
                dialog.dismiss();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set button callback
                callback.onMessageDismiss(appMessage, AGConnectAppMessagingCallback.DismissType.CLICK);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((getScreenWidth(activity) / 4 * 3), LinearLayout.LayoutParams.WRAP_CONTENT);
        callback.onMessageDisplay(appMessage);
    }


    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
