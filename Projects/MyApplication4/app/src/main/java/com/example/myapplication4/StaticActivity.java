package com.example.myapplication4;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StaticActivity extends ListActivity {
    private static final String STATICACTION = "com.example.myapplication4.StaticReceiver";
    int image[] = new int[] {R.drawable.apple, R.drawable.banana, R.drawable.cherry,
            R.drawable.coco, R.drawable.kiwi, R.drawable.orange, R.drawable.pear,
            R.drawable.strawberry, R.drawable.watermelon};
    String fruitName[] = new String[] {"Apple", "Banana", "Cherry", "Coco", "Kiwi",
            "Orange", "Pear", "Strawberry", "Watermelon"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Map<String, Object> > data = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("image", image[i]);
            temp.put("fruitName", fruitName[i]);
            data.add(temp);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.static_layout,
                new String[] {"image", "fruitName"}, new int[] {R.id.image, R.id.fruitName});
        setListAdapter(simpleAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Bundle bundle = new Bundle();
        bundle.putInt("image", image[position]);
        bundle.putString("fruitName", fruitName[position]);
        Intent intent = new Intent(STATICACTION);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }
}