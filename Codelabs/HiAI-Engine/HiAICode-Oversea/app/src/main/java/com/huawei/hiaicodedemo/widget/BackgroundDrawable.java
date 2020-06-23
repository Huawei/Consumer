package com.huawei.hiaicodedemo.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

public class BackgroundDrawable extends ShapeDrawable {

	private int topColor;
	private int bottomColor;
	private int leftPercent;
	private int rightPercent;
	private Paint topPaint;
	private Paint bottomPaint;

	private BackgroundDrawable(Builder builder) {
		super(builder.shape);

		topColor = builder.topColor;
		bottomColor = builder.bottomColor;
		leftPercent = builder.leftPercent;
		rightPercent = builder.rightPercent;

		topPaint = new Paint();
		topPaint.setStyle(Paint.Style.FILL);
		topPaint.setAntiAlias(true);
		topPaint.setColor(topColor);

		bottomPaint = new Paint();
		bottomPaint.setStyle(Paint.Style.FILL);
		bottomPaint.setAntiAlias(true);
		bottomPaint.setColor(bottomColor);

	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		Rect r = getBounds();
		canvas.drawRect(r, topPaint);

		int lBottom;
		int rBottom;
		if (leftPercent > 0 && rightPercent > 0 && leftPercent < 100 && rightPercent < 100) {

			lBottom = (int) (r.bottom * leftPercent * 0.01);
			rBottom = (int) (r.bottom * rightPercent * 0.01);
			Path path = new Path();
			path.lineTo(r.left, r.top);
			path.lineTo(r.left, lBottom);
			path.lineTo(r.right, rBottom);
			path.lineTo(r.right, r.top);

			Rect bRect = new Rect(r.left, r.top, r.right, r.bottom);
			canvas.drawRect(bRect, bottomPaint);
			canvas.drawPath(path, topPaint);


		} else {
			topPaint.setColor(Color.WHITE);
			canvas.drawRect(r, topPaint);

			TextPaint textPaint = new TextPaint();
			textPaint.setARGB(0xFF, 0xFF, 0, 0);
			textPaint.setColor(Color.BLACK);
			textPaint.setTextSize(40);
			textPaint.setStrokeWidth(5);
			StaticLayout layout = new StaticLayout("The values of left and right are going to be greater than 0 and less than 100", textPaint, r.right,
					Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
			canvas.translate(20, r.bottom / 3);
			layout.draw(canvas);
		}

	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder implements IShapeDrawableBuilder {

		private RectShape shape;
		private int topColor;
		private int bottomColor;
		private int leftPercent;
		private int rightPercent;

		private Builder() {
			shape = new RectShape();
			topColor = Color.WHITE;
			bottomColor = Color.WHITE;
			leftPercent = 0;
			rightPercent = 0;
		}

		@Override
		public IShapeDrawableBuilder left(int percent) {
			leftPercent = percent;
			return this;
		}

		@Override
		public IShapeDrawableBuilder right(int percent) {
			rightPercent = percent;
			return this;
		}

		@Override
		public IShapeDrawableBuilder topColor(int topColor) {
			this.topColor = topColor;
			return this;
		}

		@Override
		public IShapeDrawableBuilder bottomColor(int bottomColor) {
			this.bottomColor = bottomColor;
			return this;
		}

		@Override
		public BackgroundDrawable build() {
			return new BackgroundDrawable(this);
		}
	}

	public interface IShapeDrawableBuilder {
		public IShapeDrawableBuilder left(int percent);

		public IShapeDrawableBuilder right(int percent);

		public IShapeDrawableBuilder topColor(int topColor);

		public IShapeDrawableBuilder bottomColor(int bottomColor);

		public BackgroundDrawable build();
	}

}
