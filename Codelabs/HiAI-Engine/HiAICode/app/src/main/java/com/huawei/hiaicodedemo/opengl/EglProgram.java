/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 *
 */

package com.huawei.hiaicodedemo.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * The EGL program
 *
 * @author c00249868
 * @since 2019-06-05
 */
public class EglProgram {
    private static final String TAG = EglProgram.class.getSimpleName();

    private static final String SEPARATOR = System.lineSeparator();

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

    // shader for NV21 render directly
    private static final String YUV_VERTEX_SHADER =
            "uniform mat4 uMVPMatrix;" + SEPARATOR
            + "uniform mat4 uTexMatrix;" + SEPARATOR
            + "attribute vec4 aPosition;" + SEPARATOR
            + "attribute vec4 aTextureCoord;" + SEPARATOR
            + "varying vec2 vTextureCoord;" + SEPARATOR
            + "void main() {" + SEPARATOR
            + "    gl_Position = uMVPMatrix * aPosition;" + SEPARATOR
            + "    vTextureCoord = (uTexMatrix * aTextureCoord).xy;" + SEPARATOR
            + "}" + SEPARATOR;

    private static final String YUV_FRAME_SHADER = "precision mediump float;" + SEPARATOR
            + "uniform sampler2D samplerY;" + SEPARATOR
            + "uniform sampler2D samplerUV;" + SEPARATOR
            + "varying vec2 vTextureCoord;" + SEPARATOR
            + "void main() {" + SEPARATOR
            + "  float y = texture2D(samplerY, vTextureCoord).r;" + SEPARATOR
            + "  vec4 c = vec4(y);" + SEPARATOR
            + "  float U = texture2D(samplerUV, vTextureCoord).a - 128./255.;" + SEPARATOR
            + "  float V = texture2D(samplerUV, vTextureCoord).r - 128./255.;" + SEPARATOR
            + "  c.r = y+ 1.402 * V;" + SEPARATOR
            + "  c.g = y - 0.344 * U - 0.714 * V;" + SEPARATOR
            + "  c.b = y + 1.772 * U;" + SEPARATOR
            + "  c.a = 1.0;" + SEPARATOR
            + "  gl_FragColor = c;" + SEPARATOR + "}" + SEPARATOR;


    private static final String FRAG_NV21_BG_SHADER =
            "precision mediump float;" + SEPARATOR
                    + "varying vec2 vTextureCoord;" + SEPARATOR
                    + "uniform sampler2D uTextureY;" + SEPARATOR
                    + "uniform sampler2D uTextureUV;" + SEPARATOR
                    + "uniform sampler2D uTextureAlpha;" + SEPARATOR
                    + "uniform sampler2D uTextureBG;" + SEPARATOR
                    + "uniform int iRotation;" + SEPARATOR
                    + "void main() {" + SEPARATOR
                    + "    vec2 vTextureCoordBG;" + SEPARATOR
                    + "    if (iRotation == 270) {" + SEPARATOR
                    + "        vTextureCoordBG = vec2(1.0 - vTextureCoord.y, 1.0 - vTextureCoord.x);" + SEPARATOR
                    + "    } else {" + SEPARATOR
                    + "        vTextureCoordBG = vec2(1.0 - vTextureCoord.y, vTextureCoord.x);" + SEPARATOR
                    + "    }" + SEPARATOR
                    + "    float y = texture2D(uTextureY, vTextureCoord).r;"+ SEPARATOR
                    + "    vec4 yuv = vec4(y);" + SEPARATOR
                    + "    float U = texture2D(uTextureUV, vTextureCoord).a - 128./255.;" + SEPARATOR
                    + "    float V = texture2D(uTextureUV, vTextureCoord).r - 128./255.;" + SEPARATOR
                    + "    yuv.r = y + 1.402 * V;\n"
                    + "    yuv.g = y - 0.344 * U - 0.714 * V;\n"
                    + "    yuv.b = y + 1.772 * U;\n"
                    + "    vec4 backGround = texture2D(uTextureBG, vTextureCoordBG);" + SEPARATOR
                    + "    vec4 yuvAlpha = texture2D(uTextureAlpha, vTextureCoord);" + SEPARATOR
                    + "    gl_FragColor = vec4(backGround.r * (1.0 - yuvAlpha.r) + yuv.r * yuvAlpha.r," + SEPARATOR
                    + "                        backGround.g * (1.0 - yuvAlpha.r) + yuv.g * yuvAlpha.r," + SEPARATOR
                    + "                        backGround.b * (1.0 - yuvAlpha.r) + yuv.b * yuvAlpha.r," + SEPARATOR
                    + "                        1.0);" + SEPARATOR
                    + "}";

    // 1 2 , 3 4 is the bottom left and right, and 5 6, 7 8 is the top
    private static final float[] SQUARE_COORDS = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};

    private static final float[] TEXTURE_COORDS = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};

    private static final int COORDS_PER_VERTEX = 2; // for example: botton left hava 2 coords

    private static final int TEXTURE_NUM = 5;

    private static FloatBuffer mVertexBuf;

    private static FloatBuffer mTexureBuf;

    private int mStride = COORDS_PER_VERTEX * EglUtil.SIZEOF_FLOAT;

    private int mVertexCount = SQUARE_COORDS.length / COORDS_PER_VERTEX;

    private int mTextureTarget;

    // simple shader
    private ProgramHandle mProgram;

    // YUV shader for normal preview
    private ProgramHandle mYUVProgram;

    private ProgramHandle mYUVBGProgram;

    private byte[] uvPlane;

    private int[] textureIds;

    private int bgTextureId;

    private int mRotation = 270;

    /**
     * Constructor of EglProgram
     */
    public EglProgram() {
        mTextureTarget = GLES20.GL_TEXTURE_2D;
        mProgram = new ProgramHandle(SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER);
        mYUVProgram = new ProgramHandle(YUV_VERTEX_SHADER, YUV_FRAME_SHADER);
        mYUVBGProgram = new ProgramHandle(YUV_VERTEX_SHADER, FRAG_NV21_BG_SHADER);

        textureIds = EglUtil.createTextureObject(TEXTURE_NUM);
        initVertexAndShaderBuffer();
    }

    /**
     * Release the egl
     */
    public void release() {
        GLES20.glDeleteProgram(mProgram.mProgramId);
        mProgram.mProgramId = -1;
        GLES20.glDeleteProgram(mYUVProgram.mProgramId);
        mYUVProgram.mProgramId = -1;
        GLES20.glDeleteProgram(mYUVBGProgram.mProgramId);
        mYUVBGProgram.mProgramId = -1;
        GLES20.glDeleteTextures(TEXTURE_NUM, textureIds, 0);
        //GLES20.glDeleteTextures(TEXTURE_NUM, new int[] {bgTextureId}, 0);
    }

    /**
     * The program handle
     *
     * @since 2019-05-21
     */
    public class ProgramHandle {
        private int mProgramId;

        private int maPositionLocation;

        private int maTextureCoordLocation;

        private int muMVPMatrixLocation;

        private int muTexMatrixLocation;

        /**
         * The Constructor of ProgramHandle
         *
         * @param vertexSource The vertex sorce
         * @param fragmentSource The fragment source
         */
        public ProgramHandle(String vertexSource, String fragmentSource) {
            mProgramId = EglUtil.makeProgram(vertexSource, fragmentSource);
            getLocationAttr();
        }

        private void getLocationAttr() {
            // get locations of attributes and uniforms
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

    private void initVertexAndShaderBuffer() {
        if (mVertexBuf == null) {
            mVertexBuf = ByteBuffer.allocateDirect(SQUARE_COORDS.length * EglUtil.SIZEOF_FLOAT)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mVertexBuf.put(SQUARE_COORDS);
            mVertexBuf.position(0);
        }
        if (mTexureBuf == null) {
            mTexureBuf = ByteBuffer.allocateDirect(TEXTURE_COORDS.length * EglUtil.SIZEOF_FLOAT)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mTexureBuf.put(TEXTURE_COORDS);
            mTexureBuf.position(0);
        }
    }

    private void bindShaderLocationValues(ProgramHandle program, float[] mvpMatrix, float[] textureMatrix) {
        GLES20.glUniformMatrix4fv(program.muMVPMatrixLocation, 1, false, mvpMatrix, 0);
        EglUtil.checkEglError("glUniformMatrix4fv: muMVPMatrixLocation");

        GLES20.glUniformMatrix4fv(program.muTexMatrixLocation, 1, false, textureMatrix, 0);
        EglUtil.checkEglError("glUniformMatrix4fv: muTexMatrixLocation");

        GLES20.glEnableVertexAttribArray(program.maPositionLocation);
        EglUtil.checkEglError("glEnableVertexAttribArray");

        GLES20.glVertexAttribPointer(program.maPositionLocation,
                COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, mStride, mVertexBuf);
        EglUtil.checkEglError("glVertexAttribPointer");

        GLES20.glEnableVertexAttribArray(program.maTextureCoordLocation);
        EglUtil.checkEglError("glEnableVertexAttribArray");

        GLES20.glVertexAttribPointer(program.maTextureCoordLocation,
                COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, mStride, mTexureBuf);
        EglUtil.checkEglError("glVertexAttribPointer");
    }

    public int[] converYuvAndAlphaToTexture(byte[] yuv, byte[] alphaData, int width, int height) {

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        Buffer yPlane = ByteBuffer.wrap(yuv, 0, width * height);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0, GLES20.GL_LUMINANCE,
                GLES20.GL_UNSIGNED_BYTE, yPlane);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[1]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        if ((uvPlane == null) || (uvPlane.length != width * height / 2)) {
            uvPlane = new byte[width * height / 2]; // the uv in nv21 is 1/2 of width * height
        }

        System.arraycopy(yuv, width * height, uvPlane, 0, width * height / 2);

        Buffer vuPlane = ByteBuffer.wrap(uvPlane);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE_ALPHA, width / 2, height / 2, 0,
                GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, vuPlane);

        Buffer alpha = ByteBuffer.wrap(alphaData);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[2]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0,
                GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, alpha);

        return textureIds;
    }

    //the background picture size should be same to the camera stream size, or should has the same aspect ratio
    // if not, the background picture will be stretched
    public int loadBackGroundPicToTexture(Bitmap bmp) {
        int[] textureId = EglUtil.createTextureObject(1);
        bgTextureId = textureId[0];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(mTextureTarget, bgTextureId);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp, 0);
        return bgTextureId;
    }

    public void setBGRotation(int rotation) {
        mRotation = rotation;
    }

    public void drawNv21AndBackGround(float[] mvpMatrix, float[] textureMatrix, int[] yuvAndAlphaId, int backgroundTextureId) {
        EglUtil.checkEglError("draw start");

        // Select the program.
        GLES20.glUseProgram(mYUVBGProgram.mProgramId);
        EglUtil.checkEglError("mYUVProgramHandle");

        bindShaderLocationValues(mYUVBGProgram, mvpMatrix, textureMatrix);
        int y = GLES20.glGetUniformLocation(mYUVBGProgram.mProgramId, "uTextureY");
        int uv = GLES20.glGetUniformLocation(mYUVBGProgram.mProgramId, "uTextureUV");
        int alpha = GLES20.glGetUniformLocation(mYUVBGProgram.mProgramId, "uTextureAlpha");
        int backGround = GLES20.glGetUniformLocation(mYUVBGProgram.mProgramId, "uTextureBG");
        GLES20.glUniform1i(y, 0);


        // Set the texture.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(mTextureTarget, yuvAndAlphaId[0]);

        GLES20.glUniform1i(uv, 1);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(mTextureTarget, yuvAndAlphaId[1]);

        GLES20.glUniform1i(alpha, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(mTextureTarget, yuvAndAlphaId[2]);

        GLES20.glUniform1i(backGround, 3);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        GLES20.glBindTexture(mTextureTarget, backgroundTextureId);

        // set rotation
        int rotation = GLES20.glGetUniformLocation(mYUVBGProgram.mProgramId, "iRotation");
        GLES20.glUniform1i(rotation, mRotation);

        // Draw the rect.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexCount);

        EglUtil.checkEglError("glDrawArrays");

        GLES20.glDisableVertexAttribArray(mYUVBGProgram.maPositionLocation);
        GLES20.glDisableVertexAttribArray(mYUVBGProgram.maTextureCoordLocation);
        GLES20.glBindTexture(mTextureTarget, 0);
        GLES20.glUseProgram(0);
    }
}
