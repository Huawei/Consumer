package com.huawei.hiaicodedemo.widget.cropview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.huawei.hiai.vision.image.docrefine.DocRefine;
import com.huawei.hiai.vision.visionkit.common.Frame;
import com.huawei.hiai.vision.visionkit.image.ImageResult;
import com.huawei.hiai.vision.visionkit.image.detector.DocCoordinates;
import com.huawei.hiaicodedemo.R;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.cert.PolicyNode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("AppCompatCustomView")
public class CropImageView extends ImageView {
    //裁剪框边框画笔
    private Paint mBorderPaint;

    //裁剪框九宫格画笔
    private Paint mGuidelinePaint;

    //绘制裁剪边框四个角的画笔
    private Paint mCornerPaint1;
    private Paint mCornerPaint2;

    private Paint cropPaint;

    //判断手指位置是否处于缩放裁剪框位置的范围：如果是当手指移动的时候裁剪框会相应的变化大小
    //否则手指移动的时候就是拖动裁剪框使之随着手指移动
    private float mScaleRadius;


    //四个角的半径长度
    private float mCornerRadius;

    //用来表示图片边界的矩形
    public RectF mBitmapRect = new RectF();


    private CropWindowEdgeSelector mPressedCropWindowEdgeSelector;

    //手指位置距离裁剪框的偏移量
    private PointF mTouchOffset = new PointF();


    public final static int RECT = 0;//矩形裁剪框
    public final static int FREE_SHAPE = 1;//自由裁剪框
    //设置裁剪模式默认矩形裁剪框;
    private int corpMode;
    //过滤模式
    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    //蒙黑背景
    private Bitmap mBitmapCover;
    //最底部背景
    private Bitmap mBitmaplast;


    //描述四条边的路径
    private Path edgePath;
    //描述四个区域
    private Lasso region1;
    private Lasso region2;
    private Lasso region3;
    private Lasso region4;

    //原图与显示图的缩放比例
    private float scaleX;
    private float scaleY;
    private float transX;
    private float transY;
    private int drawableDisplayWidth;
    private int drawableDisplayHeight;

    private DocCoordinates docCoordinates;

    public void setDocCoordinates(DocCoordinates docCoordinates){
        this.docCoordinates = docCoordinates;
    };

    public void setCropMode(int mode) {
        this.corpMode = mode;
    }

    public CropImageView(Context context) {
        super(context);
    }

    public CropImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 里面的值暂时写死，也可以从AttributeSet里面来配置
     *
     * @param context
     */
    private void init(@NonNull Context context) {

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStrokeWidth(dip2px(context, 1.5f));
        mBorderPaint.setColor(Color.parseColor("#ff79C0F9"));

        mGuidelinePaint = new Paint();
        mGuidelinePaint.setStyle(Paint.Style.STROKE);
        mGuidelinePaint.setStrokeWidth(dip2px(context, 1));
        mGuidelinePaint.setColor(Color.parseColor("#ff79C0F9"));


        mCornerPaint1 = new Paint();
        mCornerPaint1.setStyle(Paint.Style.FILL);
        mCornerPaint1.setStrokeWidth(dip2px(context, 1));
        mCornerPaint1.setColor(Color.parseColor("#FF017EFF"));

        mCornerPaint2 = new Paint();
        mCornerPaint2.setStyle(Paint.Style.FILL);
        mCornerPaint2.setStrokeWidth(dip2px(context, 1));
        mCornerPaint2.setColor(Color.parseColor("#aa79C0F9"));

        mScaleRadius = dip2px(context, 20);
        mCornerRadius = dip2px(context, 7);

        cropPaint = new Paint();
        cropPaint.setStyle(Paint.Style.FILL);
        cropPaint.setStrokeWidth(dip2px(context, 1));
        cropPaint.setColor(Color.parseColor("#000000"));

    }

    /*生成bitmap*/
    private Bitmap makeBitmap(int mwidth, int mheight, int resource, int staX, int staY) {
        Bitmap bm = Bitmap.createBitmap(mwidth, mheight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(resource);
        c.drawRect(staX, staY, mwidth, mheight, p);
        return bm;
    }


    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(@Nullable Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        //获取图片的范围RectF
        mBitmapRect = getBitmapRect();
        //初始化裁剪框的大小
        initCropWindow(mBitmapRect);

        mBitmapCover = makeBitmap(getWidth(), getHeight(), 0x9a191917, 0, 0);
        mBitmaplast = makeBitmap(getWidth(), getHeight(), 0xff000000, 0, 0);
    }


    /**
     * 获取图片ImageView周围的边界组成的RectF对象
     * 注意:不同屏幕像素密度的情况下图片会被拉伸。而这个getIntrinsicWidth（）得到的就是拉伸后的宽度，不是真正图片的宽度。
     * 有时候原有宽度可能很大，但是实际上空间不够，所有效果上并没有那么大，这个方法可以获得原有宽度，可以辅助测量的时候选择合适的展示宽度。
     */
    private RectF getBitmapRect() {

        final Drawable drawable = getDrawable();
        if (drawable == null) {
            return new RectF();
        }

        final float[] matrixValues = new float[9];
        getImageMatrix().getValues(matrixValues);
        final float scaleX = matrixValues[Matrix.MSCALE_X];
        final float scaleY = matrixValues[Matrix.MSCALE_Y];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.transX = transX;
        this.transY = transY;

        final int drawableIntrinsicWidth = drawable.getIntrinsicWidth();
        final int drawableIntrinsicHeight = drawable.getIntrinsicHeight();
        final int drawableDisplayWidth = Math.round(drawableIntrinsicWidth * scaleX);
        final int drawableDisplayHeight = Math.round(drawableIntrinsicHeight * scaleY);
        this.drawableDisplayWidth = drawableDisplayWidth;
        this.drawableDisplayHeight = drawableDisplayHeight;

        float left = getWidth() / 2 - drawableDisplayWidth / 2;
        if (left < 0) left = 0;
        float top = getHeight() / 2 - drawableDisplayHeight / 2;
        if (top < 0) top = 0;
        float right = left + drawableDisplayWidth;
        float bottom = top + drawableDisplayHeight;


        return new RectF(left, top, right, bottom);


    }

    /**
     * 初始化裁剪框
     *
     * @param bitmapRect
     */
    private void initCropWindow(@NonNull RectF bitmapRect) {
        switch (corpMode) {
            case CropImageView.RECT:
                //裁剪框距离图片左右的padding值
                //final float horizontalPadding = 0.1f * bitmapRect.width();
                //final float verticalPadding = 0.1f * bitmapRect.height();
                //初始化裁剪框上下左右四条边
                Edge.LEFT.initCoordinate(bitmapRect.left);
                Edge.TOP.initCoordinate(bitmapRect.top);
                Edge.RIGHT.initCoordinate(bitmapRect.right);
                Edge.BOTTOM.initCoordinate(bitmapRect.bottom);
                break;
            case CropImageView.FREE_SHAPE:
                //初始化自由裁剪框的四个点
                if(docCoordinates != null){
                    int w = ((BitmapDrawable) getDrawable()).getBitmap().getWidth();
                    int h = ((BitmapDrawable) getDrawable()).getBitmap().getHeight();
                    CornerPoint.TOP_LEFT.setPointX( Math.round(Math.max(1, docCoordinates.getTopLeftCoordinate().x) *scaleX+transX));
                    CornerPoint.TOP_LEFT.setPointY( Math.round(Math.max(1,docCoordinates.getTopLeftCoordinate().y)*scaleY+transY));
                    CornerPoint.TOP_RIGHT.setPointX(  Math.round(Math.min(w-1,docCoordinates.getTopRightCoordinate().x)*scaleX+transX));
                    CornerPoint.TOP_RIGHT.setPointY( Math.round(Math.max(1,docCoordinates.getTopRightCoordinate().y)*scaleY+transY));
                    CornerPoint.BOTTOM_LEFT.setPointX( Math.round(Math.max(1,docCoordinates.getBottomLeftCoordinate().x)*scaleX+transX));
                    CornerPoint.BOTTOM_LEFT.setPointY( Math.round(Math.min(h-1,docCoordinates.getBottomLeftCoordinate().y)*scaleY+transY));
                    CornerPoint.BOTTOM_RIGHT.setPointX( Math.round(Math.min(w-1,docCoordinates.getBottomRightCoordinate().x)*scaleX+transX));
                    CornerPoint.BOTTOM_RIGHT.setPointY( Math.round(Math.min(h-1,docCoordinates.getBottomRightCoordinate().y)*scaleY+transY));
                }else {
                    CornerPoint.TOP_LEFT.setPointX((int) bitmapRect.left+30);
                    CornerPoint.TOP_LEFT.setPointY((int) bitmapRect.top+30);
                    CornerPoint.TOP_RIGHT.setPointX((int) bitmapRect.right-30);
                    CornerPoint.TOP_RIGHT.setPointY((int) bitmapRect.top+30);
                    CornerPoint.BOTTOM_LEFT.setPointX((int) bitmapRect.left+30);
                    CornerPoint.BOTTOM_LEFT.setPointY((int) bitmapRect.bottom-30);
                    CornerPoint.BOTTOM_RIGHT.setPointX((int) bitmapRect.right-30);
                    CornerPoint.BOTTOM_RIGHT.setPointY((int) bitmapRect.bottom-30);
                }


                region1 = new Lasso(new float[]{CornerPoint.TOP_LEFT.getPointX(), (CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2, ((CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2) / 2, (CornerPoint.TOP_LEFT.getPointX() + CornerPoint.BOTTOM_LEFT.getPointX()) / 2}, new float[]{CornerPoint.TOP_LEFT.getPointY(), (CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2, ((CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2) / 2, (CornerPoint.TOP_LEFT.getPointY() + CornerPoint.BOTTOM_LEFT.getPointY()) / 2}, 4);
                region2 = new Lasso(new float[]{(CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2, CornerPoint.TOP_RIGHT.getPointX(), (CornerPoint.TOP_RIGHT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2, ((CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2) / 2}, new float[]{(CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2, CornerPoint.TOP_RIGHT.getPointY(), (CornerPoint.TOP_RIGHT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2, ((CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2) / 2}, 4);
                region3 = new Lasso(new float[]{(CornerPoint.TOP_LEFT.getPointX() + CornerPoint.BOTTOM_LEFT.getPointX()) / 2, ((CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2) / 2, (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2, CornerPoint.BOTTOM_LEFT.getPointX()}, new float[]{(CornerPoint.TOP_LEFT.getPointY() + CornerPoint.BOTTOM_LEFT.getPointY()) / 2, ((CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2) / 2, (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2, CornerPoint.BOTTOM_LEFT.getPointY()}, 4);
                region4 = new Lasso(new float[]{((CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2) / 2, (CornerPoint.TOP_RIGHT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2, CornerPoint.BOTTOM_RIGHT.getPointX(), (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2}, new float[]{((CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2) / 2, (CornerPoint.TOP_RIGHT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2, CornerPoint.BOTTOM_RIGHT.getPointY(), (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2}, 4);


                break;
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        //画图片
        canvas.drawBitmap(mBitmaplast, 0, 0, mBorderPaint);
        canvas.drawBitmap(((BitmapDrawable) getDrawable()).getBitmap(), null, mBitmapRect, mBorderPaint);
        int saveCount = 0;
        if (corpMode == CropImageView.FREE_SHAPE) {
            //将绘制操作保存到新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
            saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), mBorderPaint, Canvas.ALL_SAVE_FLAG);
        }
        //画蒙黑背景
        canvas.drawBitmap(mBitmapCover, 0, 0, mBorderPaint);
        //绘制裁剪边框
        drawBorder(canvas, saveCount);
        //绘制九宫格引导线
        drawGuidelines(canvas);
        //绘制裁剪边框的四个角
        drawCorners(canvas);
    }


    private void drawBorder(@NonNull Canvas canvas, int saveCount) {
        switch (corpMode) {
            case CropImageView.RECT:
                canvas.drawRect(Edge.LEFT.getCoordinate(),
                        Edge.TOP.getCoordinate(),
                        Edge.RIGHT.getCoordinate(),
                        Edge.BOTTOM.getCoordinate(),
                        mBorderPaint);
                break;
            case CropImageView.FREE_SHAPE:
                edgePath = new Path();
                edgePath.moveTo(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                edgePath.lineTo(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                edgePath.lineTo(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                edgePath.lineTo(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                edgePath.close();

                InRegion.contiansPoint(region1, region2, region3, region4, edgePath);
                mBorderPaint.setStyle(Paint.Style.FILL);
                //设置过滤
                mBorderPaint.setXfermode(xfermode);
                canvas.drawPath(edgePath, mBorderPaint);
                mBorderPaint.setXfermode(null);
                //画边
                mBorderPaint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(edgePath, mBorderPaint);
                //还原画布
                canvas.restoreToCount(saveCount);

                return;
        }


    }


    private void drawGuidelines(@NonNull Canvas canvas) {
        switch (corpMode) {
            case CropImageView.RECT:
                final float left = Edge.LEFT.getCoordinate();
                final float top = Edge.TOP.getCoordinate();
                final float right = Edge.RIGHT.getCoordinate();
                final float bottom = Edge.BOTTOM.getCoordinate();

                final float oneThirdCropWidth = Edge.getWidth() / 3;

                final float x1 = left + oneThirdCropWidth;
                //引导线竖直方向第一条线
                canvas.drawLine(x1, top, x1, bottom, mGuidelinePaint);
                final float x2 = right - oneThirdCropWidth;
                //引导线竖直方向第二条线
                canvas.drawLine(x2, top, x2, bottom, mGuidelinePaint);

                final float oneThirdCropHeight = Edge.getHeight() / 3;

                final float y1 = top + oneThirdCropHeight;
                //引导线水平方向第一条线
                canvas.drawLine(left, y1, right, y1, mGuidelinePaint);
                final float y2 = bottom - oneThirdCropHeight;
                //引导线水平方向第二条线
                canvas.drawLine(left, y2, right, y2, mGuidelinePaint);
                break;
            case CropImageView.FREE_SHAPE:
                int top_left_x = CornerPoint.TOP_LEFT.getPointX();
                int top_left_y = CornerPoint.TOP_LEFT.getPointY();

                int top_right_x = CornerPoint.TOP_RIGHT.getPointX();
                int top_right_y = CornerPoint.TOP_RIGHT.getPointY();

                int bottom_left_x = CornerPoint.BOTTOM_LEFT.getPointX();
                int bottom_left_y = CornerPoint.BOTTOM_LEFT.getPointY();

                int bottom_right_x = CornerPoint.BOTTOM_RIGHT.getPointX();
                int bottom_right_y = CornerPoint.BOTTOM_RIGHT.getPointY();

                canvas.drawLine((top_left_x + bottom_left_x) / 2, (top_left_y + bottom_left_y) / 2, (top_right_x + bottom_right_x) / 2, (top_right_y + bottom_right_y) / 2, mGuidelinePaint);
                canvas.drawLine((top_left_x + (top_left_x + bottom_left_x) / 2) / 2, (top_left_y + (top_left_y + bottom_left_y) / 2) / 2, (top_right_x + (top_right_x + bottom_right_x) / 2) / 2, (top_right_y + (top_right_y + bottom_right_y) / 2) / 2, mGuidelinePaint);
                canvas.drawLine((bottom_left_x + (top_left_x + bottom_left_x) / 2) / 2, (bottom_left_y + (top_left_y + bottom_left_y) / 2) / 2, (bottom_right_x + (top_right_x + bottom_right_x) / 2) / 2, (bottom_right_y + (top_right_y + bottom_right_y) / 2) / 2, mGuidelinePaint);

                canvas.drawLine((top_left_x + top_right_x) / 2, (top_left_y + top_right_y) / 2, (bottom_left_x + bottom_right_x) / 2, (bottom_left_y + bottom_right_y) / 2, mGuidelinePaint);
                canvas.drawLine((top_left_x + (top_left_x + top_right_x) / 2) / 2, (top_left_y + (top_left_y + top_right_y) / 2) / 2, (bottom_left_x + (bottom_left_x + bottom_right_x) / 2) / 2, (bottom_left_y + (bottom_left_y + bottom_right_y) / 2) / 2, mGuidelinePaint);
                canvas.drawLine((top_right_x + (top_left_x + top_right_x) / 2) / 2, (top_right_y + (top_left_y + top_right_y) / 2) / 2, (bottom_right_x + (bottom_left_x + bottom_right_x) / 2) / 2, (bottom_right_y + (bottom_left_y + bottom_right_y) / 2) / 2, mGuidelinePaint);
                return;
        }

    }

    private void drawCorners(Canvas canvas) {
        switch (corpMode) {
            case CropImageView.RECT:
                final float left = Edge.LEFT.getCoordinate();
                final float top = Edge.TOP.getCoordinate();
                final float right = Edge.RIGHT.getCoordinate();
                final float bottom = Edge.BOTTOM.getCoordinate();


                canvas.drawCircle(left, top, mCornerRadius + 5, mCornerPaint2);
                canvas.drawCircle(left, top, mCornerRadius, mCornerPaint1);

                canvas.drawCircle(right, top, mCornerRadius + 5, mCornerPaint2);
                canvas.drawCircle(right, top, mCornerRadius, mCornerPaint1);


                canvas.drawCircle(left, bottom, mCornerRadius + 5, mCornerPaint2);
                canvas.drawCircle(left, bottom, mCornerRadius, mCornerPaint1);

                canvas.drawCircle(right, bottom, mCornerRadius + 5, mCornerPaint2);
                canvas.drawCircle(right, bottom, mCornerRadius, mCornerPaint1);
                break;
            case CropImageView.FREE_SHAPE:

                canvas.drawCircle(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY(), mCornerRadius + 5, mCornerPaint2);
                canvas.drawCircle(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY(), mCornerRadius, mCornerPaint1);

                canvas.drawCircle(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY(), mCornerRadius + 5, mCornerPaint2);
                canvas.drawCircle(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY(), mCornerRadius, mCornerPaint1);

                canvas.drawCircle(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY(), mCornerRadius + 5, mCornerPaint2);
                canvas.drawCircle(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY(), mCornerRadius, mCornerPaint1);

                canvas.drawCircle(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY(), mCornerRadius + 5, mCornerPaint2);
                canvas.drawCircle(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY(), mCornerRadius, mCornerPaint1);
                return;
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                onActionDown(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onActionUp();
                return true;

            case MotionEvent.ACTION_MOVE:
                onActionMove(event.getX(), event.getY());
                return true;
            default:
                return false;

        }
    }


    /**
     * 处理手指按下事件
     *
     * @param x 手指按下时水平方向的坐标
     * @param y 手指按下时竖直方向的坐标
     */
    private void onActionDown(float x, float y) {
        switch (corpMode) {
            case CropImageView.RECT:
                //获取边框的上下左右四个坐标点的坐标
                final float left = Edge.LEFT.getCoordinate();
                final float top = Edge.TOP.getCoordinate();
                final float right = Edge.RIGHT.getCoordinate();
                final float bottom = Edge.BOTTOM.getCoordinate();

                //获取手指所在位置位于图二种的A，B，C，D位置种哪一种
                mPressedCropWindowEdgeSelector = CatchEdgeUtil.getPressedHandle(x, y, left, top, right, bottom, mScaleRadius);

                if (mPressedCropWindowEdgeSelector != null) {
                    //计算手指按下的位置与裁剪框的偏移量
                    CatchEdgeUtil.getOffset(mPressedCropWindowEdgeSelector, x, y, left, top, right, bottom, mTouchOffset);
                }
                break;
            case CropImageView.FREE_SHAPE:

                float nearestDistance = 50;
                //计算手指距离左上角的距离
                final float distanceToTopLeft = CatchEdgeUtil.calculateDistance(x, y, CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                if (distanceToTopLeft < nearestDistance) {
                    mTouchOffset.x = CornerPoint.TOP_LEFT.getPointX() - x;
                    mTouchOffset.y = CornerPoint.TOP_LEFT.getPointY() - y;
                    CornerPoint.TOP_LEFT.setClick(true);
                    return;
                }
                //计算手指距离右上角的距离
                final float distanceToTopRight = CatchEdgeUtil.calculateDistance(x, y, CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                if (distanceToTopRight < nearestDistance) {
                    mTouchOffset.x = CornerPoint.TOP_RIGHT.getPointX() - x;
                    mTouchOffset.y = CornerPoint.TOP_RIGHT.getPointY() - y;
                    CornerPoint.TOP_RIGHT.setClick(true);
                    return;
                }
                //计算手指距离左下角的距离
                final float distanceToBottomLeft = CatchEdgeUtil.calculateDistance(x, y, CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                if (distanceToBottomLeft < nearestDistance) {
                    mTouchOffset.x = CornerPoint.BOTTOM_LEFT.getPointX() - x;
                    mTouchOffset.y = CornerPoint.BOTTOM_LEFT.getPointY() - y;
                    CornerPoint.BOTTOM_LEFT.setClick(true);
                    return;
                }
                //计算手指距离右下角的距离
                final float distanceToBottomRight = CatchEdgeUtil.calculateDistance(x, y, CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                if (distanceToBottomRight < nearestDistance) {
                    mTouchOffset.x = CornerPoint.BOTTOM_RIGHT.getPointX() - x;
                    mTouchOffset.y = CornerPoint.BOTTOM_RIGHT.getPointY() - y;
                    CornerPoint.BOTTOM_RIGHT.setClick(true);
                    return;
                }
        }


    }


    private void onActionUp() {
        switch (corpMode) {
            case CropImageView.RECT:
                if (mPressedCropWindowEdgeSelector != null) {
                    mPressedCropWindowEdgeSelector = null;
                }
                break;
            case CropImageView.FREE_SHAPE:


                CornerPoint.TOP_LEFT.setClick(false);
                CornerPoint.TOP_RIGHT.setClick(false);
                CornerPoint.BOTTOM_LEFT.setClick(false);
                CornerPoint.BOTTOM_RIGHT.setClick(false);
                break;
        }

    }


    private void onActionMove(float x, float y) {
        switch (corpMode) {
            case CropImageView.RECT:
                if (mPressedCropWindowEdgeSelector == null) {
                    return;
                }
                x += mTouchOffset.x;
                y += mTouchOffset.y;
                mPressedCropWindowEdgeSelector.updateCropWindow(x, y, mBitmapRect);
                invalidate();
                break;
            case CropImageView.FREE_SHAPE:
                x += mTouchOffset.x;
                y += mTouchOffset.y;

                if (CornerPoint.TOP_LEFT.isClick()) {
                    CornerPoint.TOP_LEFT.setPointX((int) x);
                    CornerPoint.TOP_LEFT.setPointY((int) y);
                   // Log.e("tag", "click1");
                }
                if (CornerPoint.TOP_RIGHT.isClick()) {
                    CornerPoint.TOP_RIGHT.setPointX((int) x);
                    CornerPoint.TOP_RIGHT.setPointY((int) y);
                    //Log.e("tag", "click2");
                }
                if (CornerPoint.BOTTOM_LEFT.isClick()) {
                    CornerPoint.BOTTOM_LEFT.setPointX((int) x);
                    CornerPoint.BOTTOM_LEFT.setPointY((int) y);
                  //  Log.e("tag", "click3");
                }
                if (CornerPoint.BOTTOM_RIGHT.isClick()) {
                    CornerPoint.BOTTOM_RIGHT.setPointX((int) x);
                    CornerPoint.BOTTOM_RIGHT.setPointY((int) y);
                  //  Log.e("tag", "click4");
                }

                region1 = new Lasso(new float[]{CornerPoint.TOP_LEFT.getPointX(), (CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2, ((CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2) / 2, (CornerPoint.TOP_LEFT.getPointX() + CornerPoint.BOTTOM_LEFT.getPointX()) / 2}, new float[]{CornerPoint.TOP_LEFT.getPointY(), (CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2, ((CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2) / 2, (CornerPoint.TOP_LEFT.getPointY() + CornerPoint.BOTTOM_LEFT.getPointY()) / 2}, 4);
                region2 = new Lasso(new float[]{(CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2, CornerPoint.TOP_RIGHT.getPointX(), (CornerPoint.TOP_RIGHT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2, ((CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2) / 2}, new float[]{(CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2, CornerPoint.TOP_RIGHT.getPointY(), (CornerPoint.TOP_RIGHT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2, ((CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2) / 2}, 4);
                region3 = new Lasso(new float[]{(CornerPoint.TOP_LEFT.getPointX() + CornerPoint.BOTTOM_LEFT.getPointX()) / 2, ((CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2) / 2, (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2, CornerPoint.BOTTOM_LEFT.getPointX()}, new float[]{(CornerPoint.TOP_LEFT.getPointY() + CornerPoint.BOTTOM_LEFT.getPointY()) / 2, ((CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2) / 2, (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2, CornerPoint.BOTTOM_LEFT.getPointY()}, 4);
                region4 = new Lasso(new float[]{((CornerPoint.TOP_LEFT.getPointX() + CornerPoint.TOP_RIGHT.getPointX()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2) / 2, (CornerPoint.TOP_RIGHT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2, CornerPoint.BOTTOM_RIGHT.getPointX(), (CornerPoint.BOTTOM_LEFT.getPointX() + CornerPoint.BOTTOM_RIGHT.getPointX()) / 2}, new float[]{((CornerPoint.TOP_LEFT.getPointY() + CornerPoint.TOP_RIGHT.getPointY()) / 2 + (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2) / 2, (CornerPoint.TOP_RIGHT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2, CornerPoint.BOTTOM_RIGHT.getPointY(), (CornerPoint.BOTTOM_LEFT.getPointY() + CornerPoint.BOTTOM_RIGHT.getPointY()) / 2}, 4);

                invalidate();
                break;
        }

    }

    /**
     * 获取裁剪好的BitMap
     */
    public Bitmap getCroppedImage() {
        if (corpMode == CropImageView.RECT) {
            final Drawable drawable = getDrawable();
            if (drawable == null || !(drawable instanceof BitmapDrawable)) {
                return null;
            }

            final float[] matrixValues = new float[9];
            getImageMatrix().getValues(matrixValues);

            final float scaleX = matrixValues[Matrix.MSCALE_X];
            final float scaleY = matrixValues[Matrix.MSCALE_Y];
            final float transX = matrixValues[Matrix.MTRANS_X];
            final float transY = matrixValues[Matrix.MTRANS_Y];

            float bitmapLeft = (transX < 0) ? Math.abs(transX) : 0;
            float bitmapTop = (transY < 0) ? Math.abs(transY) : 0;

            final Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();

            final float cropX = (bitmapLeft + Edge.LEFT.getCoordinate()) / scaleX;
            final float cropY = (bitmapTop + Edge.TOP.getCoordinate()) / scaleY;

            final float cropWidth = Math.min(Edge.getWidth() / scaleX, originalBitmap.getWidth() - cropX);
            final float cropHeight = Math.min(Edge.getHeight() / scaleY, originalBitmap.getHeight() - cropY);

            return Bitmap.createBitmap(originalBitmap,
                    (int) cropX,
                    (int) cropY,
                    (int) cropWidth,
                    (int) cropHeight);

        } else {
            Bitmap srcBp = ((BitmapDrawable) getDrawable()).getBitmap();
            if (srcBp == null) {
                return null;
            }
            int width = srcBp.getWidth();
            int height = srcBp.getHeight();
            int wideLength = width;
            int highLength = height;
            if (edgePath != null) {
                RectF rectF = new RectF();
                edgePath.computeBounds(rectF, true);
                wideLength = (int) rectF.width();
                highLength = (int) rectF.height();

            }

            Bitmap destBp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(destBp);
            Rect srcRect = new Rect(0, 0, wideLength, highLength);

            RectF destRect = new RectF();
            destRect.set(0, 0, wideLength, highLength);


            BitmapShapeOption option = new BitmapShapeOption.Builder().setStrokeWidth(2).setStrokeColor(Color.BLUE).build();
            if (edgePath != null) {
                BitmapUtils.clipPath(canvas, edgePath, option);
            }
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(srcBp, null, mBitmapRect, paint);
            //BitmapUtils.drawShapeOption(canvas, paint, edgePath, option);
            //BitmapUtils.clipPath(canvas, edgePath, option);
            return destBp;
        }
    }

    public void getCroppedImage(CroppedImageCall croppedImageCall) {

        Bitmap srcBp = ((BitmapDrawable) getDrawable()).getBitmap();
        RectF clipRectF = new RectF();
        edgePath.computeBounds(clipRectF, true);

        if (!mBitmapRect.contains(clipRectF)) {
            Toast.makeText(getContext(), getContext().getString(R.string.toast_4), Toast.LENGTH_SHORT).show();
            return;
        }
        int newBgLeft = (int) ((clipRectF.left - transX) / scaleX);
        int newBgtTop = (int) ((clipRectF.top - transY) / scaleY);
        int newBgRight = (int) (newBgLeft + clipRectF.width() / scaleX);
        int newBgBottom = (int) (newBgtTop + clipRectF.height() / scaleY);

        DocCoordinates dc = getDocCoordinates(clipRectF.left,clipRectF.top);
        //DocCoordinates dc = getDocCoordinates();
        if(newBgLeft == 0){
            newBgLeft = 1;
        }
        if(newBgtTop == 0){
            newBgtTop = 1;
        }
        Bitmap newBp = Bitmap.createBitmap(srcBp, newBgLeft - 1, newBgtTop, (int) (clipRectF.width() / scaleX), (int) (clipRectF.height() / scaleY));
        croppedImageCall.doCroppedImage(newBp,dc);

    }

    private Bitmap getBitmap(Bitmap bitmap){
        //上面是将全屏截图的结果先裁剪成需要的大小，下面是裁剪成曲线图形区域
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        Bitmap temp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);

        Path path = new Path();
        path.moveTo((CornerPoint.TOP_LEFT.getPointX()-transX) / scaleX,(CornerPoint.TOP_LEFT.getPointX()-transY) / scaleY);
        path.lineTo((CornerPoint.TOP_RIGHT.getPointX()-transX) / scaleX,(CornerPoint.TOP_RIGHT.getPointX()-transY) / scaleY);
        path.lineTo((CornerPoint.BOTTOM_RIGHT.getPointX()-transX) / scaleX,(CornerPoint.BOTTOM_RIGHT.getPointX()-transY) / scaleY);
        path.lineTo((CornerPoint.BOTTOM_LEFT.getPointX()-transX) / scaleX,(CornerPoint.BOTTOM_LEFT.getPointX()-transY) / scaleY);
        canvas.drawPath(path,cropPaint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 关键代码，关于Xfermode和SRC_IN请自行查阅
        canvas.drawBitmap(bitmap, 0 , 0, paint);

        //canvas.drawBitmap(bitmap,0,0,cropPaint);
        return temp;
    }

    private DocCoordinates getDocCoordinates(){
        Point topLeft = new Point(Math.round((CornerPoint.TOP_LEFT.getPointX()-transX) / scaleX),Math.round((CornerPoint.TOP_LEFT.getPointY()-transY) / scaleY));
        Point topRight = new Point(Math.round((CornerPoint.TOP_RIGHT.getPointX()-transX) / scaleX),Math.round((CornerPoint.TOP_RIGHT.getPointY()-transY) / scaleY));
        Point bottomLeft = new Point(Math.round((CornerPoint.BOTTOM_LEFT.getPointX()-transX) / scaleX),Math.round((CornerPoint.BOTTOM_LEFT.getPointY()-transY) / scaleY));
        Point bottomRight = new Point(Math.round((CornerPoint.BOTTOM_RIGHT.getPointX()-transX) / scaleX),Math.round((CornerPoint.BOTTOM_RIGHT.getPointY()-transY) / scaleY));
        return new DocCoordinates(topLeft,topRight,bottomLeft,bottomRight);
    }

    private DocCoordinates getDocCoordinates(float left,float top){
        Point topLeft = new Point(Math.round((CornerPoint.TOP_LEFT.getPointX()-left) / scaleX),Math.round((CornerPoint.TOP_LEFT.getPointY()-top) / scaleY));
        Point topRight = new Point(Math.round((CornerPoint.TOP_RIGHT.getPointX()-left) / scaleX),Math.round((CornerPoint.TOP_RIGHT.getPointY()-top) / scaleY));
        Point bottomLeft = new Point(Math.round((CornerPoint.BOTTOM_LEFT.getPointX()-left) / scaleX),Math.round((CornerPoint.BOTTOM_LEFT.getPointY()-top) / scaleY));
        Point bottomRight = new Point(Math.round((CornerPoint.BOTTOM_RIGHT.getPointX()-left) / scaleX),Math.round((CornerPoint.BOTTOM_RIGHT.getPointY()-top) / scaleY));
        return new DocCoordinates(topLeft,topRight,bottomLeft,bottomRight);
    }

    public interface CroppedImageCall{
        void doCroppedImage(Bitmap croppedImage,DocCoordinates docCoordinates);
    }

    private double formatNumber(double value) {

        int DEFAULT_SCALE = 3;//默认保留三位
        int MAX_SCALE = 9;//最大保留9位
        String INTEGER_BIT_NOT_ZERO = "(0([.](0+|[0-9]{1,3}))?)|(-?[1-9][0-9]*([.][0-9]+)?)";//不包括整数位为0,小数位超过三位的正则
        String INTEGER_BIT_IS_ZERO = "0[.]0*[1-9][0-9]{0,2}";//获取需要保留小数位的精度的正则
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        int scale = DEFAULT_SCALE;
        if (!bigDecimal.toString().matches(INTEGER_BIT_NOT_ZERO)) {
            Matcher matcher = Pattern.compile(INTEGER_BIT_IS_ZERO).matcher(
                    bigDecimal.toString());
            if (matcher.find()) {
                scale = matcher.group().length() - 2;
            } else {
                scale = MAX_SCALE;//因为如果是科学计数的形式，这儿会匹配不成功，所以给个默认的精度
            }
        }
        return bigDecimal.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }
}
