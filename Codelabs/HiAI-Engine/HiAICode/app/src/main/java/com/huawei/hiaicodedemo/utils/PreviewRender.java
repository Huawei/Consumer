/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 *
 */
package com.huawei.hiaicodedemo.utils;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import com.huawei.hiaicodedemo.gles.EglCore;
import com.huawei.hiaicodedemo.gles.FullFrameRect;
import com.huawei.hiaicodedemo.gles.Texture2dProgram;
import com.huawei.hiaicodedemo.gles.WindowSurface;
import com.huawei.hiaicodedemo.opengl.EglProgram;
import com.huawei.hiaicodedemo.opengl.EglUtil;


public class PreviewRender {
    private static final String TAG = PreviewRender.class.getSimpleName();

    private static final int RETRY_TIMES = 2000;

    private HandlerThread mThread;

    private Handler mHandler;

    private EglCore mEglCore;

    private WindowSurface mDisplaySurface;

    private SurfaceTexture mCameraTexture = null;  // receives the output from the camera preview

    private FullFrameRect mFullFrameBlit;

    private final float[] mTmpMatrix = new float[16];

    private int mTextureId;

    private static final int MSG_EGL_INIT = 0;

    private static final int MSG_FRAME_AVAILABLE = 1;

    // segment frame available
    private static final int MSG_SG_FRAME_AVAILABLE = 2;

    private static final int MSG_EGL_DEINIT = 3;

    private static final int MSG_SET_ROTAION = 4;

    private static final int MSG_SET_BCAK_GROUND = 5;

    private int mBGPicTextureId = 0;

    // the segment EGL program
    private EglProgram mEglProgram;

    public PreviewRender(Surface var) {
        mThread = new HandlerThread("Render");
        mThread.start();
        mHandler = new Handler(mThread.getLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_EGL_INIT:
                        Surface surface = (Surface)msg.obj;
                        mEglCore = new EglCore(null, EglCore.FLAG_RECORDABLE);
                        mDisplaySurface = new WindowSurface(mEglCore, surface, false);
                        mDisplaySurface.makeCurrent();

                        mFullFrameBlit = new FullFrameRect(
                                new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT));
                        mTextureId = mFullFrameBlit.createTextureObject();
                        mCameraTexture = new SurfaceTexture(mTextureId);

                        mEglProgram = new EglProgram();
                        break;
                    case MSG_FRAME_AVAILABLE:
                        drawFrame();
                        break;
                    case MSG_SG_FRAME_AVAILABLE:
                        SegmentData data = (SegmentData)msg.obj;
                        drawSegmentFrame(data);
                        break;
                    case MSG_SET_ROTAION:
                        if (mEglProgram != null) {
                            mEglProgram.setBGRotation(msg.arg1);
                        }
                        break;
                    case MSG_EGL_DEINIT:
                        doRelease();
                        break;
                    case MSG_SET_BCAK_GROUND:
                        mBGPicTextureId = mEglProgram.loadBackGroundPicToTexture((Bitmap)msg.obj);
                        Log.i(TAG, "backGroundPicTextureId: " + mBGPicTextureId);
                        break;
                    default:
                        break;
                }
            }
        };
        mHandler.sendMessage(mHandler.obtainMessage(MSG_EGL_INIT, var));
    }

    public SurfaceTexture getEglSurfaceTexture() {
        int times = 0;
        while (mCameraTexture == null) {
            times++;
            if (times > RETRY_TIMES) {
                Log.e(TAG, "getEglSurfaceTexture fail");
                return null;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }
        return mCameraTexture;
    }

    public void setBackGroundPicture(Bitmap bmp) {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_BCAK_GROUND, bmp));
    }

    public void drawNormalFrame() {
        mHandler.sendEmptyMessage(MSG_FRAME_AVAILABLE);
    }

    public void drawSegmentFrame(byte[] srcYUV, byte[] result, int width, int height) {
        SegmentData data = new SegmentData(srcYUV, result, width, height);
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SG_FRAME_AVAILABLE, data));
    }

    private void drawFrame() {
        //Log.d(TAG, "drawFrame");
        if (mEglCore == null) {
            Log.d(TAG, "Skipping drawFrame after shutdown");
            return;
        }
        Log.i(TAG, "drawFrame");
        // Latch the next frame from the camera.
        mDisplaySurface.makeCurrent();
        mCameraTexture.updateTexImage();
        mCameraTexture.getTransformMatrix(mTmpMatrix);

        GLES20.glViewport(0, 0, mDisplaySurface.getWidth(), mDisplaySurface.getHeight());
        mFullFrameBlit.drawFrame(mTextureId, mTmpMatrix);
        mDisplaySurface.swapBuffers();
    }

    private void drawSegmentFrame(SegmentData data) {
        if (mEglCore == null) {
            Log.d(TAG, "Skipping drawFrame after shutdown");
            return;
        }
        Log.i(TAG, "drawSegmentFrame");
        // Latch the next frame from the camera.
        mDisplaySurface.makeCurrent();
        mCameraTexture.updateTexImage();
        mCameraTexture.getTransformMatrix(mTmpMatrix);

         GLES20.glViewport(0, 0, mDisplaySurface.getWidth(), mDisplaySurface.getHeight());
        int[] textureIds = mEglProgram.converYuvAndAlphaToTexture(data.src, data.result, data.width, data.height);
        mEglProgram.drawNv21AndBackGround(EglUtil.MATRIX_IDENTITY, mTmpMatrix, textureIds, mBGPicTextureId);
        mDisplaySurface.swapBuffers();
    }

    public void release() {
        mHandler.sendEmptyMessage(MSG_EGL_DEINIT);
    }

    private void doRelease() {
        if (mCameraTexture != null) {
            mCameraTexture.release();
            mCameraTexture = null;
        }
        if (mDisplaySurface != null) {
            mDisplaySurface.release();
            mDisplaySurface = null;
        }
        if (mFullFrameBlit != null) {
            mFullFrameBlit.release(false);
            mFullFrameBlit = null;
        }
        if (mEglCore != null) {
            mEglCore.release();
            mEglCore = null;
        }
        if (mEglProgram != null) {
            mEglProgram.release();
            mEglCore = null;
        }
    }

    public void setRotation(int rotation) {
        Message msg = new Message();
        msg.what = MSG_SET_ROTAION;
        msg.arg1 = rotation;
        mHandler.sendMessage(msg);
    }
}
