package me.yluo.puretime;


import android.app.Activity;
import android.content.Context;
import android.view.View;

public class Util {

    public static void regBackBtn(final Activity a) {
        View b = a.findViewById(R.id.btn_back);
        if (b != null)
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a.finish();
                }
            });
    }
}
