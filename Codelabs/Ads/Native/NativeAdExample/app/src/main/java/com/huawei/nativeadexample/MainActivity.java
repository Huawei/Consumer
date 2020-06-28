/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.huawei.nativeadexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.VideoOperator;
import com.huawei.hms.ads.nativead.DislikeAdListener;
import com.huawei.hms.ads.nativead.MediaView;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdConfiguration;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.ads.nativead.NativeView;

public class MainActivity extends AppCompatActivity {
    private RadioButton small;
    private RadioButton video;
    private Button loadBtn;
    private ScrollView adScrollView;

    private int layoutId;
    private NativeAd globalNativeAd;

    private VideoOperator.VideoLifecycleListener videoLifecycleListener = new VideoOperator.VideoLifecycleListener() {
        @Override
        public void onVideoStart() {
            updateStatus(getString(R.string.status_play_start), false);
        }

        @Override
        public void onVideoPlay() {
            updateStatus(getString(R.string.status_playing), false);
        }

        @Override
        public void onVideoEnd() {
            // If there is a video, load a new native ad only after video playback is complete.
            updateStatus(getString(R.string.status_play_end), true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this);

        small = findViewById(R.id.radio_button_small);
        video = findViewById(R.id.radio_button_video);
        loadBtn = findViewById(R.id.btn_load);
        adScrollView = findViewById(R.id.scroll_view_ad);

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAd(getAdId());
            }
        });

        loadAd(getAdId());
    }

    /**
     * Initialize ad slot ID and layout template.
     *
     * @return ad slot ID
     */
    private String getAdId() {
        String adId;
        layoutId = R.layout.native_video_template;
        if (small.isChecked()) {
            adId = getString(R.string.ad_id_native_small);
            layoutId = R.layout.native_small_template;
        } else if (video.isChecked()) {
            adId = getString(R.string.ad_id_native_video);
        } else {
            adId = getString(R.string.ad_id_native);
        }
        return adId;
    }

    /**
     * Load a native ad.
     *
     * @param adId ad slot ID.
     */
    private void loadAd(String adId) {
        updateStatus(null, false);

        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(this, adId);

        builder.setNativeAdLoadedListener(new NativeAd.NativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                // Call this method when an ad is successfully loaded.
                updateStatus(getString(R.string.status_load_ad_success), true);

                // Display native ad.
                showNativeAd(nativeAd);

                nativeAd.setDislikeAdListener(new DislikeAdListener() {
                    @Override
                    public void onAdDisliked() {
                        // Call this method when an ad is closed.
                        updateStatus(getString(R.string.ad_is_closed), true);
                    }
                });
            }
        }).setAdListener(new AdListener() {
            @Override
            public void onAdFailed(int errorCode) {
                // Call this method when an ad fails to be loaded.
                updateStatus(getString(R.string.status_load_ad_fail) + errorCode, true);
            }
        });

        NativeAdConfiguration adConfiguration = new NativeAdConfiguration.Builder()
                .setChoicesPosition(NativeAdConfiguration.ChoicesPosition.BOTTOM_RIGHT) // Set custom attributes.
                .build();

        NativeAdLoader nativeAdLoader = builder.setNativeAdOptions(adConfiguration).build();

        nativeAdLoader.loadAd(new AdParam.Builder().build());

        updateStatus(getString(R.string.status_ad_loading), false);
    }

    /**
     * Display native ad.
     *
     * @param nativeAd native ad object that contains ad materials.
     */
    private void showNativeAd(NativeAd nativeAd) {
        // Destroy the original native ad.
        if (null != globalNativeAd) {
            globalNativeAd.destroy();
        }
        globalNativeAd = nativeAd;

        // Obtain NativeView.
        NativeView nativeView = (NativeView) getLayoutInflater().inflate(layoutId, null);

        // Register and populate a native ad material view.
        initNativeAdView(globalNativeAd, nativeView);

        // Add NativeView to the app UI.
        adScrollView.removeAllViews();
        adScrollView.addView(nativeView);
    }

    /**
     * Register and populate a native ad material view.
     *
     * @param nativeAd   native ad object that contains ad materials.
     * @param nativeView native ad view to be populated into.
     */
    private void initNativeAdView(NativeAd nativeAd, NativeView nativeView) {
        // Register a native ad material view.
        nativeView.setTitleView(nativeView.findViewById(R.id.ad_title));
        nativeView.setMediaView((MediaView) nativeView.findViewById(R.id.ad_media));
        nativeView.setAdSourceView(nativeView.findViewById(R.id.ad_source));
        nativeView.setCallToActionView(nativeView.findViewById(R.id.ad_call_to_action));

        // Populate a native ad material view.
        ((TextView) nativeView.getTitleView()).setText(nativeAd.getTitle());
        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (null != nativeAd.getAdSource()) {
            ((TextView) nativeView.getAdSourceView()).setText(nativeAd.getAdSource());
        }
        nativeView.getAdSourceView()
                .setVisibility(null != nativeAd.getAdSource() ? View.VISIBLE : View.INVISIBLE);

        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        nativeView.getCallToActionView()
                .setVisibility(null != nativeAd.getCallToAction() ? View.VISIBLE : View.INVISIBLE);

        // Obtain a video controller.
        VideoOperator videoOperator = nativeAd.getVideoOperator();

        // Check whether a native ad contains video materials.
        if (videoOperator.hasVideo()) {
            // Add a video lifecycle event listener.
            videoOperator.setVideoLifecycleListener(videoLifecycleListener);
        }

        // Register a native ad object.
        nativeView.setNativeAd(nativeAd);
    }

    /**
     * Update tip and status of the load button.
     *
     * @param text           tip.
     * @param loadBtnEnabled status of the load button.
     */
    private void updateStatus(String text, boolean loadBtnEnabled) {
        if (null != text) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
        loadBtn.setEnabled(loadBtnEnabled);
    }

    @Override
    protected void onDestroy() {
        if (null != globalNativeAd) {
            globalNativeAd.destroy();
        }

        super.onDestroy();
    }
}
