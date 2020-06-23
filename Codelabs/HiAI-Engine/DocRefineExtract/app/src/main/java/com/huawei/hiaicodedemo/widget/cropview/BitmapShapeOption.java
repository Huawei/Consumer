package com.huawei.hiaicodedemo.widget.cropview;

public class BitmapShapeOption {

    private int strokeWidth;//画笔线宽
    private int strokeColor;//画笔线颜色
    private boolean hasInverseEvenOdd;//是否反转判断图片内外的奇偶规则

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public boolean isHasInverseEvenOdd() {
        return hasInverseEvenOdd;
    }

    public void setHasInverseEvenOdd(boolean hasInverseEvenOdd) {
        this.hasInverseEvenOdd = hasInverseEvenOdd;
    }

    public static class Builder {
        private BitmapShapeOption mOption;

        public Builder() {
            mOption = new BitmapShapeOption();
        }

        public BitmapShapeOption build() {
            return mOption;
        }

        public int getStrokeWidth() {
            return mOption.strokeWidth;
        }

        public Builder setStrokeWidth(int strokeWidth) {
            mOption.strokeWidth = strokeWidth;
            return this;
        }

        public int getStrokeColor() {
            return mOption.strokeColor;
        }

        public Builder setStrokeColor(int strokeColor) {
            mOption.strokeColor = strokeColor;
            return this;
        }

        public boolean isHasInverseEvenOdd() {
            return mOption.hasInverseEvenOdd;
        }

        public Builder setHasInverseEvenOdd(boolean hasInverseEvenOdd) {
            mOption.hasInverseEvenOdd = hasInverseEvenOdd;
            return this;
        }
    }
}
