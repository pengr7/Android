package com.example.administrator.gtd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class AddActivity extends AppCompatActivity {
    ImageView save;
    EditText taskname, taskdetail;
    DatePicker datePicker;
    TimePicker timePicker;
    String currentdate, currenttime;
    int y,m,d,h,mi;
    MyDB helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);
        findAllView();
        helper = new MyDB(this, "taskTable", null, 1);
        setListener();
    }
    void findAllView() {
        save = (ImageView) findViewById(R.id.save);
        taskname = (EditText) findViewById(R.id.taskname);
        taskdetail = (EditText) findViewById(R.id.detail);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
    }
    void setAlarm() {
        Intent intent = new Intent(AddActivity.this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(
                AddActivity.this, y*10000+m*1000+d*100+h*10+mi, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DAY_OF_MONTH, d);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, mi);
        calendar.set(Calendar.SECOND, 1);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }
    void setListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = taskname.getText().toString();
                String detail = taskdetail.getText().toString();
                if (taskname.getText().toString().isEmpty()) {
                    Toast.makeText(AddActivity.this, "No Empty Task Name!" ,Toast.LENGTH_SHORT).show();
                } else {
                    helper.insert(title, detail, currentdate, currenttime, "未完成");
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
