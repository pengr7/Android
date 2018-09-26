package com.example.administrator.gtd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class MyDB extends SQLiteOpenHelper {
    String tablename;
    MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        tablename = name;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "create table if not exists "+tablename+" (_id integer primary key autoincrement, title text, detail text, date text, time text, status text)";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void insert(String title, String detail, String date, String time, String status) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("detail", detail);
        values.put("date", date);
        values.put("time", time);
        values.put("status", status);
        database.insert(tablename, null, values);
        database.close();
    }
    public void delete(long id) {
        String key = ""+id;
        SQLiteDatabase database = getWritableDatabase();
        String[] args = {key};
        database.delete(tablename, "_id = ?", args);
        database.close();
    }
    public void update(long id, String title, String detail, String date, String time, String status) {
        String key = ""+id;
        SQLiteDatabase database = getWritableDatabase();
        String[] args = {key};
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("detail", detail);
        values.put("date", date);
        values.put("time", time);
        values.put("status", status);
        database.update(tablename, values, "_id = ?", args);
        database.close();
    }
    public Cursor query() {
        SQLiteDatabase database = getReadableDatabase();
        String rawquery = "select * from "+tablename;
        return database.rawQuery(rawquery, null);
    }
    public Cursor query(long id) {
        SQLiteDatabase database = getReadableDatabase();
        String rawquery = "select * from "+tablename+" where _id = ?";
        String[] args = {""+id};
        return database.rawQuery(rawquery, args);
    }
}
