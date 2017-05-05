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
                + "id INTEGER PRIMARY KEY autoincrement,"
                + "time date NOT NULL," //日期
                + "name varchar(15) NOT NULL,"//事情
                + "cost real NOT NULL,"//花费时间
                + "comment int,"//打分
                + "note VARCHAR(50)"//备注
                + ")";
        db.execSQL(sql);
        Log.e("DATABASE", "数据表创建成功");
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + MyDB.TABLE_NAME;
        db.execSQL(sql);
    }

}