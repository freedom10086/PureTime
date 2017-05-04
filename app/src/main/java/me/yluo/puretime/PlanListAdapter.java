package me.yluo.puretime;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlanListAdapter extends BaseAdapter {

    private List<PlanData> planDatas;
    private Context context;
    private LayoutInflater mInflater;


    public PlanListAdapter(Context context, List<PlanData> planDatas) {
        this.planDatas = planDatas;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return planDatas.size() == 0 ? planDatas.size() : planDatas.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return planDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position != getCount() - 1) {
            convertView = mInflater.inflate(R.layout.item_main, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            TextView note = (TextView) convertView.findViewById(R.id.note);
            ImageView star = (ImageView) convertView.findViewById(R.id.star);
        } else {
            convertView = mInflater.inflate(R.layout.item_main_comment, null);
        }
        return convertView;
    }


}
