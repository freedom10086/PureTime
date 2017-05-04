package me.yluo.puretime;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by free2 on 16-3-14.
 * 添加好友diaog
 */
public class AddActivityDialog extends DialogFragment {
    private AddActivityListener dialogListener;
    private GridView activityGrid;
    private List<String> datas;
    private MyAdapter adapter;
    private View btnClose, btnDone;

    public static AddActivityDialog newInstance(AddActivityListener l) {
        AddActivityDialog frag = new AddActivityDialog();
        frag.setListner(l);
        return frag;
    }


    private void setListner(AddActivityListener listener) {
        this.dialogListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datas = new ArrayList<>();
        datas.add("学习");
        datas.add("吃饭");
        datas.add("音乐");

        for (int i = 0; i < 5; i++) {
            datas.add("睡觉");
        }

        adapter = new MyAdapter(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_add_activity, null);
        btnClose = view.findViewById(R.id.btn_close);
        btnDone = view.findViewById(R.id.btn_done);
        activityGrid = (GridView) view.findViewById(R.id.activity_grid);
        activityGrid.setAdapter(adapter);
        activityGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CheckBox) view).setChecked(!((CheckBox) view).isChecked());
            }
        });

        builder.setView(view);

        Dialog dialog =   builder.create();

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = Util.dip2px(45);
        Animation a = new TranslateAnimation(0,-width,0,0);
        Animation a2 = new TranslateAnimation(0,width,0,0);
        a.setFillAfter(true);
        a2.setFillAfter(true);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a2.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setDuration(400);
        a2.setDuration(400);

        btnClose.setAnimation(a);
        btnDone.setAnimation(a2);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dialogListener = (AddActivityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    public interface AddActivityListener {
        void OnAddFriendOkClick(String mes, String uid);
    }

    private class MyAdapter extends ArrayAdapter<String> {
        private Context context;

        MyAdapter(Context context) {
            super(context, R.layout.item_user_info, datas);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_label, parent, false);
            ((CheckBox) rowView).setText(datas.get(position));
            return rowView;
        }
    }
}
