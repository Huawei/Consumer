/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 *
 */

package com.huawei.hiaicodedemo.opengl;

import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * EglUtil
 *
 * @author c00249868
 * @since 2019-06-05
 */
public class EglUtil {
    /**
     * The size of egl float
     */
    public static final int SIZEOF_FLOAT = 4;

    /**
     * The identity of matrix
     */
    public static final float[] MATRIX_IDENTITY;

    private static final String TAG = EglUtil.class.getSimpleName();

    private static final int MATRIX_SIZE = 16;

    static {
        MATRIX_IDENTITY = new float[MATRIX_SIZE];
        Matrix.setIdentityM(MATRIX_IDENTITY, 0);
    }

    private EglUtil() {
    }

    /**
     * Check location error
     *
     * @param location The location
     * @param label The label
     */
    public static void checkLocationError(int location, String label) {
        if (location < 0) {
            Log.e(TAG, "location error: " + label);
        }
    }

    /**
     * create the texture object
     *
     * @param num the number of texture object number
     * @return The textures array
     */
    public static int[] createTextureObject(int num) {
        int[] textures = new int[num];
        GLES20.glGenTextures(num, textures, 0);
        checkEglError("glGenTextures");

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        checkEglError("glTexParameter");

        return textures;
    }

    /**
     * Check the egl error
     *
     * @param msg The message
     */
    public static void checkEglError(String msg) {
        int error;
        if ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
            Log.e(TAG, "checkEglError error " + error);
        }
    }

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
        checkEglError("glAttachShader");
        GLES20.glAttachShader(programId, pixelProgramShader);
        checkEglError("glAttachShader");
        GLES20.glLinkProgram(programId);
        int[] linkProgramStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkProgramStatus, 0);
        if (linkProgramStatus[0] != GLES20.GL_TRUE) {
            Log.e(TAG, "Could not link program: ");
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
        checkEglError("glCreateShader type=" + shaderType);
        GLES20.glShaderSource(shaderId, shaderSource);
        GLES20.glCompileShader(shaderId);
        int[] compiledShaders = new int[1];
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compiledShaders, 0);
        if (compiledShaders[0] == 0) {
            Log.e(TAG, "Could not compile shaderId " + shaderType + ":");
            Log.e(TAG, " " + GLES20.glGetShaderInfoLog(shaderId));
            GLES20.glDeleteShader(shaderId);
            shaderId = 0;
        }
        return shaderId;
    }
}
