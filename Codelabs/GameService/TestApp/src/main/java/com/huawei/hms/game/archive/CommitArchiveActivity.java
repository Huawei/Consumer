
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
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.R;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.game.common.BaseActivity;
import com.huawei.hms.game.common.SignInCenter;
import com.huawei.hms.game.common.TimeUtil;
import com.huawei.hms.jos.games.ArchivesClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.GamesStatusCodes;
import com.huawei.hms.jos.games.archive.Archive;
import com.huawei.hms.jos.games.archive.ArchiveDetails;
import com.huawei.hms.jos.games.archive.ArchiveSummary;
import com.huawei.hms.jos.games.archive.ArchiveSummaryUpdate;
import com.huawei.hms.jos.games.archive.OperationResult;
import com.wildma.pictureselector.PictureSelector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommitArchiveActivity extends BaseActivity {
    private static final String TAG = "CommitArchiveActivity";

    private Bitmap bitmap;

    private String archiveId;

    private String description;

    private long playedTime;

    private long progress;

    private boolean hasThumbnail;

    private String id;

    @BindView(R.id.not_support_image)
    public RadioButton notSupportImage;

    @BindView(R.id.support_image)
    public RadioButton supportImageRadio;

    @BindView(R.id.not_support)
    public RadioButton notSupport;

    @BindView(R.id.support)
    public RadioButton support;

    @BindView(R.id.metadata_description)
    public EditText editTextDescription;

    @BindView(R.id.metadata_playedTime)
    public EditText editTextPlayedTime;

    @BindView(R.id.metadata_progress)
    public EditText editTextProgress;

    @BindView(R.id.image_cover)
    public ImageView coverImage;

    @BindView(R.id.et_image_type)
    public EditText editTextImageType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_add_dialog);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (data != null) {
            archiveId = data.getString("archiveId");
            description = data.getString("description");
            progress = data.getLong("progressValue", 0);
            playedTime = data.getLong("playedTime", 0);
            hasThumbnail = data.getBoolean("hasThumbnail");
            id = data.getString("id");
        }

        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        if (!TextUtils.isEmpty(description)) {
            editTextDescription.setText(description);
        }

        if (playedTime != 0) {
            editTextPlayedTime.setText(String.valueOf(playedTime));
        }

        if (progress != 0) {
            editTextProgress.setText(String.valueOf(progress));
        }

        if (progress != 0) {
            editTextProgress.setText(String.valueOf(progress));
        }

        if (hasThumbnail) {
            final RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
            Task<Bitmap> coverImageTask = getArchivesClient().getThumbnail(id);
            coverImageTask.addOnSuccessListener(new OnSuccessListener<Bitmap>() {
                @Override
                public void onSuccess(Bitmap bitmap1) {
                    bitmap = bitmap1;
                    Glide.with(getApplicationContext()).load(bitmap1).apply(options).into(coverImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    if (e instanceof ApiException) {
                        Toast.makeText(getApplicationContext(),"load image failed"+ ((ApiException) e).getStatusCode(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private ArchivesClient archivesClient;

    private synchronized ArchivesClient getArchivesClient() {
        if (archivesClient == null) {
            archivesClient = Games.getArchiveClient(CommitArchiveActivity.this, SignInCenter.get().getAuthHuaweiId());
        }
        return archivesClient;
    }

    @OnClick(R.id.add_cover_image)
    public void addImageCover() {
        PictureSelector.create(CommitArchiveActivity.this, PictureSelector.SELECT_REQUEST_CODE)
            .selectPicture(true, 200, 200, 1, 1);
    }

    private Bitmap.CompressFormat getMimeType(String mimeType) {
        if ("JPG".equalsIgnoreCase(mimeType)) {
            return Bitmap.CompressFormat.JPEG;
        }
        if ("PNG".equalsIgnoreCase(mimeType)) {
            return Bitmap.CompressFormat.PNG;
        }
        return null;
    }

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public Bitmap returnBitMap() throws InterruptedException {
        if (hasThumbnail) {
            Task<Bitmap> coverImageTask = getArchivesClient().getThumbnail(id);
            coverImageTask.addOnSuccessListener(new OnSuccessListener<Bitmap>() {
                @Override
                public void onSuccess(Bitmap bitmap1) {
                    bitmap = bitmap1;
                    countDownLatch.countDown();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    if (e instanceof ApiException) {
                        Toast.makeText(getApplicationContext(),"load image failed"+ ((ApiException) e).getStatusCode(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        countDownLatch.await(3000, TimeUnit.MILLISECONDS);
        return bitmap;
    }

    @OnClick(R.id.commit)
    public void commit() {
        String description = editTextDescription.getText().toString();
        long playedTime = String2Long(editTextPlayedTime.getText().toString());
        long progress = String2Long(editTextProgress.getText().toString());
        boolean support = !notSupport.isChecked();
        boolean supportImage = supportImageRadio.isChecked();

        if (TextUtils.isEmpty(description) && playedTime == 0 && progress == 0 && bitmap == null) {
            Log.w(TAG, "add archive failed, params is null");
        } else {
            if (bitmap == null && hasThumbnail) {
                try {
                    bitmap = returnBitMap();
                } catch (InterruptedException e) {
                    Log.w(TAG, "add archive failed, params is null InterruptedException");
                }
            }
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.demoicon);
            }

            String imageType = editTextImageType.getText().toString();
            ArchiveSummaryUpdate.Builder builder = new ArchiveSummaryUpdate.Builder().setActiveTime(playedTime)
                    .setCurrentProgress(progress)
                    .setDescInfo(description);
            if (supportImage) {
                builder.setThumbnail(bitmap).setThumbnailMimeType(imageType);
            }
            ArchiveSummaryUpdate archiveMetadataChange = builder.build();
            ArchiveDetails archiveContents = new ArchiveDetails.Builder().build();
            archiveContents.set((progress + description + playedTime).getBytes());

            if (TextUtils.isEmpty(archiveId)) {
                Task<ArchiveSummary> task = getArchivesClient().addArchive(archiveContents, archiveMetadataChange, support);
                task.addOnSuccessListener(new OnSuccessListener<ArchiveSummary>() {
                    @Override
                    public void onSuccess(ArchiveSummary archiveSummary) {
                        if (archiveSummary != null) {
                            showLog("UniqueName:" + archiveSummary.getFileName());
                            showLog("PlayedTime:" + archiveSummary.getActiveTime());
                            showLog("ProgressValue:" + archiveSummary.getCurrentProgress());
                            showLog("ModifiedTimestamp:" + TimeUtil.longToUTC(archiveSummary.getRecentUpdateTime()));
                            showLog("CoverImageAspectRatio:" + archiveSummary.getThumbnailRatio());
                            showLog("hasThumbnail:" + archiveSummary.hasThumbnail());
                            showLog("ArchiveId:" + archiveSummary.getId());
                            showLog("Description:" + archiveSummary.getDescInfo());

                            showPlayerAndGame(archiveSummary);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        ApiException apiException = (ApiException) e;
                        final String content = "add result:" + apiException.getStatusCode();
                        Toast.makeText(CommitArchiveActivity.this, content, Toast.LENGTH_LONG).show();
                        if (apiException.getStatusCode() == GamesStatusCodes.GAME_STATE_ARCHIVE_NO_DRIVE) {
                            guideToAgreeDriveProtocol();
                        }
                    }
                });
            } else {
                Task<OperationResult> task =
                        getArchivesClient().updateArchive(archiveId, archiveMetadataChange, archiveContents);

                task.addOnSuccessListener(new OnSuccessListener<OperationResult>() {
                    @Override
                    public void onSuccess(OperationResult archiveDataOrConflict) {
                        showLog("isDifference:"
                            + ((archiveDataOrConflict == null) ? "" : archiveDataOrConflict.isDifference()));
                        if (archiveDataOrConflict != null && !archiveDataOrConflict.isDifference()) {
                            Archive archive = archiveDataOrConflict.getArchive();
                            if (archive != null && archive.getSummary() != null) {
                                showLog("ArchiveId:" + archive.getSummary().getId());
                                try {
                                    showLog("content:" + new String(archive.getDetails().get(), "UTF-8"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                showLog("UniqueName:" + archive.getSummary().getFileName());
                                showLog("PlayedTime:" + archive.getSummary().getActiveTime());
                                showLog("ProgressValue:" + archive.getSummary().getCurrentProgress());
                                showLog("ModifiedTimestamp:"
                                    + TimeUtil.longToUTC(archive.getSummary().getRecentUpdateTime()));
                                showLog("CoverImageAspectRatio:" + archive.getSummary().getThumbnailRatio());
                                showLog("Description:" + archive.getSummary().getDescInfo());
                                showLog("hasThumbnail:" + archive.getSummary().hasThumbnail());

                                showPlayerAndGame(archive.getSummary());
                            }
                        } else {
                            // Conflict resolution
                            handleConflict(archiveDataOrConflict);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        ApiException apiException = (ApiException) e;
                        showLog("loadArchiveDetails result:" + apiException.getStatusCode());
                        if (apiException.getStatusCode() == GamesStatusCodes.GAME_STATE_ARCHIVE_NO_DRIVE) {
                            guideToAgreeDriveProtocol();
                        }
                    }
                });
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Result callback */
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                try {
                    String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                    FileInputStream fs = new FileInputStream(picturePath);
                    bitmap = BitmapFactory.decodeStream(fs);
                    Uri imageUri =
                        Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));

                    RequestOptions options = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true);
                    Glide.with(CommitArchiveActivity.this).load(imageUri).apply(options).into(coverImage);
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

    private String getArchiveMessage(Archive archive) {
        return "PlayedTime:" + archive.getSummary().getActiveTime() + "\n" + "Progress:"
            + archive.getSummary().getCurrentProgress() + "\n" + "Description:" + archive.getSummary().getDescInfo()
            + "\n" + "ModifyTime:" + archive.getSummary().getRecentUpdateTime();
    }

    private void showConflictDialog(final Archive openArchive, final Archive serverArchive,
                                    final ArchiveMetadataDetailActivity.ConflictResolveCallback callback) {

        if (openArchive == null || serverArchive == null) {
            return;
        }

        final ConflictDialog dialog = new ConflictDialog(this);

        String recentMessage = getArchiveMessage(openArchive);
        String serverMessage = getArchiveMessage(serverArchive);

        dialog.setRecentMessage(recentMessage)
            .setServerMessage(serverMessage)
            .setTitle("ResolveConflict")
            .setOnClickBottomListener(new ConflictDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick(int type) {
                    if (type == ConflictDialog.TYPE_ARCHIVE_RECENT) {
                        callback.onResult(openArchive);
                        Toast.makeText(CommitArchiveActivity.this, "chose opened archive", Toast.LENGTH_SHORT).show();
                    } else if (type == ConflictDialog.TYPE_ARCHIVE_SERVER) {
                        callback.onResult(serverArchive);
                        Toast.makeText(CommitArchiveActivity.this, "chose server archive", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onNegativeClick() {
                    callback.onResult(null);
                    dialog.dismiss();
                }
            })
            .show();
    }

    private void handleConflict(OperationResult archiveDataOrConflict) {
        if (archiveDataOrConflict != null) {
            OperationResult.Difference archiveConflict = archiveDataOrConflict.getDifference();
            Archive openedArchive = archiveConflict.getRecentArchive();
            Archive serverArchive = archiveConflict.getServerArchive();
            showConflictDialog(openedArchive,
                serverArchive,
                new ArchiveMetadataDetailActivity.ConflictResolveCallback() {
                    @Override
                    public void onResult(Archive archive) {
                        if (archive == null) {
                            return;
                        }

                        Task<OperationResult> task =
                            getArchivesClient().updateArchive(archive);
                        task.addOnSuccessListener(new OnSuccessListener<OperationResult>() {
                            @Override
                            public void onSuccess(OperationResult archiveDataOrConflict) {
                                showLog("isDifference:"
                                    + ((archiveDataOrConflict == null) ? "" : archiveDataOrConflict.isDifference()));
                                if (archiveDataOrConflict != null && !archiveDataOrConflict.isDifference()) {
                                    Archive archive = archiveDataOrConflict.getArchive();
                                    if (archive != null && archive.getSummary() != null) {
                                        showLog("ArchiveId:" + archive.getSummary().getId());
                                        try {
                                            showLog("content:"
                                                + new String(archive.getDetails().get(), "UTF-8"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        showLog("Description:" + archive.getSummary().getDescInfo());
                                        showLog("UniqueName:" + archive.getSummary().getFileName());
                                        showLog("PlayedTime:" + archive.getSummary().getActiveTime());
                                        showLog("ProgressValue:" + archive.getSummary().getCurrentProgress());
                                        showLog(
                                            "ModifiedTimestamp:" + archive.getSummary().getRecentUpdateTime());
                                        showLog("CoverImageAspectRatio:"
                                            + archive.getSummary().getThumbnailRatio());
                                        showLog("hasThumbnail:" + archive.getSummary().hasThumbnail());

                                        showPlayerAndGame(archive.getSummary());
                                    }
                                } else {
                                    handleConflict(archiveDataOrConflict);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                ApiException apiException = (ApiException) e;
                                showLog("resolve result:" + apiException.getStatusCode());
                            }
                        });
                    }
                });
        }
    }

}
