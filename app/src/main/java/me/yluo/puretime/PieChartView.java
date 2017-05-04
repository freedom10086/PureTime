package me.yluo.puretime;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.PathInterpolator;

import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {

    private Paint mPaint;//饼状画笔
    private Paint clockPaint;//背景
    private TextPaint mTextPaint; // 文字画笔
    private Paint pointPaint;
    private float ringwidth;
    private int dp_5 = 5;
    private float animatedValue;
    private float scoreValue;
    private List<PieChartItem> datas = new ArrayList<>();
    private float radius = 0;

    private int width, height;
    private RectF oval, newOval;
    private Rect clockRect;
    private Bitmap clockBg;

    public PieChartView(Context context) {
        this(context, null, 0);

    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mTextPaint = new TextPaint();

        dp_5 = dp2px(5);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(80);

        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);

        clockPaint = new Paint();
        clockPaint.setAntiAlias(true);
        clockPaint.setFilterBitmap(true);
        clockPaint.setDither(true);
        clockBg = BitmapFactory.decodeResource(getResources(), R.drawable.clock_bg);
        clockRect = new Rect(0, 0, clockBg.getWidth(), clockBg.getHeight());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);

        int minHeight = dp2px(250);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = Math.max(minHeight, heightSize);
        }

        setMeasuredDimension(width, height);
        radius = Math.min((width - getPaddingStart() - getPaddingEnd()) / 3,
                (height - getPaddingTop() - getPaddingBottom()) / 2) - ringwidth / 2;
        oval = new RectF(-radius, -radius, radius, radius);
        newOval = new RectF(-radius * 0.9f, -radius * 0.9f, radius * 0.9f, radius * 0.9f);
        ringwidth = radius * 0.2f;
        mPaint.setStrokeWidth(radius * 0.2f);
    }

    @Override
    protected void onDraw(Canvas mCanvas) {
        super.onDraw(mCanvas);
        //把坐标轴移到圆心好计算
        mCanvas.translate(width / 3, Math.min(height / 2, radius + dp_5 * 5));
        paintPie(mCanvas);
    }


    private void paintPie(final Canvas mCanvas) {
        if (datas.size() == 0) return;
        float currentAngle = -90.0f;

        //背景时钟
        mCanvas.drawBitmap(clockBg, clockRect, oval, clockPaint);

        for (int i = 0; i < datas.size(); i++) {
            if (currentAngle >= animatedValue) return;

            PieChartItem item = datas.get(i);
            int sum = 24;
            float needDrawAngle = item.value / sum * 360;
            String lable = item.value + "h";

            float textAngle = -90f;
            for (int j = 0; j < i; j++) {
                textAngle += datas.get(j).value / sum * 360;
            }
            textAngle += datas.get(i).value / sum * 180;

            float toangle = needDrawAngle + currentAngle;
            mPaint.setColor(item.color);
            mCanvas.drawArc(newOval,
                    currentAngle,
                    Math.min(toangle, animatedValue) - currentAngle,
                    false, mPaint);

            currentAngle = Math.min(toangle, animatedValue);
            if (currentAngle >= textAngle) {
                drawText(mCanvas, textAngle, lable);
                drawLegend(i, item.lable, mCanvas, item.color);
            }
        }

        //中间文字
        mTextPaint.setTextSize(radius * 0.4f);
        mCanvas.drawText("24h", 0, -8, mTextPaint);
        mTextPaint.setTextSize(radius * 0.2f);

        String s = "总评分:" + scoreValue;
        Rect rect = new Rect();
        mTextPaint.getTextBounds(s, 0, 7, rect);
        mCanvas.drawText(s.substring(0, 7), 0, rect.height() + 8, mTextPaint);
    }

    //画文字
    private void drawText(Canvas mCanvas, float textAngle, String kinds) {
        Rect rect = new Rect();
        mTextPaint.setTextSize(sp2px(13));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.getTextBounds(kinds, 0, kinds.length(), rect);
        mCanvas.drawText(kinds,
                (float) ((radius + ringwidth) * Math.cos(Math.toRadians(textAngle))),
                (float) ((radius + ringwidth) * Math.sin(Math.toRadians(textAngle)) + rect.height() / 2),
                mTextPaint);
    }

    //标注
    private void drawLegend(int i, String lable, Canvas canvas, int color) {
        float r = dp_5 * 0.8f;
        float startx = width / 3 + dp_5 * 4;
        float starty = -radius + i * dp_5 * 4 + (dp_5 * 5.0f) / 2;

        pointPaint.setColor(color);
        canvas.drawCircle(startx + r, starty + dp_5 / 5, r, pointPaint);

        Rect rect = new Rect();
        mTextPaint.setTextSize(sp2px(16));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.getTextBounds(lable, 0, lable.length(), rect);
        canvas.drawText(lable, startx + dp_5 * 6, starty + rect.height() / 2, mTextPaint);
    }

    public void setDatas(float soore, List<PieChartItem> datas) {
        this.datas = datas;
        if (datas != null && datas.size() > 0) {
            initAnimator();
        }
        this.scoreValue = soore;
    }


    private void initAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(-90, 270);
        anim.setDuration(1500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }

        });
        anim.start();
    }


    /**
     * dp 2 px
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

}
