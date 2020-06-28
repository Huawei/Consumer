/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package com.huawei.image.render.sample.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Function Description
 *
 * @since 2020-04-28
 */
public class Utils {

    /**
     * TAG
     */
    private static final String TAG = "Utils";

    /**
     * create demo dir
     *
     * @param dirPath dir path
     * @return result
     */
    public static boolean createResourceDirs(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (dir.getParentFile().mkdir()) {
                return dir.mkdir();
            } else {
                return dir.mkdir();
            }
        }
        return false;
    }

    /**
     * copy assets folders to sdCard
     * @param context context
     * @param foldersName folderName
     * @param path path
     * @return result
     */
    public static boolean copyAssetsFilesToDirs(Context context, String foldersName, String path){
        try {
            String[] files = context.getAssets().list(foldersName);
            for (String file :
                    files) {
                if (!copyAssetsFileToDirs(context, foldersName + File.separator + file, path + File.separator + file)) {
                    Log.e(TAG, "Copy resource file fail, please check permission");
                    return false;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * copy resource file to sdCard
     *
     * @param context  context
     * @param fileName fileName
     * @param path     sdCard path
     * @return result
     */
    public static boolean copyAssetsFileToDirs(Context context, String fileName, String path) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
            File file = new File(path);
            outputStream = new FileOutputStream(file);
            byte[] temp = new byte[4096];
            int n;
            while (-1 != (n = inputStream.read(temp))) {
                outputStream.write(temp, 0, n);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }
        return true;
    }
}
