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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.SignInCenter;
import com.huawei.hms.jos.games.ArchivesClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.archive.ArchiveDetails;
import com.huawei.hms.jos.games.archive.ArchiveSummary;
import com.huawei.hms.jos.games.archive.ArchiveSummaryUpdate;
import com.wildma.pictureselector.PictureSelector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class OpenArchiveActivity extends Activity {
    private static final String TAG = "CommitArchiveActivity";

    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_add_dialog);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_cover_image)
    public void addImageCover() {
        PictureSelector.create(OpenArchiveActivity.this, PictureSelector.SELECT_REQUEST_CODE)
            .selectPicture(true, 200, 200, 1, 1);
    }

    @OnClick(R.id.commit)
    public void commit() {
        EditText descriptionEdit = findViewById(R.id.metadata_description);

        String description = descriptionEdit.getText().toString();
        EditText playedTimeEdit = findViewById(R.id.metadata_playedTime);
        long playedTime = String2Long(playedTimeEdit.getText().toString());
        EditText progressEdit = findViewById(R.id.metadata_progress);
        long progress = String2Long(progressEdit.getText().toString());

        if (TextUtils.isEmpty(description) && playedTime == 0 && progress == 0 && bitmap == null) {
            Log.w(TAG, "add archive failed, params is null");
        } else {
            ArchiveSummaryUpdate archiveMetadataChange = new ArchiveSummaryUpdate.Builder().setActiveTime(10000)
                .setCurrentProgress(200)
                .setDescInfo("Jerry is happy")
                .setThumbnail(bitmap)
                .setThumbnailMimeType("png")
                .build();
            ArchiveDetails archiveContents = new ArchiveDetails.Builder().build();
            archiveContents
                .set((description + "," + progress + "," + playedTime).getBytes(Charset.forName("UTF-8")));
            ArchivesClient archivesClient = Games.getArchiveClient(OpenArchiveActivity.this, SignInCenter.get().getAuthHuaweiId());
            Task<ArchiveSummary> task = archivesClient.addArchive(archiveContents, archiveMetadataChange, false);
            task.addOnSuccessListener(new OnSuccessListener<ArchiveSummary>() {
                @Override
                public void onSuccess(ArchiveSummary archiveSummary) {
                    if (archiveSummary != null) {
                        String content = "archiveId:" + archiveSummary.getId();
                        Toast.makeText(OpenArchiveActivity.this, content, Toast.LENGTH_LONG).show();
                }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    ApiException apiException = (ApiException) e;
                    String content = "add result:" + apiException.getStatusCode();
                    Toast.makeText(OpenArchiveActivity.this, content, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                try {
                    String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                    FileInputStream fs = new FileInputStream(picturePath);
                    bitmap = BitmapFactory.decodeStream(fs);
                    Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),bitmap , null,null));
                    ImageView imageView =  findViewById(R.id.image_cover);
                    Glide.with(OpenArchiveActivity.this).load(imageUri).into(imageView);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private long String2Long(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
