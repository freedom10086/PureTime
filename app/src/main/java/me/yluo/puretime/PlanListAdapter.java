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
        return planDatas.size();
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_main, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.note = (TextView) convertView.findViewById(R.id.note);
            holder.star = (ImageView) convertView.findViewById(R.id.star);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //set data here
        return convertView;
    }


    private class ViewHolder {
        TextView name;
        TextView time;
        TextView note;
        ImageView star;
    }
}
