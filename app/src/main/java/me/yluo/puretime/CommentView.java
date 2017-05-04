package me.yluo.puretime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class CommentView extends View {
    private int width;
    private RectF oval;
    private Rect starSrc, starSrcFill;
    private int radius;
    private Paint ovalPaint, starPaint;
    private int starWidth;
    private Bitmap star, starFill;

    private Rect starRect;
    //当前评分 0-10
    private int currentLevel = 0;
    private OnValueChangeListener listener;


    public CommentView(Context context) {
        super(context);
        init();
    }

    public CommentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(OnValueChangeListener listener) {
        this.listener = listener;
    }

    private void init() {
        ovalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ovalPaint.setColor(Color.WHITE);
        ovalPaint.setStrokeWidth(5);

        star = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        starFill = BitmapFactory.decodeResource(getResources(), R.drawable.star_fill);
        starSrc = new Rect(0, 0, star.getWidth(), star.getHeight());
        starSrcFill = new Rect(0, 0, starFill.getWidth(), starFill.getHeight());
        starRect = new Rect();

        starPaint = new Paint();
        starPaint.setAntiAlias(true);
        starPaint.setFilterBitmap(true);
        starPaint.setDither(true);
    }


    public void setLevel(int level) {
        if (level < 0) level = 0;
        else if (level > 10) level = 10;

        if (level != currentLevel) {
            currentLevel = level;
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
        radius = (int) (width * 0.75f * 0.5f);
        oval = new RectF(-radius, -radius, radius, radius);
        starWidth = (int) (width * 0.1f);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = event.getX();
        float y = event.getY();
        boolean isok = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isok = false;
                if ((Math.abs(x - width / 2) > 0.3 * width) || (Math.abs(y - width / 2) > 0.3 * width)) {
                    isok = true;
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isok && (Math.abs(x - width / 2) > 0.3 * width) || (Math.abs(y - width / 2) > 0.3 * width)) {
                    float r = (float) Math.atan2(y - width / 2, x - width / 2);
                    int angle = (int) (r * 180 / Math.PI) + 90;
                    if (angle < 0) angle = angle + 360;
                    int level = Math.round(angle / 36f);
                    Log.d("=====", "level " + level);
                    if (level == 0) level = 10;
                    if (level != currentLevel && listener != null) {
                        listener.onValueChange(level);
                    }
                    setLevel(level);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //把坐标轴移到圆心好计算
        canvas.translate(width / 2, width / 2);

        ovalPaint.setStyle(Paint.Style.STROKE);
        canvas.drawOval(oval, ovalPaint); //外圈圆

        ovalPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(oval, -90, currentLevel * 36, true, ovalPaint);

        for (int i = 0; i < 10; i++) {
            int angle = -90 + i * 36;
            int cx = (int) ((0.0725f + 0.75f * 0.5f) * width * Math.cos(angle * Math.PI / 180));
            int cy = (int) ((0.0725f + 0.75f * 0.5f) * width * Math.sin(angle * Math.PI / 180));

            starRect.left = cx - starWidth / 2;
            starRect.right = cx + starWidth / 2;
            starRect.top = cy - starWidth / 2;
            starRect.bottom = cy + starWidth / 2;


            if (i == 0) {
                canvas.drawBitmap(currentLevel == 10 ? starFill : star,
                        currentLevel == 10 ? starSrcFill : starSrc, starRect, starPaint);
            } else {
                if (i <= currentLevel) {
                    canvas.drawBitmap(starFill, starSrcFill, starRect, starPaint);
                } else {
                    canvas.drawBitmap(star, starSrc, starRect, starPaint);
                }
            }
        }
    }


    interface OnValueChangeListener {
        void onValueChange(int level);
    }
}
