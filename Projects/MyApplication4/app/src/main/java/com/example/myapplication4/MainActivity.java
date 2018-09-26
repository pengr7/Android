package com.example.myapplication4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button staticRegister = (Button) findViewById(R.id.staticRegister);
        Button dynamicRegister = (Button) findViewById(R.id.dynamicRegister);
        assert staticRegister != null;
        staticRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StaticActivity.class);
                startActivity(intent);
            }
        });
        assert dynamicRegister != null;
        dynamicRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DynamicActivity.class);
                startActivity(intent);
            }
        });
    }
}
