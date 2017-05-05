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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by free2 on 16-3-14.
 * 添加好友diaog
 */
public class AddActivityDialog extends DialogFragment {
    private AddActivityListener l;
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
        this.l = listener;
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

        final EditText name = (EditText) view.findViewById(R.id.name);
        final EditText note = (EditText) view.findViewById(R.id.note);

        activityGrid = (GridView) view.findViewById(R.id.activity_grid);
        activityGrid.setAdapter(adapter);
        activityGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CheckBox) view).setChecked(!((CheckBox) view).isChecked());
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("=====","============");

                if (activityGrid.getSelectedItem() == null && TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(getActivity(), "请选择任务", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nameStr = name.getText().toString();
                String noteStr = note.getText().toString();
                if (l != null)
                    l.onAddActivity(new PlanData(
                            0,
                            new Date(),
                            nameStr,
                            4.5f,
                            0,
                            noteStr));
                dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view);

        Dialog dialog = builder.create();

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


    public interface AddActivityListener {
        void onAddActivity(PlanData planData);
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
