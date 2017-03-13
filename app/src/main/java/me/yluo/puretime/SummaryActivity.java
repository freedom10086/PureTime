package me.yluo.puretime;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SummaryActivity extends AppCompatActivity{


    private Typeface mTfLight,mTfRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        //mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");



        List<PieChartItem> datas = new ArrayList<>();
        datas.add(new PieChartItem("学习",4,Color.CYAN));
        datas.add(new PieChartItem("锻炼",1.5f,Color.BLUE));
        datas.add(new PieChartItem("画画",2,Color.RED));
        datas.add(new PieChartItem("其余",15.5f,Color.GRAY));


        PieChartView pieChatView = (PieChartView) findViewById(R.id.pie_chart2);
        pieChatView.setDatas("24h",datas);
    }
}
