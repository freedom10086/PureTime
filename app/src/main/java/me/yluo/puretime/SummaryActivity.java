package me.yluo.puretime;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CalendarView;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    private boolean isShow = true;
    private PieChartView pieChatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Util.regBackBtn(this);

        List<PieChartItem> datas = new ArrayList<>();
        datas.add(new PieChartItem("学习", 4, ContextCompat.getColor(this, R.color.colorStudy)));
        datas.add(new PieChartItem("锻炼", 1.5f, ContextCompat.getColor(this, R.color.colorExercise)));
        datas.add(new PieChartItem("画画", 2, ContextCompat.getColor(this, R.color.colorPaint)));

        pieChatView = (PieChartView) findViewById(R.id.pie_chart2);
        pieChatView.setDatas(5, datas);

        final CalendarView c = (CalendarView) findViewById(R.id.calendar);

        final TabLayout tab = (TabLayout) findViewById(R.id.tabs);
        tab.addTab(tab.newTab().setText("日"));
        tab.addTab(tab.newTab().setText("周"));
        tab.addTab(tab.newTab().setText("月"));
        Util.setUpIndicatorWidth(tab);


        findViewById(R.id.btn_cal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChatView.setEnabled(false);
                c.setVisibility(isShow ? View.GONE : View.VISIBLE);
                Animation a = new AlphaAnimation(0, 1);
                a.setDuration(400);
                pieChatView.setAnimation(a);
                isShow = !isShow;
            }
        });
    }
}
