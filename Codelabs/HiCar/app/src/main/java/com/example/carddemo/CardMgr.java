
package com.example.carddemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hicarsdk.builder.ButtonBuilder;
import com.huawei.hicarsdk.builder.Card;
import com.huawei.hicarsdk.builder.CardBuilder;
import com.huawei.hicarsdk.constant.ConstantEx;
import com.huawei.hicarsdk.exception.RemoteServiceNotRunning;
import com.huawei.hicarsdk.job.CreateCardBack;

import java.util.ArrayList;
import java.util.List;

public class CardMgr {
    public static List<Integer> mCardIdList = new ArrayList<>(10);

    int messageCardId;

    int mediaCardId;

    int mapCardId;

    int callCardId;

    private Context mContext;

    private Bitmap cover, cover1, cover2, cover3;

    private int musicIndex = 0;

    private String text = "";

    private String text1 = "Kalimba";

    private String text2 = "Maid with Flaxen Hair";

    private String text3 = "Sleep Away";

    private String subText = "";

    private String subText1 = "Kalimba";

    private String subText2 = "Richard Stoltzman";

    private String subText3 = "Bob Acri";

    private boolean isStarted = false;

    private Bitmap widgetPrev, widgetPlay, widgetPause, widgetNext, playBitmap;

    private CardBuilder mapCardBuilder;

    private CardBuilder callCardBuilder;

    public CardMgr(Context context) {
        mContext = context;
        initResource();
    }

    public int getmCardId() {
        if (mCardIdList != null && mCardIdList.size() >= 1) {
            return mCardIdList.get(mCardIdList.size() - 1);
        } else {
            return 0;
        }
    }

    private void initResource() {
        cover1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.album_cover_1);
        cover2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.album_cover_2);
        cover3 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.album_cover_3);
        widgetPrev = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_prev);
        widgetPlay = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_play);
        widgetPause = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_pause);
        widgetNext = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_next);
    }

    public void createCards() {

        CardBuilder messageCardBuilder = Card.createCardBuilder(mContext, 1, 0);
        messageCardBuilder.setTitle(0, "情景智能")
            .setInfoImage(mContext.getDrawable(R.drawable.image_departure), ConstantEx.InfoImageStyle.ICON)
            .setMainText("[img]10:15[img]", R.drawable.icon_huawei_intelligent, R.drawable.icon_huawei_intelligent)
            .setSubText("[img]上海浦东T2出发12:15到达深圳宝安", R.drawable.icon_huawei_intelligent)
            .setOptText("东航MU5413[img]", R.drawable.icon_huawei_music)
            .setPendingIntent(getActivityIntente());
        try {
            com.huawei.hicarsdk.builder.CardMgr.createCard(mContext, messageCardBuilder, new CreateCardBack() {
                @Override
                public void callBack(int i) {
                    messageCardId = i;
                    mCardIdList.add(i);
                }

                @Override
                public void remoteServiceNotRunning() {

                }
            });
        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
            remoteServiceNotRunning.printStackTrace();
        }

        changeMusic();
        /**
         *  Use MediaCardBuilder to complete the data structure of the media class card.
         *  For details, please refer to the SDK method annotation.
         */
        CardBuilder mediaCardBuilder = Card.createCardBuilder(mContext, 6, 0)
            .setTitle(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_huawei_music), "华为音乐")
            .setBackground(cover, ConstantEx.BackgroundStyle.IMMERSIVE)
            .setMainText(text)
            .setSubText(subText)
            .setPendingIntent(getActivityIntente());

        /**
         *  Construct the control data, each Bundle here will be attached to the corresponding control.
         *  When the control is clicked, hicar will pass the bundle back to the application through the
         *  callBackApp method, so that the three-party application can complete various kinds according
         *  to its own demands.
         */
        Bundle musicPrev = new Bundle();
        musicPrev.putString("action", "ACTION_PREV");
        Bundle musicPlay = new Bundle();
        musicPlay.putString("action", !isStarted ? "ACTION_PLAY" : "ACTION_SUS");
        Bundle musicNext = new Bundle();
        musicNext.putString("action", "ACTION_NEXT");
        Bundle button1 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.WIDGET)
            .icon(R.drawable.media_prev)
            .action(musicPrev)
            .index(1)
            .build();
        Bundle button2 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.WIDGET)
            .icon(R.drawable.media_play)
            .action(musicPlay)
            .index(2)
            .build();
        Bundle button3 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.WIDGET)
            .icon(R.drawable.media_next)
            .action(musicNext)
            .index(3)
            .build();
        mediaCardBuilder.setButtons(button1, button2, button3);
        try {
            com.huawei.hicarsdk.builder.CardMgr.createCard(mContext, mediaCardBuilder, new CreateCardBack() {
                @Override
                public void callBack(int i) {
                    mediaCardId = i;
                    mCardIdList.add(i);
                }

                @Override
                public void remoteServiceNotRunning() {
                }
            });
        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
        }
        /**
         *  CardSender provides three methods for data transfer using cardSender.
         *  One is to create a card, the other is to update the card, and the other
         *  is to actively destroy the card. When using the update or create card method,
         *  please make sure that hicar can complete the drawing of the whole card through the data in the builder.
         *  CardSender.createCard(mContext, mediaCardBuilder, cardId -> mediaCardId = cardId);
         */
        CardBuilder mapCardBuilder = Card.createCardBuilder(mContext, 6, 0);
        mapCardBuilder
            .setTitle(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_huawei_intelligent), "地图")
            .setInfoImage(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image_departure),
                ConstantEx.InfoImageStyle.ICON)
            .setBackground(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.espace))
            .setMainText("50米 左转")
            .setSubText("进入新金桥路")
            .setPendingIntent(getActivityIntente());
        Bundle button4 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.ROUND)
            .icon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_play))
            .action(musicPlay)
            .index(2)
            .build();

        mapCardBuilder.setButtons(button4);
        try {
            com.huawei.hicarsdk.builder.CardMgr.createCard(mContext, mapCardBuilder, new CreateCardBack() {
                @Override
                public void callBack(int i) {
                    mapCardId = i;
                    mCardIdList.add(i);
                }

                @Override
                public void remoteServiceNotRunning() {

                }
            });
        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
            remoteServiceNotRunning.printStackTrace();
        }
        mapCardBuilder = Card.createCardBuilder(mContext, 6, 0);
        mapCardBuilder
            .setTitle(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_huawei_intelligent), "地图")
            .setInfoImage(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image_departure),
                ConstantEx.InfoImageStyle.ICON)
            .setBackground(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.images))
            .setMainText("50米 左转")
            .setSubText("进入新金桥路")
            .setPendingIntent(getActivityIntente());
        Bundle button5 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.CHIPS)
            .text(" 结束导航")
            .action(musicPlay)
            .index(2)
            .build();

        mapCardBuilder.setButtons(button5);
        try {
            com.huawei.hicarsdk.builder.CardMgr.createCard(mContext, mapCardBuilder, new CreateCardBack() {
                @Override
                public void callBack(int i) {
                    mapCardId = i;
                    mCardIdList.add(i);
                }

                @Override
                public void remoteServiceNotRunning() {

                }
            });
        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
            remoteServiceNotRunning.printStackTrace();
        }
    }

    public void createMusicCard() {
        changeMusic();
        CardBuilder mediaCardBuilder = Card.createCardBuilder(mContext, 2, 0)
            .setTitle(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_huawei_music), "华为音乐")
            .setBackground(cover, ConstantEx.BackgroundStyle.IMMERSIVE)
            .setMainText(text)
            .setSubText(subText)
            .setPendingIntent(getActivityIntente());
        Bundle musicPrev = new Bundle();
        musicPrev.putString("action", "ACTION_PREV");
        Bundle musicPlay = new Bundle();
        musicPlay.putString("action", !isStarted ? "ACTION_PLAY" : "ACTION_SUS");
        Bundle musicNext = new Bundle();
        musicNext.putString("action", "ACTION_NEXT");
        Bundle button1 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.WIDGET)
            .icon(R.drawable.media_prev)
            .action(musicPrev)
            .index(1)
            .build();
        Bundle button2 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.WIDGET)
            .icon(R.drawable.media_play)
            .action(musicPlay)
            .index(2)
            .build();
        Bundle button3 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.WIDGET)
            .icon(R.drawable.media_next)
            .action(musicNext)
            .index(3)
            .build();
        mediaCardBuilder.setButtons(button1, button2, button3);
        try {
            com.huawei.hicarsdk.builder.CardMgr.createCard(mContext, mediaCardBuilder, new CreateCardBack() {
                @Override
                public void callBack(int i) {
                    mediaCardId = i;
                    mCardIdList.add(i);
                }

                @Override
                public void remoteServiceNotRunning() {
                }
            });
        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
        }
    }

    public void createNaviCard() {
        Bundle musicPlay = new Bundle();
        musicPlay.putString("action", !isStarted ? "ACTION_PLAY" : "ACTION_SUS");
        CardBuilder mapCardBuilder = Card.createCardBuilder(mContext, 1, 0);
        mapCardBuilder
            .setTitle(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_huawei_intelligent), "地图")
            .setInfoImage(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image_departure),
                ConstantEx.InfoImageStyle.ICON)
            .setBackground(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.espace))
            .setMainText("50米 左转")
            .setSubText("进入新金桥路")
            .setPendingIntent(getActivityIntente());
        Bundle button4 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.ROUND)
            .icon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_play))
            .action(musicPlay)
            .index(2)
            .build();

        mapCardBuilder.setButtons(button4);
        try {
            com.huawei.hicarsdk.builder.CardMgr.createCard(mContext, mapCardBuilder, new CreateCardBack() {
                @Override
                public void callBack(int i) {
                    mapCardId = i;
                    mCardIdList.add(i);
                }

                @Override
                public void remoteServiceNotRunning() {

                }
            });
        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
            remoteServiceNotRunning.printStackTrace();
        }
    }

    public void createQjCard(int callCardId) {
        initResource();
        CardBuilder messageCardBuilder = Card.createCardBuilder(mContext, 6, 0);
        messageCardBuilder.setTitle(callCardId, "情景智能" + callCardId)
            .setInfoImage(mContext.getDrawable(R.drawable.image_departure), ConstantEx.InfoImageStyle.ICON)
            .setMainText("[img]10:16[img]" + callCardId,
                R.drawable.icon_huawei_intelligent,
                R.drawable.icon_huawei_intelligent)
            .setSubText("[img]上海浦东T2出发12:15到达深圳宝安" + callCardId, R.drawable.icon_huawei_intelligent)
            .setOptText("东航MU5413[img]" + callCardId, R.drawable.icon_huawei_music)
            .setPendingIntent(getActivityIntente());
        try {
            com.huawei.hicarsdk.builder.CardMgr.createCard(mContext, messageCardBuilder, new CreateCardBack() {
                @Override
                public void callBack(int i) {
                    messageCardId = i;
                    mCardIdList.add(i);
                }

                @Override
                public void remoteServiceNotRunning() {

                }
            });
        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
            remoteServiceNotRunning.printStackTrace();
        }
    }

    public void createPutongCard(int callCardId) {
        initResource();
        CardBuilder messageCardBuilder = Card.createCardBuilder(mContext, 7, 0);
        messageCardBuilder.setTitle(callCardId, "智能家居" + callCardId)
            .setInfoImage(mContext.getDrawable(R.drawable.image_departure), ConstantEx.InfoImageStyle.ICON)
            .setMainText("[img]10:15[img]" + callCardId,
                R.drawable.icon_huawei_intelligent,
                R.drawable.icon_huawei_intelligent)
            .setSubText("[img]上海浦东T2出发12:15到达深圳宝安" + callCardId, R.drawable.icon_huawei_intelligent)
            .setOptText("东航MU5413[img]" + callCardId, R.drawable.icon_huawei_music)
            .setPendingIntent(getActivityIntente());
        try {
            com.huawei.hicarsdk.builder.CardMgr.createCard(mContext, messageCardBuilder, new CreateCardBack() {
                @Override
                public void callBack(int i) {
                    messageCardId = callCardId;
                    mCardIdList.add(i);
                }

                @Override
                public void remoteServiceNotRunning() {

                }
            });
        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
            remoteServiceNotRunning.printStackTrace();
        }
    }

    private void changeMusic() {
        cover = musicIndex == 0 ? cover1 : musicIndex == 1 ? cover2 : cover3;
        text = musicIndex == 0 ? text1 : musicIndex == 1 ? text2 : text3;
        subText = musicIndex == 0 ? subText1 : musicIndex == 1 ? subText2 : subText3;
        playBitmap = isStarted ? widgetPause : widgetPlay;
    }

    public void callBack(Bundle params) {
        initResource();
        String action = params.getString("action", "");
        switch (action) {
            case "ACTION_PREV":
                musicIndex = musicIndex + 2;
                musicIndex = musicIndex % 3;
                break;
            case "ACTION_PLAY":
                isStarted = !isStarted;
                break;
            case "ACTION_NEXT":
                musicIndex = musicIndex + 1;
                musicIndex = musicIndex % 3;
                break;
            case "ACTION_SUS":
                isStarted = !isStarted;
        }

        int cardId = params.getInt("cardId", -1);
        changeMusic();
        CardBuilder mediaCardBuilder = Card.createCardBuilder(mContext, 6, 0)
            .setBackground(cover, ConstantEx.BackgroundStyle.GRADIENT)
            .setMainText(text)
            .setSubText(subText)
            .setPendingIntent(getActivityIntente());
        Bundle musicPrev = new Bundle();
        musicPrev.putString("action", "ACTION_PREV");
        Bundle musicPlay = new Bundle();
        musicPlay.putString("action", !isStarted ? "ACTION_PLAY" : "ACTION_SUS");
        Bundle musicNext = new Bundle();
        musicNext.putString("action", "ACTION_NEXT");
        Bundle button1 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.WIDGET)
            .icon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_prev))
            .action(musicPrev)
            .index(1)
            .build();
        Bundle button2 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.WIDGET)
            .icon(isStarted ? BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_play)
                : BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_pause))
            .action(musicPlay)
            .index(2)
            .build();
        Bundle button3 = new ButtonBuilder(mContext).style(ConstantEx.ButtonStyle.WIDGET)
            .icon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.media_next))
            .action(musicNext)
            .index(3)
            .build();
        mediaCardBuilder.setButtons(button1, button2, button3);
        try {
            com.huawei.hicarsdk.builder.CardMgr.updateCard(mContext, cardId, mediaCardBuilder);
        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
            remoteServiceNotRunning.printStackTrace();
        }
    }

    private Intent getActivityIntente() {
        return new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            .setComponent(new ComponentName(mContext, MainActivity.class))
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
    }

    public void removeCards(int cardId) {
        try {
            if (cardId < 0) {
                for (int cardid : mCardIdList) {
                    Log.i("hhh", "removeCards: mapCardId=" + cardid);
                    com.huawei.hicarsdk.builder.CardMgr.destoryCard(mContext, cardid);
                }
                mCardIdList.clear();
            } else {
                int cardid = mCardIdList.get(cardId - 1);
                com.huawei.hicarsdk.builder.CardMgr.destoryCard(mContext, cardid);
                mCardIdList.remove(cardId - 1);
            }

        } catch (RemoteServiceNotRunning remoteServiceNotRunning) {
            remoteServiceNotRunning.printStackTrace();
        }
    }
}
