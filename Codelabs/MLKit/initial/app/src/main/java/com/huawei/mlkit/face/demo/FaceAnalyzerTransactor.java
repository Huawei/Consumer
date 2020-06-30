package com.huawei.mlkit.face.demo;
/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import android.util.SparseArray;

import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.mlkit.face.demo.camera.GraphicOverlay;
import com.huawei.mlkit.face.demo.ui.MLFaceGraphic;

public class FaceAnalyzerTransactor implements MLAnalyzer.MLTransactor<MLFace> {
    private GraphicOverlay mGraphicOverlay;

    FaceAnalyzerTransactor(GraphicOverlay ocrGraphicOverlay) {
        this.mGraphicOverlay = ocrGraphicOverlay;
    }

    @Override
    public void transactResult(MLAnalyzer.Result<MLFace> result) {
        this.mGraphicOverlay.clear();
        SparseArray<MLFace> faceSparseArray = result.getAnalyseList();
        for (int i = 0; i < faceSparseArray.size(); i++) {
            // todo step 4: add on-device face graphic

            // finish
        }
    }

    @Override
    public void destroy() {
        this.mGraphicOverlay.clear();
    }

}