package com.example.administrator.gtd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by selfishlover on 2016/12/12.
 */

public class ModifyActivity extends AppCompatActivity {
    TextView titlebar;
    ImageView save;
    EditText taskname, taskdetail;
    DatePicker datePicker;
    TimePicker timePicker;
    String currentdate, currenttime, status;
    int y,m,d,h,mi;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    MyDB helper;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);
        findAllView();
        helper = new MyDB(this, "taskTable", null, 1);
        initUI();
        setListener();
    }
    void findAllView() {
        titlebar = (TextView)findViewById(R.id.titlebar);
        save = (ImageView) findViewById(R.id.save);
        taskname = (EditText) findViewById(R.id.taskname);
        taskdetail = (EditText) findViewById(R.id.detail);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
    }
    void initUI() {
        titlebar.setText("Modify the Task");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getLong("id");
        Cursor cursor = helper.query(id);
        if (cursor.moveToFirst()) {
            String itemname = cursor.getString(cursor.getColumnIndex("title"));
            String itemdetail = cursor.getString(cursor.getColumnIndex("detail"));
            currentdate = cursor.getString(cursor.getColumnIndex("date"));
            currenttime = cursor.getString(cursor.getColumnIndex("time"));
            status = cursor.getString(cursor.getColumnIndex("status"));
            taskname.setText(itemname);
            taskdetail.setText(itemdetail);
            String[] temp = currentdate.split("/");
            year = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]);
            day = Integer.parseInt(temp[2]);
            datePicker.init(year, month-1, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    currentdate = i+"/"+(i1+1)+"/"+i2;
                }
            });
            String[] temp1 = currenttime.split(":");
            hour = Integer.parseInt(temp1[0]);
            minute = Integer.parseInt(temp1[1]);
            //timePicker.setHour(hour);
            //timePicker.setMinute(minute);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
        cursor.close();
    }
    void setAlarm() {
        Intent intent = new Intent(ModifyActivity.this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(
                ModifyActivity.this, y*10000+m*1000+d*100+h*10+mi, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DAY_OF_MONTH, d);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, mi);
        calendar.set(Calendar.SECOND, 1);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        Intent intent1 = new Intent(ModifyActivity.this,
                AlarmReceiver.class);
        PendingIntent sender1 = PendingIntent.getBroadcast(
                ModifyActivity.this, year*10000+(month-1)*1000+day*100+hour*10+minute, intent1, 0);
        AlarmManager am1 = (AlarmManager) getSystemService(ALARM_SERVICE);
        am1.cancel(sender1);
    }
    void setListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = taskname.getText().toString();
                String detail = taskdetail.getText().toString();
                if (taskname.getText().toString().isEmpty()) {
                    Toast.makeText(ModifyActivity.this, "No Empty Task Name!" ,Toast.LENGTH_SHORT).show();
                } else {
                    helper.update(id, title, detail, currentdate, currenttime, status);
                    setAlarm();
                    finish();
                }
            }
        });
        Calendar calendar = Calendar.getInstance();
        int curyear = calendar.get(Calendar.YEAR);
        int curmonth = calendar.get(Calendar.MONTH)+1;
        int curday = calendar.get(Calendar.DAY_OF_MONTH);
        int curhour = calendar.get(Calendar.HOUR_OF_DAY);
        int curminute = calendar.get(Calendar.MINUTE);
        y = curyear;
        m = curmonth - 1;
        d = curday;
        currentdate = curyear+"/"+curmonth+"/"+curday;
        currenttime = String.format("%02d:%02d", curhour, curminute);
        datePicker.init(curyear, curmonth-1, curday, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                currentdate = year+"/"+(month+1)+"/"+day;
                y = year;
                m = month;
                d = day;
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                currenttime = String.format("%02d:%02d", hour, minute);
                h = hour;
                mi  = minute;
            }
        });
    }
}
