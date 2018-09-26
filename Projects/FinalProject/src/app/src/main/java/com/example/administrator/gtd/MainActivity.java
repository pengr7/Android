package com.example.administrator.gtd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    MyDB helper, finished;
    long curid;
    AlertDialog dialog;
    String currentdate, currenttime;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    SensorManager manager;
    Sensor accelerometer;
    List<Long> idList = new ArrayList<>();
    int index = 0;
    SensorEventListener accelerateListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            for (float value : sensorEvent.values)
                if (value < -20 || value > 20) {
                    Toast.makeText(MainActivity.this, "桌面组件已更新任务", Toast.LENGTH_SHORT).show();
                    index++;
                    index = index % idList.size();
                    Bundle bundle = new Bundle();
                    bundle.putLong("id", idList.get(index));
                    Intent intent = new Intent("selfishlover.final.updateWidget");
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                    return;
                }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helper = new MyDB(this, "taskTable", null, 1);
        finished = new MyDB(this, "finishedTask", null, 1);
        setListener();
        setListView();
        buildAlertdialog();
        getSensor();
        registerListener();
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterListener();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateListview();
        registerListener();
    }

    void setListener() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton showhistory = (FloatingActionButton)findViewById(R.id.showhistory);
        showhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivtiy.class);
                startActivity(intent);
            }
        });
    }
    void setListView() {
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                curid = l;
                /*TextView temp = (TextView) view.findViewById(R.id.itemstatus);
                String temp1 = temp.getText().toString();
                if (temp1.equals("Unfinished")) curstatus = false;
                else curstatus = true;*/
                dialog.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putLong("id", l);
                Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    void updateListview() {
        Cursor cursor = helper.query();
        idList.clear();
        if (cursor.moveToFirst()) {
            long temp = cursor.getLong(cursor.getColumnIndex("_id"));
            idList.add(temp);
        }
        while (cursor.moveToNext()) {
            long temp = cursor.getLong(cursor.getColumnIndex("_id"));
            idList.add(temp);
        }
        String[] keys = {"title", "date", "time", "status"};
        int[] ids = {R.id.itemtitle, R.id.itemdate, R.id.itemtime, R.id.itemstatus};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, keys, ids, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);
    }
    void buildAlertdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑条目");
        builder.setMessage("删除该项还是标志为达成？");
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteItem(false);
            }
        });
        builder.setPositiveButton("达成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteItem(true);
            }
        });
        dialog = builder.create();
    }
    void deleteItem(boolean flag) {
        Cursor cursor = helper.query(curid);
        String itemname = "", itemdetail = "";
        if (cursor.moveToFirst()) {
            itemname = cursor.getString(cursor.getColumnIndex("title"));
            itemdetail = cursor.getString(cursor.getColumnIndex("detail"));
            currentdate = cursor.getString(cursor.getColumnIndex("date"));
            currenttime = cursor.getString(cursor.getColumnIndex("time"));
            String[] temp = currentdate.split("/");
            year = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]) - 1;
            day = Integer.parseInt(temp[2]);
            String[] temp1 = currenttime.split(":");
            hour = Integer.parseInt(temp1[0]);
            minute = Integer.parseInt(temp1[1]);
        }
        cursor.close();
        helper.delete(curid);
        if (flag) finished.insert(itemname, itemdetail, currentdate, currenttime, "已完成");
        updateListview();
        Intent intent = new Intent(MainActivity.this,
                AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(
                MainActivity.this, year*10000+month*1000+day*100+hour*10+minute, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }
    void getSensor() {
        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    void registerListener() {
        manager.registerListener(accelerateListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }
    void unregisterListener() {
        manager.unregisterListener(accelerateListener);
    }
}
