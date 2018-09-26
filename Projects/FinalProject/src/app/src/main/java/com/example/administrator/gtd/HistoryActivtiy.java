package com.example.administrator.gtd;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by selfishlover on 2016/12/13.
 */

public class HistoryActivtiy extends AppCompatActivity {
    MyDB helper;
    ListView listView;
    AlertDialog dialog, record;
    long id;
    TextView dateview, timeview, titleview, detailview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        helper = new MyDB(this, "finishedTask", null, 1);
        listView = (ListView)findViewById(R.id.listView);
        buildDialog();
        updateListview();
        setListener();
    }
    void updateListview() {
        Cursor cursor = helper.query();
        String[] keys = {"title", "date", "time", "status"};
        int[] ids = {R.id.itemtitle, R.id.itemdate, R.id.itemtime, R.id.itemstatus};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, keys, ids, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);
    }
    void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除记录");
        builder.setMessage("确定删除该记录？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                helper.delete(id);
                updateListview();
            }
        });
        dialog = builder.create();
        builder = new AlertDialog.Builder(this);
        builder.setTitle("任务详情");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        dateview = (TextView) view.findViewById(R.id.hisdate);
        timeview = (TextView) view.findViewById(R.id.histime);
        titleview = (TextView) view.findViewById(R.id.histitle);
        detailview = (TextView) view.findViewById(R.id.hisdetail);
        builder.setView(view);
        builder.setPositiveButton("知道了", null);
        record = builder.create();
    }
    void setListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                id = l;
                dialog.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = helper.query(l);
                if (cursor.moveToFirst()) {
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String detail = cursor.getString(cursor.getColumnIndex("detail"));
                    dateview.setText(date);
                    timeview.setText(time);
                    titleview.setText(title);
                    detailview.setText(detail);
                }
                cursor.close();
                record.show();
            }
        });
    }
}
