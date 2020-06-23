package com.huawei.hiaicodedemo.widget.cropview;

import android.graphics.RectF;
import androidx.annotation.NonNull;

/**
 * 裁剪框上下左右的四个坐标位置：(LEFT,TOP),(LEFT,RIGHT),(RIGHT,BOTTOM),(LEFT,BOTTOM)四个坐标点
 * 组成的矩形就是裁剪框
 */

public enum Edge {
    LEFT,
    TOP,
    RIGHT,
    BOTTOM;

    //裁剪框的最小宽度或者高度
    static final int MIN_CROP_LENGTH_PX = 80;

    //上下左右边界的的值
    private float mCoordinate;

    //初始化裁剪框时
    public void initCoordinate(float coordinate) {
        mCoordinate = coordinate;
    }

    public float getCoordinate() {
        return mCoordinate;
    }

    /**
     * 随着手指的移动而改变坐标值
     *
     * @param distance
     */
    public void offset(float distance) {
        mCoordinate += distance;
    }

    /**
     * 判断裁剪框是否超越图片指定的边界
     */
    public boolean isOutsideMargin(@NonNull RectF rect) {

        final boolean result;

        switch (this) {
            case LEFT:
                result = mCoordinate - rect.left < 0;
                break;
            case TOP:
                result = mCoordinate - rect.top < 0;
                break;
            case RIGHT:
                result = rect.right - mCoordinate < 0;
                break;
            default: // BOTTOM
                result = rect.bottom - mCoordinate < 0;
                break;
        }
        return result;
    }

    /**
     * 获取剪切框的宽
     */
    public static float getWidth() {
        return Edge.RIGHT.getCoordinate() - Edge.LEFT.getCoordinate();
    }

    /**
     * 获取剪切框的高
     */
    public static float getHeight() {
        return Edge.BOTTOM.getCoordinate() - Edge.TOP.getCoordinate();
    }


    /**
     * 更新某条边的坐标位置
     */
    public void updateCoordinate(float x, float y, @NonNull RectF imageRect) {

        switch (this) {
            case LEFT:
                mCoordinate = adjustLeft(x, imageRect);
                break;
            case TOP:
                mCoordinate = adjustTop(y, imageRect);
                break;
            case RIGHT:
                mCoordinate = adjustRight(x, imageRect);
                break;
            case BOTTOM:
                mCoordinate = adjustBottom(y, imageRect);
                break;
        }
    }

    private static float adjustLeft(float x, @NonNull RectF imageRect) {

        final float resultX;
        if (x - imageRect.left < 0) {//左边越界

            resultX = imageRect.left;
        } else {

            //防止裁剪框左边超过右边或者最小范围
            if ((x + MIN_CROP_LENGTH_PX) >= Edge.RIGHT.getCoordinate()) {
                x = Edge.RIGHT.getCoordinate() - MIN_CROP_LENGTH_PX;
            }

            resultX = x;
        }
        return resultX;
    }


    private static float adjustRight(float x, @NonNull RectF imageRect) {

        final float resultX;

        if (imageRect.right - x < 0) {

            resultX = imageRect.right;

        } else {

            //防止裁剪框右边超过最小范围
            if ((x - MIN_CROP_LENGTH_PX) <= Edge.LEFT.getCoordinate()) {
                x = Edge.LEFT.getCoordinate() + MIN_CROP_LENGTH_PX;
            }

            resultX = x;
        }
        return resultX;
    }

    private static float adjustTop(float y, @NonNull RectF imageRect) {

        final float resultY;

        if (y - imageRect.top < 0) {
            resultY = imageRect.top;
        } else {
            //防止裁剪框上边超过最小范围或者越过最下边
            if ((y + MIN_CROP_LENGTH_PX) >= Edge.BOTTOM.getCoordinate()) {
                y = Edge.BOTTOM.getCoordinate() - MIN_CROP_LENGTH_PX;

            }

            resultY = y;
        }
        return resultY;
    }


    private static float adjustBottom(float y, @NonNull RectF imageRect) {

        final float resultY;

        if (imageRect.bottom - y < 0) {
            resultY = imageRect.bottom;
        } else {

            if ((y - MIN_CROP_LENGTH_PX) <= Edge.TOP.getCoordinate()) {
                y = Edge.TOP.getCoordinate() + MIN_CROP_LENGTH_PX;
            }

            resultY = y;
        }
        return resultY;
    }
}
