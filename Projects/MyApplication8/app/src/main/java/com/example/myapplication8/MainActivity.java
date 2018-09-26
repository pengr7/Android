package com.example.myapplication8;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    MyDB myDB = new MyDB(MainActivity.this);
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        Button add_entry = (Button) findViewById(R.id.add_entry);
        final ListView listView = (ListView) findViewById(R.id.listView);

        // set adapter
        simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.item,
                myDB.getCursor(),
                new String[] {"name", "birth", "gift"},
                new int[] {R.id.name, R.id.birth, R.id.gift},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(simpleCursorAdapter);

        // add entry
        add_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        // update entry
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // inflate
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View dialogView = layoutInflater.inflate(R.layout.dialoglayout, null);

                // find views
                TextView update_name = (TextView) dialogView.findViewById(R.id.update_name);
                final EditText update_birth
                        = (EditText) dialogView.findViewById(R.id.update_birth);
                final EditText update_gift
                        = (EditText) dialogView.findViewById(R.id.update_gift);
                TextView update_phone = (TextView) dialogView.findViewById(R.id.update_phone);

                // get name and phone number
                Cursor cursor = (Cursor) simpleCursorAdapter.getItem(i);
                final String name = cursor.getString(cursor.getColumnIndex("name"));
                update_name.setText(name);
                update_phone.setText(getPhoneNumber(name));

                // build alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView);
                builder.setTitle("O(∩_∩)O");
                builder.setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDB.update(name, update_birth.getText().toString(),
                                update_gift.getText().toString());
                        simpleCursorAdapter.getCursor().requery();
                    }
                });
                builder.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                           int i, long l) {
                // get name
                Cursor cursor = (Cursor) simpleCursorAdapter.getItem(i);
                final String name = cursor.getString(cursor.getColumnIndex("name"));

                // build dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("是否删除？");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDB.delete(name);
                        simpleCursorAdapter.getCursor().requery();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    // retrieve phone number from contacts list
    public String getPhoneNumber(String name) {
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = '" + name + "'",
                null,
                null);
        if (cursor.moveToFirst())
            return cursor.getString(0);
        else
            return "Unknown";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        simpleCursorAdapter.getCursor().requery();
    }
}
