package com.example.myapplication7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final EditText newPassword = (EditText) findViewById(R.id.newPassword);
        final EditText confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        Button ok = (Button) findViewById(R.id.ok);
        Button clear = (Button) findViewById(R.id.clear);

        final boolean first = sharedPreferences.getBoolean("first", true);
        if (!first)
            newPassword.setVisibility(View.INVISIBLE);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (first) {
                    if (newPassword.getText().length() == 0)
                        Toast.makeText(MainActivity.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    else if (!Objects.equals(newPassword.getText().toString(), confirmPassword.getText().toString()))
                        Toast.makeText(MainActivity.this, "Password Mismatch.", Toast.LENGTH_SHORT).show();
                    else {
                        editor.putString("password", confirmPassword.getText().toString());
                        editor.putBoolean("first", false);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, FileEditorActivity.class);
                        startActivity(intent);
                    }
                } else {
                    if (!Objects.equals(confirmPassword.getText().toString(), sharedPreferences.getString("password", "")))
                        Toast.makeText(MainActivity.this, "Password Mismatch.", Toast.LENGTH_SHORT).show();
                    else {
                        Intent intent = new Intent(MainActivity.this, FileEditorActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPassword.setText("");
                confirmPassword.setText("");
            }
        });
    }
}
