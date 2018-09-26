package com.example.myapplication8;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MyDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "db_name";
    private static final String TABLE_NAME = "table_name";
    private static final int DB_VERSION = 1;

    MyDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + TABLE_NAME
                + " (_id integer primary key, name text, birth text, gift text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // insert into table_name(<name>, <birth>, <gift>) values(<_name>, <_birth>, <_gift>)
    void insert(String name, String birth, String gift) {
        getWritableDatabase().execSQL("insert into " + TABLE_NAME
                + "(name, birth, gift) values('"
                + name + "', '" + birth + "', '" + gift + "')");
    }

    // update table_name set birth = '_birth', gift = '_gift' where name = '_name'
    void update(String name, String birth, String gift) {
        getWritableDatabase().execSQL("update " + TABLE_NAME
                + " set birth = '" + birth + "', gift = '" + gift
                + "' where name = '" + name + "'");
    }

    // delete from table_name where name = '_name'
    void delete(String name) {
        getWritableDatabase().execSQL("delete from " + TABLE_NAME
                + " where name = '" + name + "'");
    }

    // select * from table_name where name = '_name'
    Cursor select(String name) {
        return getReadableDatabase().rawQuery("select * from " + TABLE_NAME
                + " where name = '" + name + "'", null);
    }

    // select * from table_name
    Cursor getCursor() {
        return getReadableDatabase().rawQuery("select * from " + TABLE_NAME, null);
    }
}
