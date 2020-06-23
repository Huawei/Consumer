package com.lucky.agc.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.HWGameAuthProvider;
import com.huawei.agconnect.auth.HwIdAuthProvider;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.jos.games.player.Player;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

public class MainActivity extends AppCompatActivity {

    private static final int HUAWEIID_SIGNIN = 8000;
    private static final int HUAWEIGAME_SIGNIN = 7000;

    private Button anonymousBtn;
    private Button hwidBtn;
    private Button gameBtn;
    private TextView resultText;

    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anonymousBtn = findViewById(R.id.anonymous_button);
        hwidBtn = findViewById(R.id.hwid_button);
        gameBtn = findViewById(R.id.game_button);
        resultText = findViewById(R.id.result_text);

        String appid = AGConnectServicesConfig.fromContext(getApplicationContext()).getString("client/app_id");
        Log.i("1111", appid);

        anonymousBtn.setOnClickListener(view -> AGConnectAuth.getInstance().signInAnonymously()
                .addOnSuccessListener(signInResult -> {
                    AGConnectUser user = signInResult.getUser();
                    Toast.makeText(MainActivity.this, user.getUid(), Toast.LENGTH_LONG).show();
                    uId = user.getUid();
                    hwidBtn.setVisibility(View.VISIBLE);
                })
        .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Anonymous SignIn Failed", Toast.LENGTH_LONG).show()));

        hwidBtn.setOnClickListener(view -> {
            HuaweiIdAuthParams authParams = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setIdToken().createParams();
            HuaweiIdAuthService service = HuaweiIdAuthManager.getService(MainActivity.this, authParams);
            startActivityForResult(service.getSignInIntent(), HUAWEIID_SIGNIN);
        });

        gameBtn.setOnClickListener(view -> {
            HuaweiIdAuthParams authParams = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME).createParams();
            HuaweiIdAuthService service = HuaweiIdAuthManager.getService(this, authParams);
            startActivityForResult(service.getSignInIntent(), HUAWEIGAME_SIGNIN);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HUAWEIID_SIGNIN) {
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()){
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                String accessToken = huaweiAccount.getAccessToken();
                AGConnectAuthCredential credential = HwIdAuthProvider.credentialWithToken(accessToken);
                AGConnectAuth.getInstance().getCurrentUser().link(credential)
                        .addOnSuccessListener(signInResult -> {
                            AGConnectUser user = signInResult.getUser();
                            Toast.makeText(MainActivity.this, user.getUid(), Toast.LENGTH_LONG).show();
                            if (user.getUid().equals(uId)) {
                                resultText.setText(R.string.success_sign);
                                resultText.setTextColor(0xff00ff00);
                                resultText.setVisibility(View.VISIBLE);
                                gameBtn.setVisibility(View.VISIBLE);
                            }else{
                                resultText.setText(R.string.fail_sign);
                                resultText.setTextColor(0xffff0000);
                                resultText.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Link failed", Toast.LENGTH_LONG).show());
            }else{
                Toast.makeText(MainActivity.this, "HwID signIn failed" + (authHuaweiIdTask.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == HUAWEIGAME_SIGNIN) {
            if (data == null) {
                Toast.makeText(MainActivity.this, "Game Signin Intent is null", Toast.LENGTH_LONG).show();
                return;
            }
            Task<AuthHuaweiId> task = HuaweiIdAuthManager.parseAuthResultFromIntent(data);

            task.addOnSuccessListener(signInHuaweiId -> {
                PlayersClient client = Games.getPlayersClient(MainActivity.this, signInHuaweiId);
                Task<Player> playerTask = client.getCurrentPlayer();

                playerTask.addOnSuccessListener(player -> {
                    String imageUrl = player.hasHiResImage() ? player.getHiResImageUri().toString()
                            : player.getIconImageUri().toString();
                    AGConnectAuthCredential credential =
                            new HWGameAuthProvider.Builder().setPlayerSign(player.getPlayerSign())
                                    .setPlayerId(player.getPlayerId())
                                    .setDisplayName(player.getDisplayName())
                                    .setImageUrl(imageUrl)
                                    .setPlayerLevel(player.getLevel())
                                    .setSignTs(player.getSignTs())
                                    .build();

                    AGConnectAuth.getInstance().getCurrentUser().link(credential).addOnSuccessListener(signInResult -> {
                        AGConnectUser user = signInResult.getUser();
                        Toast.makeText(MainActivity.this, user.getUid(), Toast.LENGTH_LONG).show();
                    }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Link failed" + e.getMessage(), Toast.LENGTH_LONG).show());

                }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Huawei Game failed" + e.getMessage(), Toast.LENGTH_LONG).show());

            }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "HwID signIn failed" + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
}
