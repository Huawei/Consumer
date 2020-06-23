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

package com.huawei.hms.audiokitdemo.ui.nowplaying;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokitdemo.R;
import com.huawei.hms.audiokitdemo.sdk.PlayHelper;
import com.huawei.hms.audiokitdemo.utils.ViewUtils;

import java.util.List;

/**
 * NowPlayingAdapter
 *
 * @since 2020-06-01
 */
public class NowPlayingAdapter extends BaseSimpleAdapter<HwAudioPlayItem> {

    private static class ViewHolder {

        private TextView mTrackName = null;

        private TextView mArtistName = null;

        private BufferMelody mBufferMelody = null;

        private ImageView mDeleteView;

        private View lineImage = null;
    }

    private int listItemMargin;

    private static class DeleteViewClickListener implements View.OnClickListener {
        int pos;

        DeleteViewClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            PlayHelper.getInstance().deleteItem(pos);
        }
    }

    /**
     * NowPlayingAdapter
     *
     * @param netSongInfos song list info
     * @param activity     activity
     */
    NowPlayingAdapter(List<HwAudioPlayItem> netSongInfos, Activity activity) {
        super(activity);
        listItemMargin = activity.getResources().getDimensionPixelSize(R.dimen.layout_margin_left_and_right);
        if (netSongInfos != null) {
            mDataSource.addAll(netSongInfos);
        }
    }

    @Override
    public View getView(int pos, View contentView, ViewGroup arg2) {
        contentView = getContentView(contentView);

        HwAudioPlayItem showBean = mDataSource.get(pos);
        if (null == showBean) {
            return contentView;
        }

        initItemView(showBean, contentView, pos);

        return contentView;
    }

    private View getContentView(View contentView) {
        ViewHolder viewholder;
        if (contentView == null) {
            viewholder = new ViewHolder();
            contentView = mInflater.inflate(R.layout.simple_song_list_item, null);
            viewholder.mTrackName = ViewUtils.findViewById(contentView, R.id.line1);
            viewholder.mArtistName = ViewUtils.findViewById(contentView, R.id.line2);
            viewholder.mBufferMelody = ViewUtils.findViewById(contentView, R.id.melody_area);
            viewholder.lineImage = ViewUtils.findViewById(contentView, R.id.simple_line);
            viewholder.mDeleteView = ViewUtils.findViewById(contentView, R.id.delete);
            ViewUtils.setVisibility(viewholder.mBufferMelody, true);
            contentView.setTag(viewholder);
        }
        return contentView;
    }

    /**
     * initItemView<BR>
     *
     * @param songBean    songBean
     * @param contentView contentView
     * @param pos         pos
     */
    private void initItemView(HwAudioPlayItem songBean, View contentView, final int pos) {
        ViewHolder viewholder = (ViewHolder) contentView.getTag();
        contentView.setPadding(listItemMargin, 0, listItemMargin, 0);
        viewholder.mTrackName.setText(songBean.getAudioTitle());
        viewholder.mArtistName.setText(songBean.getSinger());
        viewholder.mDeleteView.setOnClickListener(new DeleteViewClickListener(pos));
        ViewUtils.setVisibility(viewholder.lineImage, (getCount() - 1) != pos);
        viewholder.mBufferMelody.setColor(Color.WHITE);
        viewholder.mBufferMelody.updateState(songBean);
    }
}
