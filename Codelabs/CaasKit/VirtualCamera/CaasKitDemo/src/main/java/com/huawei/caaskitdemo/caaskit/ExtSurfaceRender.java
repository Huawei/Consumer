/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 *
 */

package com.huawei.caaskitdemo.caaskit;

import android.graphics.Point;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import com.huawei.caaskitdemo.opengl.EglManager;
import com.huawei.caaskitdemo.opengl.EglUtil;

import java.lang.ref.WeakReference;

/**
 * Create an thread to render the specified surface.
 *
 * @since 2019-10-21
 */
public class ExtSurfaceRender implements Runnable {
    private static final String TAG = "ExtSurfaceRender";
    private static final int MSG_START_RENDERING = 0;
    private static final int MSG_STOP_RENDERING = 1;
    private static final int MSG_FRAME_AVAILABLE = 2;
    private static final int MSG_QUIT_RENDERING = 3;
    private static final int VIDEO_WEIGHT = 1920;
    private static final int VIDEO_HEIGHT = 1080;
    private static ExtSurfaceRender sExtSurfaceRender;

    private Object mObject = new Object();
    private EglManager mEglManager;
    private Surface mExtSurface;
    private boolean mIsContextReady;
    private EGLContext mSharedContext;
    private volatile RenderHandler mHandler;
    private boolean mIsThreadReady;
    private boolean mIsRunning;
    private Point VideoResolution;

    private ExtSurfaceRender() {
    }

    public static ExtSurfaceRender getInstance() {
        if (sExtSurfaceRender == null) {
            synchronized (ExtSurfaceRender.class) {
                if (sExtSurfaceRender == null) {
                    sExtSurfaceRender = new ExtSurfaceRender();
                }
            }
        }
        return sExtSurfaceRender;
    }

    /**
     * Handles an available frame.
     *
     * @param textureId textureId available for rendering.
     */
    public void frameAvailable(int textureId) {
        synchronized (mObject) {
            if (!mIsThreadReady) {
                return;
            }
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG_FRAME_AVAILABLE, textureId, 0, null));
    }

    /**
     * set the shared EGLContext.
     *
     * @param sharedContext The context to share.
     */
    public void setSharedContext(EGLContext sharedContext) {
        Log.d(TAG, "setSharedContext: " + sharedContext);
        mSharedContext = sharedContext;
        mIsContextReady = true;
    }

    /**
     * get the shared EGLContext.
     *
     * @return sharedContext The context to share.
     */
    public EGLContext getSharedContext() {
        return mSharedContext;
    }

    /**
     * set the video resolution.
     *
     * @param width video width.
     * @param height video height.
     */
    public void setVideoResolution(int width, int height) {
        Log.d(TAG, "width: " + width + "height: " + height);
        VideoResolution = new Point(width, height);
    }

    /**
     * get the video resolution.
     *
     * @return video resolution.
     */
    public Point getVideoResolution() {
        if (VideoResolution == null) {
            return new Point(VIDEO_WEIGHT, VIDEO_HEIGHT);
        }
        return VideoResolution;
    }

    /**
     * Judge if EGLContext is ready.
     *
     * @return mIsContextReady.
     */
    public boolean isEGLContextReady() {
        return mIsContextReady;
    }

    /**
     * Tells the surface render to start rendering.
     *
     * @param surface the Surface we want to render.
     */
    public void startRendering(Surface surface) {
        Log.d(TAG, "startRendering.");
        synchronized (mObject) {
            if (mIsRunning) {
                Log.w(TAG, "Render thread already running.");
                return;
            }
            if (!mIsContextReady) {
                Log.w(TAG, "EGLContext is not ready.");
                return;
            }
            mIsRunning = true;
            mExtSurface = surface;
            new Thread(this, "ExtSurfaceRender").start();
            while (!mIsThreadReady) {
                try {
                    mObject.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException.");
                }
            }
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG_START_RENDERING));
    }

    /**
     * Tells the surface render to stop rendering.
     */
    public void stopRendering() {
        Log.d(TAG, "stopRendering.");
        if (isRendering()) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_STOP_RENDERING));
            mHandler.sendMessage(mHandler.obtainMessage(MSG_QUIT_RENDERING));
        }
    }

    /**
     * Identifies whether the rendering thread is working.
     *
     * @return Returns true if recording has been started.
     */
    public boolean isRendering() {
        synchronized (mObject) {
            return mIsRunning;
        }
    }

    /**
     * Render thread entry point.
     * Establishes Looper/Handler and waits for messages.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        Looper.prepare();
        synchronized (mObject) {
            mHandler = new RenderHandler(this);
            mIsThreadReady = true;
            mObject.notify();
        }
        Looper.loop();
        Log.d(TAG, "Render thread exiting.");
        synchronized (mObject) {
            mIsThreadReady = false;
            mIsRunning = false;
            mHandler = null;
        }
    }

    /**
     * Handles notification of an available frame.
     *
     * @param textureId textureId available for rendering.
     */
    private void handleFrameAvailable(int textureId) {
        if (mEglManager != null) {
            mEglManager.draw(EglUtil.IDENTITY_MATRIX, EglUtil.IDENTITY_MATRIX, textureId);
            mEglManager.swapBuffers();
        }
    }

    /**
     * Handles render state change requests. The handler is created on the render thread.
     *
     * @since 2019-10-21
     */
    private static class RenderHandler extends Handler {
        private WeakReference<ExtSurfaceRender> mWeakRender;

        public RenderHandler(ExtSurfaceRender render) {
            mWeakRender = new WeakReference<ExtSurfaceRender>(render);
        }

        @Override
        public void handleMessage(Message inputMessage) {
            int what = inputMessage.what;
            ExtSurfaceRender render = mWeakRender.get();
            if (render == null) {
                Log.e(TAG, "render is null.");
                return;
            }
            switch (what) {
                case MSG_START_RENDERING:
                    render.handleStartRendering();
                    break;
                case MSG_STOP_RENDERING:
                    render.handleStopRendering();
                    break;
                case MSG_FRAME_AVAILABLE:
                    render.handleFrameAvailable(inputMessage.arg1);
                    break;
                case MSG_QUIT_RENDERING:
                    Looper.myLooper().quit();
                    break;
                default:
                    throw new RuntimeException("invalid value: " + what);
            }
        }
    }

    /**
     * Start rendering.
     *
     */
    private void handleStartRendering() {
        Log.d(TAG, "StartRendering.");
        mEglManager = new EglManager(mSharedContext, mExtSurface);
    }

    /**
     * Stop rendering.
     */
    private void handleStopRendering() {
        Log.d(TAG, "handleStopRendering.");
        if (mEglManager != null) {
            mEglManager.release();
            mEglManager = null;
        }
        mExtSurface = null;
    }
}
