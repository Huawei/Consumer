/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.caaskitdemo.opengl;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;

/**
 * GL program and supporting functions for textured 2D and EXTERNAL_OES shapes.
 *
 * @since 2019-10-21
 */
public class EglProgram {
    private static final String TAG = "EglProgram";

    private static final String SEPARATOR = System.lineSeparator();

    private final float[] SQUARE_COORDS = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f
    };

    private final float[] TEXTURE_COORDS_OES = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    private final float[] TEXTURE_COORDS_2D = {
            0.0f, 1 - 0.0f,
            0.0f, 1 - 1.0f,
            1.0f, 1 - 0.0f,
            1.0f, 1 - 1.0f,
    };

    // for example: botton left hava 2 coords
    private static final int COORDS_PER_VERTEX = 2;

    private static final int VERTEX_SIZE = 2;

    private static final String SIMPLE_VERTEX_SHADER =
            "uniform mat4 uMVPMatrix;" + SEPARATOR
                    + "uniform mat4 uTexMatrix;" + SEPARATOR
                    + "attribute vec4 aPosition;" + SEPARATOR
                    + "attribute vec4 aTextureCoord;" + SEPARATOR
                    + "varying vec2 vTextureCoord;" + SEPARATOR
                    + "void main() {" + SEPARATOR
                    + "    gl_Position = uMVPMatrix * aPosition;" + SEPARATOR
                    + "    vTextureCoord = (uTexMatrix * aTextureCoord).xy;" + SEPARATOR
                    + "}" + SEPARATOR;

    private static final String SIMPLE_FRAGMENT_SHADER =
            "precision mediump float;" + SEPARATOR
                    + "varying vec2 vTextureCoord;" + SEPARATOR
                    + "uniform sampler2D sTexture;" + SEPARATOR
                    + "void main() {" + SEPARATOR
                    + "    gl_FragColor = texture2D(sTexture, vTextureCoord);" + SEPARATOR
                    + "}" + SEPARATOR;

    private static final String OES_FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require" + SEPARATOR
                    + "precision mediump float;" + SEPARATOR
                    + "varying vec2 vTextureCoord;" + SEPARATOR
                    + "uniform samplerExternalOES sTexture;" + SEPARATOR
                    + "void main() {" + SEPARATOR
                    + "  gl_FragColor = texture2D(sTexture, vTextureCoord);" + SEPARATOR
                    + "}" + SEPARATOR;

    private int mStride = COORDS_PER_VERTEX * EglUtil.SIZEOF_FLOAT;

    private int mVertexCount = SQUARE_COORDS.length / COORDS_PER_VERTEX;

    private FloatBuffer mVertexBuf;

    private FloatBuffer mTexureBuf;

    private int mProgramId;

    private int muMVPMatrixLocation;

    private int muTexMatrixLocation;

    private int maPositionLocation;

    private int maTextureCoordLocation;

    private int mTextureTarget;

    /**
     * Prepares the program in the current EGL context.
     *
     * @param textureTarget GL_TEXTURE_2D or GL_TEXTURE_EXTERNAL_OES
     */
    public EglProgram(int textureTarget) {
        Log.d(TAG, "EglProgram.");
        mTextureTarget = textureTarget;
        initVertexAndShaderBuffer();
        if (mTextureTarget == GLES20.GL_TEXTURE_2D) {
            mProgramId = EglUtil.makeProgram(SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER);
        } else if (mTextureTarget == GL_TEXTURE_EXTERNAL_OES) {
            mProgramId = EglUtil.makeProgram(SIMPLE_VERTEX_SHADER, OES_FRAGMENT_SHADER);
        } else {
            throw new RuntimeException("textureTarget is illegal.");
        }
        if (mProgramId == 0) {
            throw new RuntimeException("Unable to create program.");
        }
        getLocationAttr();
    }

    /**
     * Releases the program.
     * <p>
     * The appropriate EGL context must be current (i.e. the one that was used to create
     * the program).
     */
    public void release() {
        Log.d(TAG, "deleting program: " + mProgramId);
        GLES20.glDeleteProgram(mProgramId);
        mProgramId = -1;
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
        // clear
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Select the program.
        GLES20.glUseProgram(mProgramId);
        EglUtil.checkEglError("glUseProgram");

        // Set the texture.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(mTextureTarget, textureId);
        EglUtil.checkEglError("glBindTexture");

        // Copy the model / view / projection matrix over.
        GLES20.glUniformMatrix4fv(muMVPMatrixLocation, 1, false, mvpMatrix, 0);
        EglUtil.checkEglError("muMVPMatrixLocation");

        // Copy the texture transformation matrix over.
        GLES20.glUniformMatrix4fv(muTexMatrixLocation, 1, false, texMatrix, 0);
        EglUtil.checkEglError("muTexMatrixLocation");

        // Enable the "aPosition" vertex attribute.
        GLES20.glEnableVertexAttribArray(maPositionLocation);
        EglUtil.checkEglError("glEnableVertexAttribArray");

        // Connect mVertexBuf to "aPosition".
        GLES20.glVertexAttribPointer(maPositionLocation, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, mStride, mVertexBuf);
        EglUtil.checkEglError("glVertexAttribPointer");

        // Enable the "aTextureCoord" vertex attribute.
        GLES20.glEnableVertexAttribArray(maTextureCoordLocation);
        EglUtil.checkEglError("glEnableVertexAttribArray");

        // Connect mTexureBuf to "aTextureCoord".
        GLES20.glVertexAttribPointer(maTextureCoordLocation, VERTEX_SIZE,
                GLES20.GL_FLOAT, false, mStride, mTexureBuf);
        EglUtil.checkEglError("glVertexAttribPointer");

        // Draw the rect.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexCount);
        EglUtil.checkEglError("glDrawArrays");

        // Done -- disable vertex array, texture, and program.
        GLES20.glDisableVertexAttribArray(maPositionLocation);
        GLES20.glDisableVertexAttribArray(maTextureCoordLocation);
        GLES20.glBindTexture(mTextureTarget, 0);
        GLES20.glUseProgram(0);
    }

    private void initVertexAndShaderBuffer() {
        if (mVertexBuf == null) {
            mVertexBuf = EglUtil.createFloatBuffer(SQUARE_COORDS);
        }
        if (mTexureBuf == null) {
            if (mTextureTarget == GLES20.GL_TEXTURE_2D) {
                mTexureBuf = EglUtil.createFloatBuffer(TEXTURE_COORDS_2D);
            } else {
                mTexureBuf = EglUtil.createFloatBuffer(TEXTURE_COORDS_OES);
            }
        }
    }

    private void getLocationAttr() {
        maPositionLocation = GLES20.glGetAttribLocation(mProgramId, "aPosition");
        EglUtil.checkLocationError(maPositionLocation, "aPosition");
        muMVPMatrixLocation = GLES20.glGetUniformLocation(mProgramId, "uMVPMatrix");
        EglUtil.checkLocationError(muMVPMatrixLocation, "uMVPMatrix");
        maTextureCoordLocation = GLES20.glGetAttribLocation(mProgramId, "aTextureCoord");
        EglUtil.checkLocationError(maTextureCoordLocation, "aTextureCoord");
        muTexMatrixLocation = GLES20.glGetUniformLocation(mProgramId, "uTexMatrix");
        EglUtil.checkLocationError(muTexMatrixLocation, "uTexMatrix");
    }
}
