/*
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

package com.huawei.hms.audiokitdemo.sdk;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.callback.HwAudioConfigCallBack;
import com.huawei.hms.audiokit.player.manager.HwAudioConfigManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManagerFactory;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerConfig;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerManager;
import com.huawei.hms.audiokit.player.manager.HwAudioQueueManager;
import com.huawei.hms.audiokit.player.manager.HwAudioStatusListener;
import com.huawei.hms.audiokitdemo.constant.SampleData;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Control playback
 *
 * @since 2020-06-01
 */
public class PlayHelper {

    private static final String TAG = "PlayHelper";

    private static final int SIZE_M = 1024 * 1024;

    private static final PlayHelper INSTANCE = new PlayHelper();

    private HwAudioPlayerManager mHwAudioPlayerManager;

    private HwAudioQueueManager mHwAudioQueueManager;

    private HwAudioConfigManager mHwAudioConfigManager;

    private HwAudioManager mHwAudioManager;

    private List<HwAudioStatusListener> mTempListeners = new CopyOnWriteArrayList<>();

    private SampleData sampleData = new SampleData();

    private PlayHelper() {
    }

    /**
     * get PlayHelper Instance
     *
     * @return PlayHelper Instance
     */
    public static PlayHelper getInstance() {
        return INSTANCE;
    }

    /**
     * init sdk
     *
     * @param context context
     */
    @SuppressLint("StaticFieldLeak")
    public void init(final Context context) {
        Log.i(TAG, "init start");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                HwAudioPlayerConfig hwAudioPlayerConfig = new HwAudioPlayerConfig(context);
                HwAudioManagerFactory.createHwAudioManager(hwAudioPlayerConfig, new HwAudioConfigCallBack() {
                    @Override
                    public void onSuccess(HwAudioManager hwAudioManager) {
                        try {
                            Log.i(TAG, "createHwAudioManager onSuccess");
                            mHwAudioManager = hwAudioManager;
                            mHwAudioPlayerManager = hwAudioManager.getPlayerManager();
                            mHwAudioQueueManager = hwAudioManager.getQueueManager();
                            mHwAudioConfigManager = hwAudioManager.getConfigManager();
                            doRestInit(context);
                        } catch (Exception e) {
                            Log.e(TAG, "player init fail", e);
                        }
                    }

                    @Override
                    public void onError(int errorCode) {
                        Log.e(TAG, "init err:" + errorCode);
                    }
                });
                return null;
            }
        }.execute();
    }

    private void doRestInit(final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (HwAudioStatusListener listener : mTempListeners) {
                    try {
                        mHwAudioManager.addPlayerStatusListener(listener);
                    } catch (RemoteException e) {
                        Log.e(TAG, TAG, e);
                    }
                }
                mHwAudioConfigManager.setSaveQueue(true);
                mHwAudioConfigManager.setNotificationFactory(
                        new SubINotificationFactory((Application) context.getApplicationContext(), mHwAudioManager));
            }
        });
    }

    /**
     * addListener
     *
     * @param listener listener
     */
    public void addListener(HwAudioStatusListener listener) {
        if (mHwAudioManager != null) {
            try {
                mHwAudioManager.addPlayerStatusListener(listener);
            } catch (RemoteException e) {
                Log.e(TAG, TAG, e);
            }
        } else {
            mTempListeners.add(listener);
        }
    }

    /**
     * removeListener
     *
     * @param listener listener
     */
    public void removeListener(HwAudioStatusListener listener) {
        if (mHwAudioManager != null) {
            try {
                mHwAudioManager.removePlayerStatusListener(listener);
            } catch (RemoteException e) {
                Log.e(TAG, TAG, e);
            }
        }
        mTempListeners.remove(listener);
    }

    /**
     * build local music list
     *
     * @param context context
     */
    public void buildLocal(Context context) {
        if (context != null && mHwAudioPlayerManager != null) {
            mHwAudioPlayerManager.playList(sampleData.getLocalPlayList(context), 0, 0);
        }
    }

    /**
     * seek
     *
     * @param pos pos
     */
    public void seek(long pos) {
        Log.i(TAG, "seek: " + pos);
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "seek fail ");
            return;
        }
        mHwAudioPlayerManager.seekTo((int) pos);
    }

    /**
     * build online play  list
     */
    public void buildOnlineList() {
        Log.i(TAG, "buildOnlineList");
        List<HwAudioPlayItem> playItemList = sampleData.getOnlinePlaylist();
        if (mHwAudioPlayerManager != null) {
            mHwAudioPlayerManager.playList(playItemList, 0, 0);
        }
    }

    /**
     * playItem
     *
     * @param path audio path
     */
    public void playItem(String path) {
        Log.i(TAG, "playItem");
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "playItem err");
            return;
        }
        Log.i(TAG, "playItem,path:  " + path);
        if (path == null || path.length() == 0) {
            return;
        }
        HwAudioPlayItem item = new HwAudioPlayItem();
        item.setAudioTitle("Playing input song");
        item.setAudioId(String.valueOf(path.hashCode()));
        if (path.startsWith("http") || path.startsWith("https")) {
            item.setOnline(1);
            item.setOnlinePath(path);
        } else {
            item.setOnline(0);
            item.setFilePath(path);
        }
        List<HwAudioPlayItem> playItemList = new ArrayList<>();
        playItemList.add(item);
        mHwAudioPlayerManager.playList(playItemList, 0, 0);
    }

    /**
     * getBufferPercentage
     *
     * @return buffer percent
     */
    public int getBufferPercentage() {
        if (mHwAudioPlayerManager == null) {
            return 0;
        }
        return mHwAudioPlayerManager.getBufferPercent();
    }

    /**
     * getPosition
     *
     * @return now playing position
     */
    public long getPosition() {
        if (mHwAudioPlayerManager == null) {
            return 0;
        }
        return mHwAudioPlayerManager.getOffsetTime();
    }

    /**
     * Stop
     */
    public void stop() {
        if (mHwAudioPlayerManager == null) {
            return;
        }
        mHwAudioPlayerManager.stop();
    }

    /**
     * getDuration
     *
     * @return duration
     */
    public long getDuration() {
        if (mHwAudioPlayerManager == null) {
            return 0;
        }
        return mHwAudioPlayerManager.getDuration();
    }

    /**
     * check is playing
     *
     * @return playing
     */
    public boolean isPlaying() {
        return mHwAudioPlayerManager != null && mHwAudioPlayerManager.isPlaying();
    }

    /**
     * play next song
     */
    public void next() {
        Log.i(TAG, "next");
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "next err");
            return;
        }
        mHwAudioPlayerManager.playNext();
    }

    /**
     * play prev song
     */
    public void prev() {
        Log.i(TAG, "prev");
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "prev err");
            return;
        }
        mHwAudioPlayerManager.playPre();
    }

    /**
     * play
     */
    public void play() {
        Log.i(TAG, "play");
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "play err");
            return;
        }
        mHwAudioPlayerManager.play();
    }

    /**
     * pause
     */
    public void pause() {
        Log.i(TAG, "pause");
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "pause err");
            return;
        }
        mHwAudioPlayerManager.pause();
    }

    /**
     * set play mode
     *
     * @param mode new mode
     */
    public void setPlayMode(int mode) {
        Log.i(TAG, "setPlayMode: " + mode);
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "play err");
            return;
        }
        mHwAudioPlayerManager.setPlayMode(mode);
    }

    /**
     * get play mode
     *
     * @return play mode
     */
    public int getPlayMode() {
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "getPlayMode err");
            return 0;
        }
        return mHwAudioPlayerManager.getPlayMode();
    }

    /**
     * get current play item
     *
     * @return current play item
     */
    public HwAudioPlayItem getCurrentPlayItem() {
        Log.i(TAG, "getCurrentPlayItem");
        if (mHwAudioQueueManager == null) {
            Log.w(TAG, "getCurrentPlayItem err");
            return null;
        }
        return mHwAudioQueueManager.getCurrentPlayItem();
    }

    /**
     * get queue position
     *
     * @return queue position
     */
    public int getCurrentIndex() {
        if (mHwAudioQueueManager == null) {
            Log.w(TAG, "getCurrentIndex err");
            return 0;
        }
        return mHwAudioQueueManager.getCurrentIndex();
    }

    public List<HwAudioPlayItem> getAllPlaylist() {
        Log.i(TAG, "getAllPlaylist");
        if (mHwAudioQueueManager == null) {
            Log.w(TAG, "getAllPlaylist err");
            return null;
        }
        return mHwAudioQueueManager.getAllPlaylist();
    }

    /**
     * play item at position
     *
     * @param pos position
     */
    public void playAt(int pos) {
        Log.i(TAG, "setQueuePosition,pos: " + pos);
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "setQueuePosition err");
            return;
        }
        mHwAudioPlayerManager.play(pos);
    }

    /**
     * isBuffering
     *
     * @return isBuffering
     */
    public boolean isBuffering() {
        return mHwAudioPlayerManager != null && mHwAudioPlayerManager.isBuffering();
    }

    /**
     * is queue empty
     *
     * @return true:empty
     */
    public boolean isQueueEmpty() {
        return mHwAudioQueueManager != null && mHwAudioQueueManager.isQueueEmpty();
    }

    /**
     * delete item
     *
     * @param pos position of queue
     */
    public void deleteItem(int pos) {
        Log.i(TAG, "deleteItem,pos: " + pos);
        if (mHwAudioQueueManager == null) {
            Log.w(TAG, "deleteItem err");
            return;
        }
        mHwAudioQueueManager.removeListByIndex(pos);
    }

    /**
     * get cache size
     *
     * @return cache size, unit MB
     */
    public String getCacheSize() {
        Log.i(TAG, "getCacheSize ");
        if (mHwAudioConfigManager == null) {
            Log.w(TAG, "getCacheSize err");
            return "";
        }
        long size = mHwAudioConfigManager.getPlayCacheSize() / SIZE_M;
        DecimalFormat format = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.ENGLISH));
        return format.format(size) + "M";
    }

    /**
     * get current used cache size
     *
     * @return current used cache size, unit MB
     */
    public String getUsedCacheSize() {
        Log.i(TAG, "getUsedCacheSize ");
        if (mHwAudioConfigManager == null) {
            Log.w(TAG, "getUsedCacheSize err");
            return "";
        }
        long size = mHwAudioConfigManager.getUsedCacheSize() / SIZE_M;
        DecimalFormat format = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.ENGLISH));
        return format.format(size) + "M";
    }

    /**
     * clear cache
     */
    public void clearCache() {
        Log.i(TAG, "clearCache ");
        if (mHwAudioConfigManager == null) {
            Log.w(TAG, "clearCache err");
            return;
        }
        mHwAudioConfigManager.clearPlayCache();
    }

    /**
     * set cache size
     *
     * @param size size ,unit MB
     */
    public void setCacheSize(long size) {
        Log.i(TAG, "setCacheSize ,size：" + size);
        if (mHwAudioConfigManager == null || size < 0) {
            Log.w(TAG, "setCacheSize err");
            return;
        }
        mHwAudioConfigManager.setPlayCacheSize(size * SIZE_M);
    }
}
