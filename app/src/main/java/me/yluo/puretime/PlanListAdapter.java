package me.yluo.puretime;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.yluo.puretime.database.MyDB;

public class PlanListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<PlanData> planDatas;
    private LayoutInflater mInflater;


    public PlanListAdapter(Context context, List<PlanData> planDatas) {
        this.planDatas = planDatas;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setPlanDatas(List<PlanData> planDatas) {
        this.planDatas = planDatas;
        notifyDataSetChanged();
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
            View starBtn = convertView.findViewById(R.id.btn_star);

            final PlanData d = planDatas.get(position);
            name.setText(d.name);
            time.setText(String.valueOf(d.cost));
            note.setText(d.note);
            int imgRes;
            switch (d.comment) {
                case 1:
                    imgRes = R.drawable.p1;
                    break;
                case 2:
                    imgRes = R.drawable.p2;
                    break;
                case 3:
                    imgRes = R.drawable.p3;
                    break;
                //todo 缺少评分
                case 4:
                    imgRes = R.drawable.p3;
                    break;
                case 5:
                    imgRes = R.drawable.p6;
                    break;
                case 6:
                    imgRes = R.drawable.p6;
                    break;
                case 7:
                    imgRes = R.drawable.p7;
                    break;
                case 8:
                    imgRes = R.drawable.p8;
                    break;
                case 9:
                    imgRes = R.drawable.p9;
                    break;
                case 10:
                    imgRes = R.drawable.p10;
                    break;
                default:
                    imgRes = 0;
                    break;
            }

            if (imgRes == 0) {
                starBtn.setVisibility(View.VISIBLE);
                star.setVisibility(View.GONE);
                starBtn.setOnClickListener(this);
                starBtn.setTag(position);
            } else {
                starBtn.setVisibility(View.GONE);
                star.setVisibility(View.VISIBLE);
                star.setBackgroundResource(imgRes);
                star.setOnClickListener(this);
                star.setTag(position);
            }
        } else {
            convertView = mInflater.inflate(R.layout.item_main_comment, null);
        }
        return convertView;
    }


    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        final PlanData d = planDatas.get(pos);
        CommentDialog.newInstance(d.id, d.comment, new CommentDialog.OnStarResultListener() {
            @Override
            public void result(int id, int value) {
                //评分完成
                if (d.comment != value) {//更新评分
                    MyDB db = new MyDB(context);
                    d.comment = value;
                    db.updateStar(d.id, value);
                    notifyDataSetChanged();
                }
            }
        }).show(((Activity) context).getFragmentManager(), "comment");
    }
}
