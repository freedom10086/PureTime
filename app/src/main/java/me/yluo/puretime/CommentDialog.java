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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 评分dialog
 */
public class CommentDialog extends DialogFragment {
    private OnStarResultListener l;
    private int currentStar = 0;
    private int currentId;

    public static CommentDialog newInstance(int id, int value, OnStarResultListener l) {
        CommentDialog frag = new CommentDialog();
        frag.setListner(l);
        frag.setCurrentId(id);
        frag.currentStar = value;
        return frag;
    }


    private void setListner(OnStarResultListener listener) {
        this.l = listener;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_comment, null);
        final TextView t = (TextView) view.findViewById(R.id.label);
        t.setText(currentStar + " 分");
        CommentView c = (CommentView) view.findViewById(R.id.comment_view);
        c.setLevel(currentStar);
        c.setListener(new CommentView.OnValueChangeListener() {
            @Override
            public void onValueChange(int level) {
                t.setText(level + " 分");
                currentStar = level;
            }
        });

        view.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null)
                    l.result(currentId, currentStar);
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }


    public interface OnStarResultListener {
        void result(int id, int value);
    }
}
