/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.caaskitdemo.opengl;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.util.Log;
import android.view.Surface;

import static android.opengl.EGLExt.EGL_RECORDABLE_ANDROID;

/**
 * Prepares EGL display and context.
 *
 * @since 2019-10-21
 */
public final class EglCore {
    private static final String TAG = "EglCore";
    private static final int VERSION_SIZE = 2;
    private static final int EGL_SIZE = 8;
    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    private EGLConfig mEGLConfig = null;
    private EGLSurface mEglSurface;

    /**
     * Prepares EGL display and context.
     *
     * @param sharedContext The context to share, or null if sharing is not desired.
     */
    public EglCore(EGLContext sharedContext) {
        if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("EGL already set up.");
        }
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display.");
        }
        int[] version = new int[VERSION_SIZE];
        if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
            mEGLDisplay = EGL14.EGL_NO_DISPLAY;
            throw new RuntimeException("eglInitialize failed.");
        }
        if (sharedContext == null) {
            sharedContext = EGL14.EGL_NO_CONTEXT;
        }
        if (mEGLContext == EGL14.EGL_NO_CONTEXT) {
            int[] attributes = {
                    EGL14.EGL_RED_SIZE, EGL_SIZE,
                    EGL14.EGL_GREEN_SIZE, EGL_SIZE,
                    EGL14.EGL_BLUE_SIZE, EGL_SIZE,
                    EGL14.EGL_ALPHA_SIZE, EGL_SIZE,
                    EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                    EGL_RECORDABLE_ANDROID, 1,
                    EGL14.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] numConfigs = new int[1];
            if (!EGL14.eglChooseConfig(mEGLDisplay, attributes, 0, configs, 0, configs.length,
                    numConfigs, 0)) {
                throw new RuntimeException("eglChooseConfig fail.");
            }
            int[] attribList = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, VERSION_SIZE,
                    EGL14.EGL_NONE
            };
            EGLContext context = EGL14.eglCreateContext(mEGLDisplay, configs[0], sharedContext,
                    attribList, 0);
            EglUtil.checkEglError("eglCreateContext");
            mEGLConfig = configs[0];
            mEGLContext = context;
        }
    }

    /**
     * Discards all resources held by this class, notably the EGL context.  This must be
     * called from the thread where the context was created.
     * <p>
     * On completion, no context will be current.
     */
    public void release() {
        EGL14.eglDestroySurface(mEGLDisplay, mEglSurface);
        EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(mEGLDisplay);
        mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        mEGLContext = EGL14.EGL_NO_CONTEXT;
        mEglSurface = EGL14.EGL_NO_SURFACE;
        mEGLConfig = null;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
                Log.e(TAG, "EglCore was not explicitly released.");
                release();
            }
        } finally {
            super.finalize();
        }
    }

    /**
     * Create a window surface, and attach it to the Surface we received.
     *
     * @param surface the Surface we want to render.
     */
    public void createWindowSurface(Object surface) {
        if (!(surface instanceof Surface) && !(surface instanceof SurfaceTexture)) {
            throw new RuntimeException("invalid surface: " + surface);
        }
        int[] surfaceAttribs = {
                EGL14.EGL_NONE
        };
        EGLSurface eglSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, mEGLConfig, surface,
                surfaceAttribs, 0);
        EglUtil.checkEglError("eglCreateWindowSurface");
        mEglSurface = eglSurface;
    }

    /**
     * Makes our EGL context current, using the supplied surface for both "draw" and "read".
     */
    public void makeCurrent() {
        if (mEglSurface == null) {
            Log.e(TAG, "makeCurrent eglSurface is null.");
            return;
        }
        if (!EGL14.eglMakeCurrent(mEGLDisplay, mEglSurface, mEglSurface, mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    /**
     * Calls eglSwapBuffers. Use this to "publish" the current frame.
     *
     * @return false on failure
     */
    public boolean swapBuffers() {
        return EGL14.eglSwapBuffers(mEGLDisplay, mEglSurface);
    }
}
