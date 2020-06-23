/*
 * Copyright 2020 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.scene.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.huawei.hms.scene.sdk.SceneView;

/**
 * SampleView
 *
 * @author HUAWEI
 * @since 2020-5-13
 */
public class SampleView extends SceneView {
    /**
     * Constructor - used in new mode.
     *
     * @param context Context of activity.
     */
    public SampleView(Context context) {
        super(context);
    }

    /**
     * Constructor - used in layout xml mode.
     *
     * @param context Context of activity.
     * @param attributeSet XML attribute set.
     */
    public SampleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * surfaceCreated
     * - You need to override this method, and call the APIs of SceneView to load and initialize materials.
     * - The super method contains the initialization logic.
     *   To override the surfaceCreated method, call the super method in the first line.
     *
     * @param holder SurfaceHolder.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);

        // Loads the model of a scene by reading files from assets.
        loadScene("scene.gltf");

        // Loads skybox materials by reading files from assets.
        loadSkyBox("skyboxTexture.dds");

        // Loads specular maps by reading files from assets.
        loadSpecularEnvTexture("specularEnvTexture.dds");

        // Loads diffuse maps by reading files from assets.
        loadDiffuseEnvTexture("diffuseEnvTexture.dds");
    }

    /**
     * surfaceChanged
     * - Generally, you do not need to override this method.
     * - The super method contains the initialization logic.
     *   To override the surfaceChanged method, call the super method in the first line.
     *
     * @param holder SurfaceHolder.
     * @param format Surface format.
     * @param width Surface width.
     * @param height Surface height.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
    }

    /**
     * surfaceDestroyed
     * - Generally, you do not need to override this method.
     * - The super method contains the initialization logic.
     *   To override the surfaceDestroyed method, call the super method in the first line.
     *
     * @param holder SurfaceHolder.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }

    /**
     * onTouchEvent
     * - Generally, override this method if you want to implement additional gesture processing logic.
     * - The super method contains the default gesture processing logic.
     *   If this logic is not required, the super method does not need to be called.
     *
     * @param motionEvent MotionEvent.
     * @return whether an event is processed.
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    /**
     * onDraw
     * - Generally, you do not need to override this method.
     *   If extra information (such as FPS) needs to be drawn on the screen, override this method.
     * - The super method contains the drawing logic.
     *   To override the onDraw method, call the super method in an appropriate position.
     *
     * @param canvas Canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

