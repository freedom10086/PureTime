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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {

    private int sum = 0;
    private Paint mPaint;//饼状画笔
    private TextPaint mTextPaint; // 文字画笔
    private static final float GAP = 1.2f;
    private float legendWidth;
    private float ringwidth;
    private int dp_5 = 5;
    private float animatedValue;
    private RectF oval, ovalInner;
    private String centerTitle;
    private List<PieChartItem> datas = new ArrayList<>();
    private float radius = 0;

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mTextPaint = new TextPaint();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);

        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        dp_5 = dp2px(5);

        ringwidth = dp_5 * 5;
        legendWidth = dp_5 * 8;
    }

    public PieChartView(Context context) {
        this(context, null, 0);

    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wideSize = getWidth();
        int heightSize = getHeight();

        radius = Math.min(wideSize - legendWidth*2, heightSize) / 2;

        oval = new RectF(-radius - legendWidth, -radius, radius - legendWidth, radius);
        ovalInner = new RectF(-radius - legendWidth + ringwidth, -radius + ringwidth, radius - legendWidth - ringwidth, radius - ringwidth);
    }

    @Override
    protected void onDraw(Canvas mCanvas) {
        super.onDraw(mCanvas);
        mCanvas.translate((getWidth() + getPaddingLeft() - getPaddingRight()) / 2, (getHeight() + getPaddingTop() - getPaddingBottom()) / 2);
        paintPie(mCanvas);
    }


    private void paintPie(final Canvas mCanvas) {
        if (datas.size() == 0) return;
        float currentAngle = -90.0f;

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
            mCanvas.drawArc(oval, currentAngle + GAP, Math.min(toangle, animatedValue) - currentAngle - GAP, true, mPaint);

            mPaint.setColor(Color.WHITE);
            mCanvas.drawArc(ovalInner, currentAngle - GAP, animatedValue - currentAngle + 2 * GAP, true, mPaint);

            currentAngle = Math.min(toangle, animatedValue);

            if (currentAngle >= textAngle) {
                drawText(mCanvas, textAngle, lable, needDrawAngle);
                mPaint.setColor(item.color);
                drawLegend(i, item.lable, mCanvas, mPaint);
            }

        }

        drawCenterText(mCanvas, centerTitle, 0 - legendWidth, 0);
    }

    //画中间文字标题
    private void drawCenterText(Canvas mCanvas, String text, float x, float y) {
        Rect rect = new Rect();
        mTextPaint.setTextSize(legendWidth*1.3f);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        mCanvas.drawText(text, x, y + rect.height() / 2, mTextPaint);
    }

    //画文字
    private void drawText(Canvas mCanvas, float textAngle, String kinds, float needDrawAngle) {
        Rect rect = new Rect();
        mTextPaint.setTextSize(sp2px(13));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.getTextBounds(kinds, 0, kinds.length(), rect);
        mCanvas.drawText(kinds, (float) ((radius - ringwidth / 2) * Math.cos(Math.toRadians(textAngle)) - legendWidth),
                (float) ((radius - ringwidth / 2) * Math.sin(Math.toRadians(textAngle)) + rect.height() / 2),
                mTextPaint);
    }

    private void drawLegend(int i, String lable, Canvas canvas, Paint paint) {
        float starty = -radius+dp_5*4+i*dp_5*4;
        float startx = radius -legendWidth+ dp_5 * 4;

        canvas.drawRect(startx, starty, startx+dp_5*3, starty+dp_5*3, paint);

        Rect rect = new Rect();
        mTextPaint.setTextSize(sp2px(15));
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.getTextBounds(lable, 0, lable.length(), rect);
        canvas.drawText(lable,startx+dp_5*6,starty+rect.height()/2+dp_5*1.3f,mTextPaint);

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
