package com.huawei.mlkit.face.demo.ui;
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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.huawei.hms.mlsdk.common.MLPosition;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.hms.mlsdk.face.MLFaceKeyPoint;
import com.huawei.hms.mlsdk.face.MLFaceShape;
import com.huawei.mlkit.face.demo.camera.GraphicOverlay;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MLFaceGraphic extends GraphicOverlay.Graphic {
    private static final float BOX_STROKE_WIDTH = 8.0f;
    private static final float LINE_WIDTH = 5.0f;

    private final GraphicOverlay overlay;

    private final Paint facePositionPaint;
    private final Paint landmarkPaint;
    private final Paint boxPaint;

    private final Paint facePaint;
    private final Paint eyePaint;
    private final Paint eyebrowPaint;
    private final Paint lipPaint;
    private final Paint nosePaint;
    private final Paint noseBasePaint;
    private final Paint textPaint;
    private final Paint probilityPaint;

    private volatile MLFace mFace;

    public MLFaceGraphic(GraphicOverlay overlay, MLFace face) {
        super(overlay);

        mFace = face;
        this.overlay = overlay;
        final int selectedColor = Color.WHITE;

        facePositionPaint = new Paint();
        facePositionPaint.setColor(selectedColor);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);
        textPaint.setTypeface(Typeface.DEFAULT);

        probilityPaint = new Paint();
        probilityPaint.setColor(Color.WHITE);
        probilityPaint.setTextSize(35);
        probilityPaint.setTypeface(Typeface.DEFAULT);

        landmarkPaint = new Paint();
        landmarkPaint.setColor(Color.RED);
        landmarkPaint.setStyle(Paint.Style.FILL);
        landmarkPaint.setStrokeWidth(10f);

        boxPaint = new Paint();
        boxPaint.setColor(Color.WHITE);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(BOX_STROKE_WIDTH);

        facePaint = new Paint();
        facePaint.setColor(Color.parseColor("#ffcc66"));
        facePaint.setStyle(Paint.Style.STROKE);
        facePaint.setStrokeWidth(LINE_WIDTH);

        eyePaint = new Paint();
        eyePaint.setColor(Color.parseColor("#00ccff"));
        eyePaint.setStyle(Paint.Style.STROKE);
        eyePaint.setStrokeWidth(LINE_WIDTH);

        eyebrowPaint = new Paint();
        eyebrowPaint.setColor(Color.parseColor("#006666"));
        eyebrowPaint.setStyle(Paint.Style.STROKE);
        eyebrowPaint.setStrokeWidth(LINE_WIDTH);

        nosePaint = new Paint();
        nosePaint.setColor(Color.parseColor("#ffff00"));
        nosePaint.setStyle(Paint.Style.STROKE);
        nosePaint.setStrokeWidth(LINE_WIDTH);

        noseBasePaint = new Paint();
        noseBasePaint.setColor(Color.parseColor("#ff6699"));
        noseBasePaint.setStyle(Paint.Style.STROKE);
        noseBasePaint.setStrokeWidth(LINE_WIDTH);

        lipPaint = new Paint();
        lipPaint.setColor(Color.parseColor("#990000"));
        lipPaint.setStyle(Paint.Style.STROKE);
        lipPaint.setStrokeWidth(LINE_WIDTH);
    }

    public List<String> sortHashMap(HashMap<String, Float> map) {

        Set<Map.Entry<String, Float>> entey = map.entrySet();
        List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String, Float>>(entey);
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                if (o2.getValue() - o1.getValue() >= 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        List<String> emotions = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            emotions.add(list.get(i).getKey());
        }
        return emotions;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mFace == null) {
            return;
        }
        float start = 350f;
        float x = start;
        float width = 500f;
        float y = overlay.getHeight() - 300.0f;
        HashMap<String, Float> emotions = new HashMap<>();
        emotions.put("Smiling", mFace.getEmotions().getSmilingProbability());
        emotions.put("Neutral", mFace.getEmotions().getNeutralProbability());
        emotions.put("Angry", mFace.getEmotions().getAngryProbability());
        emotions.put("Fear", mFace.getEmotions().getFearProbability());
        emotions.put("Sad", mFace.getEmotions().getSadProbability());
        emotions.put("Disgust", mFace.getEmotions().getDisgustProbability());
        emotions.put("Surprise", mFace.getEmotions().getSurpriseProbability());
        List<String> result = sortHashMap(emotions);

        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        // Draw the facial feature value.
        canvas.drawText("Left eye: " + decimalFormat.format(mFace.getFeatures().getLeftEyeOpenProbability()), x, y, probilityPaint);
        x = x + width;
        canvas.drawText("Right eye: " + decimalFormat.format(mFace.getFeatures().getRightEyeOpenProbability()), x, y, probilityPaint);
        y = y - 40.0f;
        x = start;
        canvas.drawText("Moustache Probability: " + decimalFormat.format(mFace.getFeatures().getMoustacheProbability()), x, y, probilityPaint);
        x = x + width;
        canvas.drawText("Glass Probability: " + decimalFormat.format(mFace.getFeatures().getSunGlassProbability()), x, y, probilityPaint);
        y = y - 40.0f;
        x = start;
        canvas.drawText("Hat Probability: " + decimalFormat.format(mFace.getFeatures().getHatProbability()), x, y, probilityPaint);
        x = x + width;
        canvas.drawText("Age: " + mFace.getFeatures().getAge(), x, y, probilityPaint);
        y = y - 40.0f;
        x = start;
        String sex = (mFace.getFeatures().getSexProbability() > 0.5f) ? "Female" : "Male";
        canvas.drawText("Gender: " + sex, x, y, probilityPaint);
        x = x + width;
        canvas.drawText("EulerAngleY: " + decimalFormat.format(mFace.getRotationAngleY()), x, y, probilityPaint);
        y = y - 40.0f;
        x = start;
        canvas.drawText("EulerAngleZ: " + decimalFormat.format(mFace.getRotationAngleZ()), x, y, probilityPaint);
        x = x + width;
        canvas.drawText("EulerAngleX: " + decimalFormat.format(mFace.getRotationAngleX()), x, y, probilityPaint);
        y = y - 40.0f;
        x = start;
        canvas.drawText(result.get(0), x, y, probilityPaint);

        // Draw a face contour.
        if (mFace.getFaceShapeList() != null) {
            for (MLFaceShape faceShape : mFace.getFaceShapeList()) {
                if (faceShape == null) {
                    continue;
                }
                List<MLPosition> points = faceShape.getPoints();
                for (int i = 0; i < points.size(); i++) {
                    MLPosition point = points.get(i);
                    canvas.drawPoint(translateX(point.getX().floatValue()), translateY(point.getY().floatValue()), boxPaint);
                    if (i != (points.size() - 1)) {
                        MLPosition next = points.get(i + 1);
                        if (point != null && point.getX() != null && point.getY() != null) {
                            if (i % 3 == 0) {
                                canvas.drawText(i + 1 + "", translateX(point.getX().floatValue()), translateY(point.getY().floatValue()), textPaint);
                            }
                            canvas.drawLines(new float[]{translateX(point.getX().floatValue()), translateY(point.getY().floatValue()),
                                    translateX(next.getX().floatValue()), translateY(next.getY().floatValue())}, getPaint(faceShape));
                        }
                    }
                }
            }
        }
        // Face Key Points
        for (MLFaceKeyPoint keyPoint : mFace.getFaceKeyPoints()) {
            if (keyPoint != null) {
                MLPosition point = keyPoint.getPoint();
                canvas.drawCircle(
                        translateX(point.getX()),
                        translateY(point.getY()),
                        10f, landmarkPaint);
            }
        }
    }

    private Paint getPaint(MLFaceShape faceShape) {
        switch (faceShape.getFaceShapeType()) {
            case MLFaceShape.TYPE_LEFT_EYE:
            case MLFaceShape.TYPE_RIGHT_EYE:
                return eyePaint;
            case MLFaceShape.TYPE_BOTTOM_OF_LEFT_EYEBROW:

            case MLFaceShape.TYPE_BOTTOM_OF_RIGHT_EYEBROW:
            case MLFaceShape.TYPE_TOP_OF_LEFT_EYEBROW:
            case MLFaceShape.TYPE_TOP_OF_RIGHT_EYEBROW:
                return eyebrowPaint;
            case MLFaceShape.TYPE_BOTTOM_OF_LOWER_LIP:
            case MLFaceShape.TYPE_TOP_OF_LOWER_LIP:
            case MLFaceShape.TYPE_BOTTOM_OF_UPPER_LIP:
            case MLFaceShape.TYPE_TOP_OF_UPPER_LIP:
                return lipPaint;
            case MLFaceShape.TYPE_BOTTOM_OF_NOSE:
                return noseBasePaint;
            case MLFaceShape.TYPE_BRIDGE_OF_NOSE:
                return nosePaint;
            default:
                return facePaint;
        }
    }
}