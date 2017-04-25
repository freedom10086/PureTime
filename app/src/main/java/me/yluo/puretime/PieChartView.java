package me.yluo.puretime;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {

    private int sum = 0;
    private Paint mPaint;//饼状画笔
    private TextPaint mTextPaint; // 文字画笔
    private Paint pointPaint;
    private static final float GAP = 1.2f;
    private float ringwidth;
    private int dp_5 = 5;
    private float animatedValue;
    private String centerTitle;
    private List<PieChartItem> datas = new ArrayList<>();
    private float radius = 0;

    private int width, height;
    private RectF oval;
    private int ovalBgColor = 0xFF636D77;

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mTextPaint = new TextPaint();

        dp_5 = dp2px(5);
        ringwidth = dp_5 * 4;

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(ringwidth);
        mPaint.setColor(0xFF636D77);

        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);

    }

    public PieChartView(Context context) {
        this(context, null, 0);

    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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
            height = Math.max(minHeight, height);
        }

        setMeasuredDimension(width, height);
        radius = Math.min((width - getPaddingStart() - getPaddingEnd()) / 3,
                (height - getPaddingTop() - getPaddingBottom()) / 2) - ringwidth / 2;
        oval = new RectF(-radius, -radius, radius, radius);
    }

    @Override
    protected void onDraw(Canvas mCanvas) {
        super.onDraw(mCanvas);
        //把坐标轴移到圆心好计算
        mCanvas.translate(width / 3, height / 2);
        paintPie(mCanvas);
    }


    private void paintPie(final Canvas mCanvas) {
        if (datas.size() == 0) return;
        float currentAngle = -90.0f;

        //mPaint.setColor(ovalBgColor);
        //mCanvas.drawArc(oval, 0, 360, false, mPaint);

        mTextPaint.setTextSize(radius * 0.65f);
        Rect rect = new Rect();
        mTextPaint.getTextBounds(centerTitle, 0, centerTitle.length(), rect);
        mCanvas.drawText(centerTitle, 0, rect.height() / 2, mTextPaint);

        for (int i = 0; i < datas.size(); i++) {
            if (currentAngle >= animatedValue) return;

            PieChartItem item = datas.get(i);
            float needDrawAngle = item.value / sum * 360;
            String lable = item.value + "h";

            float textAngle = -90f;
            for (int j = 0; j < i; j++) {
                textAngle += datas.get(j).value / sum * 360;
            }
            textAngle += datas.get(i).value / sum * 180;

            float toangle = needDrawAngle + currentAngle;

            mPaint.setColor(item.color);
            mCanvas.drawArc(oval, currentAngle + GAP, Math.min(toangle, animatedValue) - currentAngle - GAP, false, mPaint);
            currentAngle = Math.min(toangle, animatedValue);

            if (currentAngle >= textAngle) {
                drawText(mCanvas, textAngle, lable, needDrawAngle);
                drawLegend(i, item.lable, mCanvas, item.color);
            }

        }

    }

    //画文字
    private void drawText(Canvas mCanvas, float textAngle, String kinds, float needDrawAngle) {
        Rect rect = new Rect();
        mTextPaint.setTextSize(sp2px(13));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.getTextBounds(kinds, 0, kinds.length(), rect);
        mCanvas.drawText(kinds, (float) (radius  * Math.cos(Math.toRadians(textAngle))),
                (float) (radius * Math.sin(Math.toRadians(textAngle)) + rect.height() / 2),
                mTextPaint);
    }

    private void drawLegend(int i, String lable, Canvas canvas, int color) {
        float r = dp_5 * 0.8f;
        float startx = width / 3 + dp_5 * 4;
        float starty = -radius + i * dp_5 * 5 + (dp_5 * 5.0f) / 2;

        pointPaint.setColor(color);
        canvas.drawCircle(startx + r, starty, r, pointPaint);

        Rect rect = new Rect();
        mTextPaint.setTextSize(sp2px(16));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.getTextBounds(lable, 0, lable.length(), rect);
        canvas.drawText(lable, startx + dp_5 * 6, starty + rect.height() / 2, mTextPaint);
    }

    public void setDatas(String title, List<PieChartItem> datas) {
        this.datas = datas;
        if (datas != null && datas.size() > 0) {
            initAnimator();
            sum = 0;
            for (PieChartItem item : datas) {
                sum += item.value;
            }
        }
        this.centerTitle = title;
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
