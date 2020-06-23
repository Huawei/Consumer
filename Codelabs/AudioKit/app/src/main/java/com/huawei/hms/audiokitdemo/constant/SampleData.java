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

package com.huawei.hms.audiokitdemo.constant;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.huawei.hms.api.bean.HwAudioPlayItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * SampleData
 *
 * @since 2020-06-01
 */
public class SampleData {

    private static final String TAG = "SampleData";

    /**
     * get online PlayList
     *
     * @return play list
     */
    public List<HwAudioPlayItem> getOnlinePlaylist() {
        List<HwAudioPlayItem> playItemList = new ArrayList<>();
        HwAudioPlayItem audioPlayItem1 = new HwAudioPlayItem();
        audioPlayItem1.setAudioId("1000");
        audioPlayItem1.setSinger("Taoge");
        audioPlayItem1.setOnlinePath("https://lfmusicservice.hwcloudtest.cn:18084/HMS/audio/Taoge-chengshilvren.mp3");
        audioPlayItem1.setOnline(1);
        audioPlayItem1.setAudioTitle("chengshilvren");
        playItemList.add(audioPlayItem1);

        HwAudioPlayItem audioPlayItem2 = new HwAudioPlayItem();
        audioPlayItem2.setAudioId("1001");
        audioPlayItem2.setSinger("Taoge");
        audioPlayItem2.setOnlinePath("https://lfmusicservice.hwcloudtest.cn:18084/HMS/audio/Taoge-dayu.mp3");
        audioPlayItem2.setOnline(1);
        audioPlayItem2.setAudioTitle("dayu");
        playItemList.add(audioPlayItem2);

        HwAudioPlayItem audioPlayItem3 = new HwAudioPlayItem();
        audioPlayItem3.setAudioId("1002");
        audioPlayItem3.setSinger("Taoge");
        audioPlayItem3.setOnlinePath("https://lfmusicservice.hwcloudtest.cn:18084/HMS/audio/Taoge-wangge.mp3");
        audioPlayItem3.setOnline(1);
        audioPlayItem3.setAudioTitle("wangge");
        playItemList.add(audioPlayItem3);
        return playItemList;
    }

    /**
     * get Local PlayList
     *
     * @return play list
     */
    public List<HwAudioPlayItem> getLocalPlayList(Context context) {
        List<HwAudioPlayItem> playItemList = new ArrayList<>();
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver == null) {
                return playItemList;
            }
            cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            HwAudioPlayItem songItem;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    if (new File(path).exists()) {
                        songItem = new HwAudioPlayItem();
                        songItem.setAudioTitle(
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                        songItem
                            .setAudioId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)) + "");
                        songItem.setFilePath(path);
                        songItem
                            .setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                        playItemList.add(songItem);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, TAG, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.i(TAG, "getLocalPlayList: " + playItemList.size());
        return playItemList;
    }
}
