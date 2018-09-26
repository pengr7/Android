package com.example.myapplication6;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import java.text.SimpleDateFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    MusicService ms = new MusicService();
    ImageView imageView;
    TextView textView;
    TextView current;
    TextView total;
    SeekBar seekBar;
    Button play;
    Button stop;
    Button quit;

    void find() {
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        current = (TextView) findViewById(R.id.current);
        total = (TextView) findViewById(R.id.total);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        quit = (Button) findViewById(R.id.quit);
    }

    void rotate(boolean state) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        animation.setInterpolator(linearInterpolator);
        if (state)
            imageView.startAnimation(animation);
        else
            imageView.clearAnimation();
    }

    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            current.setText(time.format(ms.mp.getCurrentPosition()));
            total.setText(time.format(ms.mp.getDuration()));
            seekBar.setProgress(ms.mp.getCurrentPosition());
            seekBar.setMax(ms.mp.getDuration());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b)
                        ms.mp.seekTo(seekBar.getProgress());
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ms = ((MusicService.MyBinder)iBinder).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        find();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
        mHandler.post(mRunnable);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ms.mp.isPlaying()) {
                    play.setText("play");
                    textView.setText("Paused");
                    rotate(false);
                } else {
                    play.setText("pause");
                    textView.setText("Playing");
                    rotate(true);
                }
                ms.play();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setText("play");
                textView.setText("Stopped");
                rotate(false);
                ms.stop();
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mRunnable);
                unbindService(sc);
                MainActivity.this.finish();
                System.exit(0);
            }
        });
    }
}
