/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.huawei.cloudfunction.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.agconnect.function.AGCFunctionException;
import com.huawei.agconnect.function.AGConnectFunction;
import com.huawei.agconnect.function.FunctionResult;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button resultBtn;
    private EditText inputYear;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultBtn = findViewById(R.id.rst_btn);
        resultText = findViewById(R.id.rst_text);
        inputYear = findViewById(R.id.year_text);

        resultBtn.setOnClickListener(new ButtonClickListener());
    }




    private class ButtonClickListener implements View.OnClickListener {

        private static final String TAG = "CloudFunctionTest";

        @Override
        public void onClick(View view) {
            String inputText = inputYear.getText().toString();
            if (inputText.equals("") || !isInputLegit(inputText)){
                resultText.setText(R.string.year_error);
                return;
            }
            AGConnectFunction function = AGConnectFunction.getInstance();
            HashMap<String, String> map = new HashMap<>();
            map.put("year", inputText);
            Log.i(TAG, inputText);
            function.wrap("symbolic-animals-$latest").call(map).addOnCompleteListener(new OnCompleteListener<FunctionResult>() {
                @Override
                public void onComplete(Task<FunctionResult> task) {
                    if (task.isSuccessful()){
                        String value = task.getResult().getValue();
                        try {
                            JSONObject object = new JSONObject(value);
                            String result = (String)object.get("result");
                            resultText.setText(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, value);
                    } else {
                        Exception e = task.getException();
                        if (e instanceof AGCFunctionException){
                            AGCFunctionException functionException = (AGCFunctionException)e;
                            int errCode = functionException.getCode();
                            String message = functionException.getMessage();
                            Log.e(TAG, "errorCode: " + errCode + ", message: " + message);
                        }
                    }
                }
            });


        }

        private boolean isInputLegit(String input){
            for (int i = 0; i < input.length(); i++){
                System.out.println(input.charAt(i));
                if (!Character.isDigit(input.charAt(i))){
                    return false;
                }
            }
            return true;
        }
    }


}
