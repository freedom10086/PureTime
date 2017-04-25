package me.yluo.puretime;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        ArrayList<Data> datas = new ArrayList<>();

        datas.add(new Data(R.drawable.icon_user, "昵称", "测试昵称"));
        datas.add(new Data(R.drawable.icon_sex, "性别", "男"));
        datas.add(new Data(R.drawable.icon_sign, "签名", "测试签名签名签名签名签名签名"));

        ListView l = (ListView) findViewById(R.id.list_view);
        l.setAdapter(new MyAdapter(datas));

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        Util.regBackBtn(this);
    }

    private class Data {
        int imgId;
        String key;
        String value;

        public Data(int imgId, String key, String value) {
            this.imgId = imgId;
            this.key = key;
            this.value = value;
        }
    }

    private class MyAdapter extends ArrayAdapter<Data> {
        final ArrayList<Data> datas;

        public MyAdapter(ArrayList<Data> ds) {
            super(UserCenterActivity.this, R.layout.item_user_info, ds);
            this.datas = ds;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_user_info, parent, false);

            ImageView imgLogo = (ImageView) rowView.findViewById(R.id.img_icon);
            TextView key = (TextView) rowView.findViewById(R.id.tv_key);
            TextView value = (TextView) rowView.findViewById(R.id.tv_value);

            imgLogo.setImageResource(datas.get(position).imgId);
            key.setText(datas.get(position).key);
            value.setText(datas.get(position).value);
            return rowView;
        }
    }
}
