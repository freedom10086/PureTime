package me.yluo.puretime;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView version = (TextView) findViewById(R.id.tv_version);
        TextView versionNew = (TextView) findViewById(R.id.tv_new_version);

        PackageInfo info = null;
        PackageManager manager = getPackageManager();
        try {
            info = manager.getPackageInfo(getPackageName(), 0);


        } catch (Exception e) {
            e.printStackTrace();
        }

        int versionCode = 0;
        if (info != null) {
            String version_name = info.versionName;
            versionCode = info.versionCode;
            String a = "V" + version_name;
            version.setText(a);
        }
    }
}
