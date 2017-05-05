package me.yluo.puretime;


import java.util.Date;

public class PlanData {
    public int id;
    public Date date;
    public String name;
    public float cost;
    public String note;
    public int comment = -1;

    public PlanData(int id, Date date, String name, float cost, int comment, String note) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.cost = cost;
        this.note = note;
        this.comment = comment;
    }
}
