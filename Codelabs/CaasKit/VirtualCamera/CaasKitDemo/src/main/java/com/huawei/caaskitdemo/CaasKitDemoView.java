/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.caaskitdemo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.ViewGroup;

import com.huawei.caaskitdemo.caaskit.CaasKitHelper;
import com.huawei.caaskitdemo.opengl.EglProgram;
import com.huawei.caaskitdemo.opengl.EglUtil;

import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;

/**
 * Create an OpenGL environment. Complete interaction with mediaplayer.
 *
 * @since 2019-10-23
 */
public class CaasKitDemoView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private static final String TAG = "CaasKitDemoView";
    private static final int EGL_VERSION = 2;
    private MediaPlayer mMediaPlayer;
    private int mPlayingPosition;
    private File mVideoFilePath;
    private EglProgram mEglProgramOes;
    private EglProgram mEglProgram2D;
    private SurfaceTexture mSurfaceTexture;
    private int mOesTextureId;
    private float[] mModelMatrix = new float[EglUtil.MATRIX_SIZE];
    private int mOffscreenTextureId;
    private int mFrameBuffer;
    private Surface mOesSurface;

    public CaasKitDemoView(Context context) {
        super(context);
        Log.d(TAG, "CaasKitDemoView.");
        setEGLContextClientVersion(EGL_VERSION);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated.");
        destroyFrameBuffer();
        mOesTextureId = EglUtil.genOesTextureId();
        mSurfaceTexture = new SurfaceTexture(mOesTextureId);
        mOesSurface = new Surface(mSurfaceTexture);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        mEglProgramOes = new EglProgram(GL_TEXTURE_EXTERNAL_OES);
        mEglProgram2D = new EglProgram(GLES20.GL_TEXTURE_2D);
        Matrix.setIdentityM(mModelMatrix, 0);
        CaasKitHelper.getInstance().setSharedContext(EGL14.eglGetCurrentContext());
        if (mPlayingPosition > 0) {
            startPlaying(mPlayingPosition);
            mPlayingPosition = 0;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(TAG, "onSurfaceChanged.");
        destroyFrameBuffer();
        prepareFramebuffer(width, height);
        GLES20.glViewport(0, 0, width, height);
        Log.d(TAG, "width: " + width + "height: " + height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.updateTexImage();
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer);
        mEglProgramOes.draw(mModelMatrix, EglUtil.IDENTITY_MATRIX, mOesTextureId);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        if (CaasKitHelper.getInstance().isRendering()) {
            CaasKitHelper.getInstance().frameAvailable(mOffscreenTextureId);
        }

        mEglProgram2D.draw(EglUtil.IDENTITY_MATRIX, EglUtil.IDENTITY_MATRIX, mOffscreenTextureId);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        Log.e(TAG, "surfaceDestroyed.");
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mPlayingPosition = mMediaPlayer.getCurrentPosition();
            Log.e(TAG, "mPlayingPosition: " + mPlayingPosition);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause.");;
        super.onPause();
        if (mSurfaceTexture != null) {
            Log.d(TAG, "renderer pausing.");
            if (CaasKitHelper.getInstance().isRendering()) {
                CaasKitHelper.getInstance().stopRendering();
            }
            destroyFrameBuffer();
            mSurfaceTexture.release();
            mEglProgramOes.release();
            mEglProgram2D.release();
            mSurfaceTexture = null;
            mEglProgramOes = null;
            mEglProgram2D = null;
        }
    }

    public boolean startPlaying(File file) {
        if (file == null || !file.exists()
           || !file.canRead() || mOesSurface == null) {
            return false;
        }
        mVideoFilePath = file;
        startPlaying(0);
        return true;
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy.");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void startPlaying(int position) {
        if (mVideoFilePath == null) {
            return;
        }
        if (mMediaPlayer != null) {
            Log.e(TAG, "MediaPlayer release.");
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mVideoFilePath.toString());
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException.");
        } catch (IOException e) {
            Log.e(TAG, "IOException.");
        }
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d(TAG, "mMediaPlayer onCompletion.");
                mMediaPlayer.seekTo(0);
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setSurface(mOesSurface);
        mMediaPlayer.setOnPreparedListener(new PrepareListener(position));
    }

    private void adjustAspectRatio(int videoWidth, int videoHeight) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        double aspectRatio = (double) videoHeight / videoWidth;
        int newWidth;
        int newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);
        } else {
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        post(() -> {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = newHeight;
            layoutParams.width = newWidth;
            setLayoutParams(layoutParams);
        });
    }

    private void prepareFramebuffer(int width, int height) {
        int[] values = new int[1];
        GLES20.glGenTextures(1, values, 0);
        mOffscreenTextureId = values[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOffscreenTextureId);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glGenFramebuffers(1, values, 0);
        mFrameBuffer = values[0];
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mOffscreenTextureId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void destroyFrameBuffer() {
        GLES20.glDeleteTextures(1, new int[] {mOffscreenTextureId}, 0);
        GLES20.glDeleteFramebuffers(1, new int[] {mFrameBuffer}, 0);
    }

    private final class PrepareListener implements MediaPlayer.OnPreparedListener {
        private int mPosition;

        public PrepareListener(int position) {
            mPosition = position;
        }

        public void onPrepared(MediaPlayer mediaPlayer) {
            adjustAspectRatio(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
            if (mPosition > 0) {
                mMediaPlayer.seekTo(mPosition);
            }
            Log.d(TAG, "mediaPlayer start.");
            mMediaPlayer.start();
            CaasKitHelper.getInstance().sendShow();
        }
    }
}
