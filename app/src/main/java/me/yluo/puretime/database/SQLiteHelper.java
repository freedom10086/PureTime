package me.yluo.puretime.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pure_time.db";
    private static final int DATABASE_VERSION = 1;


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);//继承父类
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + MyDB.TABLE_NAME + "("
                + "id int primary key AUTO_INCREMENT,"
                + "cateId int NOT NULL,"
                + "name VARCHAR(15) NOT NULL,"
                + "time int NOT NULL,"
                + "note VARCHAR(50),"
                + ")";
        db.execSQL(sql);
        Log.e("DATABASE", "TABLE_READ_HISTORY数据表创建成功");
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + MyDB.TABLE_NAME;
        db.execSQL(sql);
    }

}