package com.example.carddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int cardId;

    private Button btnCreateCard;

    private CardMgr mCardMgr;

    private EditText editTextCardId;

    private Button btnMusic;

    private Button btnNavi;

    private Button btnDeleteCard;

    private Button btnPutong;

    private Button btnDeleteOneCard;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreateCard = findViewById(R.id.addQJCard);
        editTextCardId = findViewById(R.id.cardIdEditText);
        btnMusic = findViewById(R.id.addMusicCard);
        btnNavi = findViewById(R.id.addNaviCard);
        btnDeleteCard = findViewById(R.id.deleteCards);
        btnPutong = findViewById(R.id.addPutongCards);
        mCardMgr = new CardMgr(getApplicationContext());
        btnDeleteOneCard = findViewById(R.id.deleteoneCard);
        textView = findViewById(R.id.cardidList);

        btnCreateCard.setOnClickListener(view -> {
            cardId = Integer.parseInt(editTextCardId.getText().toString());
            mCardMgr.createQjCard(cardId);
            textView.setText(textView.getText() + "\n" + mCardMgr.getmCardId());
        });

        btnMusic.setOnClickListener(view -> {
            cardId = Integer.parseInt(editTextCardId.getText().toString());
            mCardMgr.createMusicCard();
            textView.setText(textView.getText() + "\n" + mCardMgr.getmCardId());
        });

        btnNavi.setOnClickListener(view -> {
            cardId = Integer.parseInt(editTextCardId.getText().toString());
            mCardMgr.createNaviCard();
            textView.setText(textView.getText() + "\n" + mCardMgr.getmCardId());
        });

        btnDeleteCard.setOnClickListener(view -> {
            mCardMgr.removeCards(-1);
            textView.setText("卡片id：\n");
        });

        btnPutong.setOnClickListener(view -> {
            mCardMgr.createPutongCard(cardId);
            textView.setText(textView.getText() + "\n" + mCardMgr.getmCardId());
        });

        btnDeleteOneCard.setOnClickListener(view -> {
            cardId = Integer.parseInt(editTextCardId.getText().toString());
            mCardMgr.removeCards(cardId);
        });
    }
}
