
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

package com.huawei.hms.game.archive;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.SignInCenter;
import com.huawei.hms.jos.games.ArchivesClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.archive.ArchiveSummary;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;

import java.util.List;

public class ArchiveListAdapter extends RecyclerView.Adapter<ArchiveListAdapter.ViewHolder> {
    private final Activity context;
    private OnBtnClickListener mBtnClickListener;
    private List<ArchiveSummary> archiveSummaries;
    ArchivesClient client;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView archiveCoverImage;
        TextView id;
        TextView name;
        TextView description;

        ViewHolder(View view) {
            super(view);
            archiveCoverImage = view.findViewById(R.id.archive_cover_image);
            id = view.findViewById(R.id.archive_id);
            name = view.findViewById(R.id.archive_name);
            description = view.findViewById(R.id.archive_des);
        }
    }

    ArchiveListAdapter(Activity mContext, List<ArchiveSummary> archiveSummaries, OnBtnClickListener btnClickListener) {
        this.context = mContext;
        this.archiveSummaries = archiveSummaries;
        this.mBtnClickListener = btnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.archive_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ArchiveSummary archiveSummary = archiveSummaries.get(position);
        final String id = archiveSummary.getId();
        holder.archiveCoverImage.setTag(id);
        String description = archiveSummary.getDescInfo();
        final String name = archiveSummary.getFileName();

        if (archiveSummary.hasThumbnail()) {
            holder.archiveCoverImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_panorama));
            Task<Bitmap> coverImageTask = getArchivesClient().getThumbnail(archiveSummary.getId());
            coverImageTask.addOnSuccessListener(new OnSuccessListener<Bitmap>() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    if (!context.isDestroyed() && id.equals(holder.archiveCoverImage.getTag())) {
                        holder.archiveCoverImage.setImageBitmap(bitmap);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    if (e instanceof ApiException) {
                        Toast.makeText(context,"load image failed"+ ((ApiException) e).getStatusCode(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        holder.id.setText(id);
        holder.name.setText(name);
        holder.description.setText(description);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return archiveSummaries.size();
    }

    public interface OnBtnClickListener {
        void onItemClick(int position);
    }

    protected AuthHuaweiId getAuthHuaweiId() {
        return SignInCenter.get().getAuthHuaweiId();
    }

    private ArchivesClient getArchivesClient() {
        if (client == null) {
            client = Games.getArchiveClient(context, getAuthHuaweiId());
        }
        return client;
    }
}
