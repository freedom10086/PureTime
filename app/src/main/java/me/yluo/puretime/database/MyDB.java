package me.yluo.puretime.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.yluo.puretime.PlanData;


public class MyDB {
    private Context context;

    static final String TABLE_NAME = "pure_time_plan";
    private SQLiteDatabase db = null;    //数据库操作


    //构造函数
    public MyDB(Context context) {
        this.context = context;
        this.db = new SQLiteHelper(context).getWritableDatabase();
    }

    private SQLiteDatabase getDb() {
        if (this.db == null || !this.db.isOpen()) {
            this.db = new SQLiteHelper(context).getWritableDatabase();
        }
        return this.db;
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        return format.format(curDate);
    }

    private Date getDate(String str) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.parse(str);
    }

    public void insertPlan(PlanData planData) {
        getDb();
        String sql = "INSERT INTO " + TABLE_NAME + " (tid,title,author,read_time)"
                + " VALUES(?,?,?,?)";
        String time = getTime();
        //Object args[] = new Object[]{tid, title, author, read_time_str};
        //this.db.execSQL(sql, args);
        this.db.close();
    }


    private PlanData getPlan(String id) {
        getDb();
        String sql = "SELECT tid from " + TABLE_NAME + " where tid = ?";
        //String args[] = new String[]{String.valueOf(tid)};
        //Cursor result = db.rawQuery(sql, args);
        //int count = result.getCount();
        //result.close();
        this.db.close();
        //return count != 0;
        return null;
    }


    private List<PlanData> getPlans() {
        getDb();
        String sql = "SELECT tid from " + TABLE_NAME + " where tid = ?";
        //String args[] = new String[]{String.valueOf(tid)};
        //Cursor result = db.rawQuery(sql, args);
        //int count = result.getCount();
        //result.close();
        this.db.close();
        //return count != 0;
        return null;
    }

    public void delePlan(int id) {
        getDb();
        String sql = "DELETE FROM " + TABLE_NAME;
        this.db.execSQL(sql);
        this.db.close();
    }

}