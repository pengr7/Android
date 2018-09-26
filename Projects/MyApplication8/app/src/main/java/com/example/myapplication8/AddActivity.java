package com.example.myapplication8;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    MyDB myDB = new MyDB(AddActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_info);

        // find views
        final TextView new_name = (TextView) findViewById(R.id.new_name);
        final EditText new_birth = (EditText) findViewById(R.id.new_birth);
        final EditText new_gift = (EditText) findViewById(R.id.new_gift);
        Button add = (Button) findViewById(R.id.add);

        // add entry
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new_name.getText().toString().length() == 0)
                    Toast.makeText(AddActivity.this, "名字为空，请完善",
                            Toast.LENGTH_SHORT).show();
                else if (myDB.select(new_name.getText().toString()).getCount() > 0)
                    Toast.makeText(AddActivity.this, "名字重复啦，请完善",
                            Toast.LENGTH_SHORT).show();
                else {
                    myDB.insert(new_name.getText().toString(), new_birth.getText().toString(),
                            new_gift.getText().toString());
                    finish();
                }
            }
        });
    }
}
