package com.huawei.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpUtil {
    static private String URL = "https://developer.huawei.com/consumer/cn/";
    static private MediaType MEDIATYPE = MediaType.parse("text/x-markdown; charset=utf-8");
    static private String REQUESTBODY = "apms http request test";

    static public void oneRequest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .post(RequestBody.create(MEDIATYPE, REQUESTBODY))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("apmsAndroidDemo", "onFailure: " + e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("apmsAndroidDemo", "onResponse: Success" );
            }
        });
    }

}
