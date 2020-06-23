package com.huawei.hiaicodedemo.widget.cropview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.NonNull;

public class BitmapUtils {

    /**
     * 获取进行过值判断生成的RectF
     *
     * @param left   矩形左边距离
     * @param top    矩形上边距离
     * @param right  矩形长度
     * @param bottom 矩形高度
     * @return 返回RectF
     */
    @NonNull
    public static RectF getRectF(float left, float top, float right, float bottom) {
        if (left < 0) {
            left = 0;
        }
        if (top < 0) {
            top = 0;
        }
        if (right < left) {
            right = left;
        }
        if (bottom < top) {
            bottom = top;
        }
        return new RectF(left, top, right, bottom);
    }

    /**
     * 根据形状选项，进行额外绘制
     *
     * @param canvas 画布
     * @param paint  画笔
     * @param path   绘制路径
     * @param option 形状选项
     */
    public static void drawShapeOption(Canvas canvas, Paint paint, Path path, BitmapShapeOption option) {
        if (canvas == null || paint == null || path == null || option == null) {
            return;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(option.getStrokeWidth());
        paint.setColor(option.getStrokeColor());
        canvas.drawPath(path, paint);
    }

    /**
     * 裁剪路径图形
     *
     * @param canvas 画布
     * @param path   裁剪路径
     * @param option 额外选项
     */
    public static void clipPath(Canvas canvas, Path path, BitmapShapeOption option) {
        if (canvas == null || path == null) {
            return;
        }
        if (option != null && option.isHasInverseEvenOdd()) {
            path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        }
        canvas.clipPath(path);
        path.setFillType(Path.FillType.EVEN_ODD);
    }
}
