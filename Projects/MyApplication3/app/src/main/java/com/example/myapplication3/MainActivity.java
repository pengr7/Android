package com.example.myapplication3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<Map<String, Object> > data = new ArrayList<>();
        String[] initial = new String[] {"A", "E", "D", "E", "F", "J", "I", "M", "J", "P"};
        String[] name = new String[] {"Aaron", "Elvis", "David", "Edwin", "Frank",
                "Joshua", "Ivan", "Mark", "Joseph", "Phoebe"};
        for (int i = 0; i < 10; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("initial", initial[i]);
            temp.put("name", name[i]);
            data.add(temp);
        }
        ListView listView = (ListView)findViewById(R.id.list_view);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.textview,
                new String[] {"initial", "name"}, new int[] {R.id.initial, R.id.name});
        assert listView != null;
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, detail.class);
                intent.putExtra("naming", (String) data.get(position).get("name"));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("删除联系人");
                builder.setMessage("确定删除联系人" + data.get(position).get("name") + "?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        data.remove(position);
                        simpleAdapter.notifyDataSetChanged();
                    }
                });
                builder.create();
                builder.show();
                return true;
            }
        });
    }
}
