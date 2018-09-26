package com.example.myapplication4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DynamicActivity extends AppCompatActivity {
    private static final String DYNAMICACTION = "com.example.myapplication4.DynamicReceiver";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_layout);
        final EditText message = (EditText) findViewById(R.id.message);
        final Button register = (Button) findViewById(R.id.register);
        Button send = (Button) findViewById(R.id.send);
        final DynamicReceiver dynamicReceiver = new DynamicReceiver();
        assert message != null;
        assert register != null;
        assert send != null;
        final int[] tag = {0};
        register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (tag[0] == 0) {
                    IntentFilter dynamic_filter = new IntentFilter();
                    dynamic_filter.addAction(DYNAMICACTION);
                    registerReceiver(dynamicReceiver, dynamic_filter);
                    register.setText("Unregister Broadcast");
                } else {
                    unregisterReceiver(dynamicReceiver);
                    register.setText("Register Broadcast");
                }
                tag[0] = 1 - tag[0];
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("message", message.getText().toString());
                Intent intent = new Intent(DYNAMICACTION);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        });
    }
}
