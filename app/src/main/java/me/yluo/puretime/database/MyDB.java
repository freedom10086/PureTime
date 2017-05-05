package me.yluo.puretime.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static String getDateStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        return format.format(date);
    }

    public void insertPlan(PlanData planData) {
        getDb();
        String sql = "INSERT INTO " + TABLE_NAME + " (time,name,cost,note) VALUES(?,?,?,?)";
        Log.d("db", getDateStr(planData.date));
        Object args[] = new Object[]{getDateStr(planData.date), planData.name, planData.cost, planData.note};
        this.db.execSQL(sql, args);
        this.db.close();
        Log.d("db", "insert");
    }

    public void updateStar(int id, int value) {
        getDb();
        String sql = "UPDATE " + TABLE_NAME + " set comment = ? where id = ?";
        Object args[] = new Object[]{value, id};
        this.db.execSQL(sql, args);
        this.db.close();
        Log.d("db", "updateStar,id:" + id + " value:" + value);
    }


    public PlanData getPlan(int id) {
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


    public List<PlanData> getPlans(Date date) {
        Log.d("db", getDateStr(date));
        getDb();
        String sql = "SELECT id,name,cost,comment,note from " + TABLE_NAME + " where time = ?";
        String args[] = new String[]{getDateStr(date)};
        Cursor result = this.db.rawQuery(sql, args);
        List<PlanData> datas = new ArrayList<>();
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            Log.d("db", getDateStr(date) + "|" + result.getString(1) + "|" + result.getFloat(2) + "|" + result.getInt(3));
            datas.add(new PlanData(
                    result.getInt(0),
                    date,
                    result.getString(1),
                    result.getFloat(2),
                    result.getInt(3),
                    result.getString(4)));
        }
        result.close();
        this.db.close();
        return datas;
    }

    public void delePlan(int id) {
        getDb();
        String sql = "DELETE FROM " + TABLE_NAME + " where id = ?";
        Object args[] = new Object[]{id};
        this.db.execSQL(sql, args);
        this.db.close();
    }

}