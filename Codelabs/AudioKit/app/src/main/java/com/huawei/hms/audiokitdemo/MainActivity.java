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

package com.huawei.hms.audiokitdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.manager.HwAudioStatusListener;
import com.huawei.hms.audiokitdemo.sdk.PlayHelper;
import com.huawei.hms.audiokitdemo.ui.nowplaying.NowPlayingFragment;
import com.huawei.hms.audiokitdemo.ui.playbutton.PlayControlButtonFragment;
import com.huawei.hms.audiokitdemo.ui.seek.SeekBarFragment;
import com.huawei.hms.audiokitdemo.utils.PlayModeUtils;
import com.huawei.hms.audiokitdemo.utils.ViewUtils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Main Entry
 *
 * @since 2020-06-01
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private ImageView mPlayModeView;

    private PlayControlButtonFragment mPlayControlButtonFragment;

    private SeekBarFragment mSeekBarFragment;

    private NowPlayingFragment mNowPlayingFragment;

    private TextView mSongName;

    private TextView mSingerName;

    private static class CacheDialogNegativeButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            PlayHelper.getInstance().clearCache();
        }
    }

    private static class CacheDialogPositiveButtonClickListener implements DialogInterface.OnClickListener {
        EditText editText;

        CacheDialogPositiveButtonClickListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                if (editText.getEditableText() != null) {
                    String cacheSize = editText.getEditableText().toString();
                    PlayHelper.getInstance().setCacheSize(Integer.parseInt(cacheSize));
                }
            } catch (RuntimeException e) {
                Log.e(TAG, "RuntimeException");
            } catch (Exception e) {
                Log.e(TAG, "setCacheSize err");
            }
        }
    }

    private static class PlayUrlSongDialogPositiveButtonClickListener implements DialogInterface.OnClickListener {
        EditText editText;

        PlayUrlSongDialogPositiveButtonClickListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getEditableText() != null) {
                PlayHelper.getInstance().playItem(editText.getEditableText().toString());
            }
        }
    }

    private HwAudioStatusListener mPlayListener = new HwAudioStatusListener() {
        @Override
        public void onSongChange(HwAudioPlayItem song) {
            Log.i(TAG, "onSongChange");
            updateSongName(song);
            if (mNowPlayingFragment != null) {
                mNowPlayingFragment.updatePlayingPos();
            }
            if (mNowPlayingFragment != null) {
                mNowPlayingFragment.initIfEmpty();
            }
        }

        @Override
        public void onQueueChanged(List<HwAudioPlayItem> infos) {
            Log.i(TAG, "onQueueChanged");
            if (mNowPlayingFragment != null) {
                mNowPlayingFragment.initNowPlayingList();
            }
        }

        @Override
        public void onBufferProgress(int percent) {

        }

        @Override
        public void onPlayProgress(long currPos, long duration) {

        }

        @Override
        public void onPlayCompleted(boolean isStopped) {

        }

        @Override
        public void onPlayError(int errorCode, boolean isUserForcePlay) {
            Log.d(TAG, "errorCode: " + errorCode + "isUserForcePlay: " + isUserForcePlay);
        }

        @Override
        public void onPlayStateChange(boolean isPlaying, boolean isBuffering) {
            Log.i(TAG, "onPlayStateChange isPlaying:" + isPlaying + ", isBuffering:" + isBuffering);
            if (mPlayControlButtonFragment != null) {
                mPlayControlButtonFragment.setPauseButtonImage();
            }
            if (mSeekBarFragment != null) {
                mSeekBarFragment.refresh();
            }
            if (mNowPlayingFragment != null) {
                mNowPlayingFragment.updatePlayingPos();
            }
            PlayModeUtils.getInstance().updatePlayMode(MainActivity.this, mPlayModeView);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        checkReadPermission();
        PlayModeUtils.getInstance().updatePlayMode(this, mPlayModeView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PlayHelper.getInstance().addListener(mPlayListener);
        initViews();
    }

    private void initViews() {
        mSongName = ViewUtils.findViewById(this, R.id.songName);
        mSingerName = ViewUtils.findViewById(this, R.id.singerName);

        mSeekBarFragment = SeekBarFragment.newInstance();
        addFragment(R.id.pro_layout, mSeekBarFragment);
        mPlayControlButtonFragment = PlayControlButtonFragment.newInstance();
        addFragment(R.id.playback_play_layout, mPlayControlButtonFragment);
        mNowPlayingFragment = NowPlayingFragment.newInstance(true);
        addFragment(R.id.playlist_layout, mNowPlayingFragment);

        ImageView mVolumeSilent = ViewUtils.findViewById(this, R.id.volume_silent);
        mVolumeSilent.setOnClickListener(this);
        SeekBar mVolumeSeekBar = ViewUtils.findViewById(this, R.id.volume_seekbar);
        ViewUtils.setVisibility(mVolumeSeekBar, false);
        ViewUtils.setVisibility(mVolumeSilent, false);
        ImageView mSettingMenu = ViewUtils.findViewById(this, R.id.setting_content_layout);
        mSettingMenu.setOnClickListener(this);

        mPlayModeView = ViewUtils.findViewById(this, R.id.playmode_imagebutton);
        mPlayModeView.setOnClickListener(this);
    }

    private void addFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayHelper.getInstance().removeListener(mPlayListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playmode_imagebutton:
                PlayModeUtils.getInstance().changePlayMode(this, mPlayModeView);
                break;
            case R.id.setting_content_layout:
                showMenuDialog();
                break;
            default:
                break;
        }
    }

    private void updateSongName(HwAudioPlayItem playItem) {
        if (playItem == null) {
            return;
        }
        if (mSongName != null) {
            mSongName.setText(playItem.getAudioTitle());
        }
        if (mSingerName != null) {
            mSingerName.setText(playItem.getSinger());
        }
    }

    /**
     * checkReadPermission
     */
    private void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    private void showMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.menu_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onDialogClick,which: " + which);
                switch (which) {
                    case 0:
                        Log.i(TAG, "dialog click online_list");
                        PlayHelper.getInstance().buildOnlineList();
                        break;
                    case 1:
                        Log.i(TAG, "dialog click local_list");
                        PlayHelper.getInstance().buildLocal(MainActivity.this);
                        break;
                    case 2:
                        showCacheDialog();
                        break;
                    case 3:
                        showPlayUrlSongDialog();
                        break;
                    default:
                        break;
                }

            }
        });
        builder.create().show();
    }

    /**
     * Song cache dialog,show cache message
     */
    private void showCacheDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                getResources().getString(R.string.cur_cache_size) + PlayHelper.getInstance().getCacheSize() + ", "
                        + getResources().getString(R.string.cur_use_cache_size)
                        + PlayHelper.getInstance().getUsedCacheSize());
        View view = View.inflate(builder.getContext(), R.layout.cache_layout, null);
        final EditText editText = ViewUtils.findViewById(view, R.id.cache_edit);
        builder.setView(view);
        builder.setNegativeButton(getResources().getString(R.string.clear_cache),
                new CacheDialogNegativeButtonClickListener());
        builder.setPositiveButton(getResources().getString(R.string.ok),
                new CacheDialogPositiveButtonClickListener(editText));
        builder.create().show();
    }

    /**
     * play url dialog <BR/>
     * play the music by user input
     */
    private void showPlayUrlSongDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.set_play));
        View view = View.inflate(builder.getContext(), R.layout.set_play_layout, null);
        final EditText editText = ViewUtils.findViewById(view, R.id.path_edit);
        builder.setView(view);
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        builder.setPositiveButton(getResources().getString(R.string.btn_play),
                new PlayUrlSongDialogPositiveButtonClickListener(editText));
        builder.create().show();
    }
}
