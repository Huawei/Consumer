/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.caaskitdemo.opengl;

import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;

/**
 * Some OpenGL utility functions.
 *
 * @since 2019-10-21
 */
public class EglUtil {
    public static final String TAG = "EglUtil";

    /**
     * The size of egl float
     */
    public static final int SIZEOF_FLOAT = 4;

    /**
     * The identity of matrix
     */
    public static final float[] IDENTITY_MATRIX;

    /**
     * The matrix size
     */
    public static final int MATRIX_SIZE = 16;

    static {
        IDENTITY_MATRIX = new float[MATRIX_SIZE];
        Matrix.setIdentityM(IDENTITY_MATRIX, 0);
    }

    private EglUtil() {}

    /**
     * Use the supplied vertex and fragment shaders to creates a new program.
     *
     * @param vertexProgramSource the vertex source
     * @param fragmentProgramSource the fragment source
     * @return The handle of the program, or 0 on failure.
     */
    public static int makeProgram(String vertexProgramSource, String fragmentProgramSource) {
        int vertexProgramShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexProgramSource);
        if (vertexProgramShader == 0) {
            return 0;
        }
        int pixelProgramShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentProgramSource);
        if (pixelProgramShader == 0) {
            return 0;
        }

        int programId = GLES20.glCreateProgram();
        checkEglError("glCreateProgram");
        if (programId == 0) {
            Log.e(TAG, "Could not make program");
        }
        GLES20.glAttachShader(programId, vertexProgramShader);
        checkEglError("vertexProgramShader");
        GLES20.glAttachShader(programId, pixelProgramShader);
        checkEglError("pixelProgramShader");
        GLES20.glLinkProgram(programId);
        int[] linkProgramStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkProgramStatus, 0);
        if (linkProgramStatus[0] != GLES20.GL_TRUE) {
            Log.e(TAG, "Could not link program.");
            Log.e(TAG, GLES20.glGetProgramInfoLog(programId));
            GLES20.glDeleteProgram(programId);
            programId = 0;
        }
        return programId;
    }

    /**
     * Load and compile the provided shader shaderSource.
     *
     * @param shaderType the shader type
     * @param shaderSource The shaderSource
     * @return The handle of the shader, or 0 on failure.
     */
    public static int loadShader(int shaderType, String shaderSource) {
        int shaderId = GLES20.glCreateShader(shaderType);
        checkEglError("glCreateShader type: " + shaderType);
        GLES20.glShaderSource(shaderId, shaderSource);
        GLES20.glCompileShader(shaderId);
        int[] compiledShaders = new int[1];
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compiledShaders, 0);
        if (compiledShaders[0] == 0) {
            Log.e(TAG, "Could not compile shaderId: " + shaderType + " : ");
            Log.e(TAG, " " + GLES20.glGetShaderInfoLog(shaderId));
            GLES20.glDeleteShader(shaderId);
            shaderId = 0;
        }
        return shaderId;
    }

    /**
     * Check the egl error.
     *
     * @param msg msg on error.
     */
    public static void checkEglError(String msg) {
        int error;
        if ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
            throw new RuntimeException(msg + "-EGL error: 0x" + Integer.toHexString(error));
        }
    }

    /**
     * Check location error.
     *
     * @param location The location
     * @param label The label
     */
    public static void checkLocationError(int location, String label) {
        if (location < 0) {
            throw new RuntimeException("location error: " + label);
        }
    }

    /**
     * Allocates a direct float buffer, and populates it with the float array data.
     *
     * @param coords Coordinate array.
     */
    public static FloatBuffer createFloatBuffer(float[] coords) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length * SIZEOF_FLOAT);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(coords);
        floatBuffer.position(0);
        return floatBuffer;
    }

    /**
     * Rotate matrix by angle.
     *
     * @param matrix Matrix before rotation.
     * @param angle Rotation angle.
     * @return Matrix after rotation.
     */
    public static float[] rotateMatrix(float[] matrix, float angle){
        Matrix.rotateM(matrix,0, angle,0,0,1);
        return matrix;
    }

    /**
     * Generate textureId and bind GL_TEXTURE_EXTERNAL_OES.
     *
     * @return textureId.
     */
    public static int genOesTextureId() {
        int[] textureObjectId = new int[1];
        GLES20.glGenTextures(1, textureObjectId, 0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureObjectId[0]);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return textureObjectId[0];
    }
}
