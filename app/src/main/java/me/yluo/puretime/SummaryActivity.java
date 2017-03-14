package me.yluo.puretime;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        List<PieChartItem> datas = new ArrayList<>();
        datas.add(new PieChartItem("学习", 4, ContextCompat.getColor(this, R.color.colorStudy)));
        datas.add(new PieChartItem("锻炼", 1.5f, ContextCompat.getColor(this, R.color.colorExercise)));
        datas.add(new PieChartItem("画画", 2, ContextCompat.getColor(this, R.color.colorPaint)));
        datas.add(new PieChartItem("其余", 15.5f, Color.GRAY));


        PieChartView pieChatView = (PieChartView) findViewById(R.id.pie_chart2);
        pieChatView.setDatas("24h", datas);
    }
}
