package me.yluo.puretime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
        , AddActivityDialog.AddActivityListener {
    private DrawerLayout drawer;
    private ListView planList;
    private PlanListAdapter adapter;
    private List<PlanData> planDatas = new ArrayList<>();
    private View dateView, holderView, halfImag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        planList = (ListView) findViewById(R.id.list);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        dateView = findViewById(R.id.date_container);
        holderView = findViewById(R.id.holder_view);
        halfImag = findViewById(R.id.half_img);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        findViewById(R.id.btn_drawer).setOnClickListener(this);
        findViewById(R.id.btn_sum).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);

        navigationView.getHeaderView(0)
                .findViewById(R.id.nav_img).setOnClickListener(this);

        for (int i = 0; i < 10; i++) {
            planDatas.add(new PlanData());
        }

        adapter = new PlanListAdapter(this, planDatas);
        planList.setAdapter(adapter);

        dateView.setVisibility(View.GONE);
        planList.setVisibility(View.GONE);
        holderView.setVisibility(View.VISIBLE);
        halfImag.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.nav_advice:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.nav_sync:
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("同步");
                dialog.setMessage("正在和云端同步......");
                drawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                }, 200);
                break;
            case R.id.nav_exit:
                // TODO: 2017/5/4
                CommentDialog.newInstance(null)
                        .show(getFragmentManager(), "comment");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_drawer:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_sum:
                startActivity(new Intent(this, SummaryActivity.class));
                break;
            case R.id.nav_img:
                startActivity(new Intent(this, UserCenterActivity.class));
                break;
            case R.id.btn_add:
                AddActivityDialog.newInstance(MainActivity.this)
                        .show(getFragmentManager(), "addActivity");
                break;
        }
    }

    @Override
    public void OnAddFriendOkClick(String mes, String uid) {

    }
}
