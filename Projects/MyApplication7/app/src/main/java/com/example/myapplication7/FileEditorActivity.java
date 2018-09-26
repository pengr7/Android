package com.example.myapplication7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by 瑞 on 11/14/2016.
 */

public class FileEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editor);
        final EditText editText = (EditText) findViewById(R.id.editText);
        Button save = (Button) findViewById(R.id.save);
        Button load = (Button) findViewById(R.id.load);
        Button clear = (Button) findViewById(R.id.clear2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try (FileOutputStream fileOutputStream = openFileOutput("file", MODE_PRIVATE)) {
                    fileOutputStream.write(editText.getText().toString().getBytes());
                    Toast.makeText(FileEditorActivity.this, "Saved successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try (FileInputStream fileInputStream = openFileInput("file")) {
                    byte[] contents = new byte[fileInputStream.available()];
                    fileInputStream.read(contents);
                    editText.setText(new String(contents));
                    Toast.makeText(FileEditorActivity.this, "Load successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(FileEditorActivity.this, "Fail to load file.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
