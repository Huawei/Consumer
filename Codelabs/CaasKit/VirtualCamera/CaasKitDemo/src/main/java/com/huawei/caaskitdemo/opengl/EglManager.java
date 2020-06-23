/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.caaskitdemo.opengl;

import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.util.Log;
import android.view.Surface;

/**
 * Control the open gl to process the video data.
 *
 * @since 2019-10-20
 */
public class EglManager {
    private static final String TAG = "EglManager";
    private EglCore mEglCore;
    private EglProgram mEglProgram2D;

    /**
     * Create EGL environment and Associates an EGL surface with the native window surface.
     *
     * @param sharedContext The context to share, or null if sharing is not desired.
     * @param surface the Surface we want to render.
     */
    public EglManager(EGLContext sharedContext, Surface surface) {
        mEglCore = new EglCore(sharedContext);
        if (surface != null) {
            mEglCore.createWindowSurface(surface);
            mEglCore.makeCurrent();
            mEglProgram2D = new EglProgram(GLES20.GL_TEXTURE_2D);
        }
    }

    /**
     * Releases any resources associated with the EGL surface.
     */
    public void release() {
        if (mEglCore != null) {
            mEglCore.release();
        }
        if (mEglProgram2D != null) {
            mEglProgram2D.release();
        }
    }

    /**
     * draw only use textureId
     *
     * @param mvpMatrix The mvp matrix for rotation and cut off
     * @param texMatrix The texture matrix
     * @param textureId the texture id
     */
    public void draw(float[] mvpMatrix,
                     float[] texMatrix, int textureId) {
        if (mEglProgram2D != null) {
            mEglProgram2D.draw(mvpMatrix, texMatrix, textureId);
        }
    }

    /**
     * call eglSwapBuffers.
     */
    public void swapBuffers() {
        if (mEglCore != null) {
            boolean isSuccess = mEglCore.swapBuffers();
            if (!isSuccess) {
                Log.e(TAG, "swapBuffers() Error.");
            }
        }
    }
}
