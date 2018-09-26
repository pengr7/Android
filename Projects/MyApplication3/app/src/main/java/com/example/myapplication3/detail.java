package com.example.myapplication3;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class detail extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        final int[] tag = {0};
        String naming = getIntent().getExtras().getString("naming");
        ImageView back = (ImageView) findViewById(R.id.back);
        TextView detailname = (TextView) findViewById(R.id.detailname);
        final ImageView star = (ImageView) findViewById(R.id.star);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView from = (TextView) findViewById(R.id.from);
        RelativeLayout detailtop = (RelativeLayout) findViewById(R.id.detailtop);
        assert back != null;
        assert detailname != null;
        assert star != null;
        assert phone != null;
        assert from != null;
        assert detailtop != null;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag[0] = 1 - tag[0];
                if (tag[0] == 0) {
                    star.setImageResource(R.drawable.full_star);
                } else {
                    star.setImageResource(R.drawable.empty_star);
                }
            }
        });
        if (Objects.equals(naming, "Aaron")) {
            phone.setText("17715523654");
            detailname.setText("Aaron");
            from.setText("手机 江苏苏州电信");
            detailtop.setBackgroundColor(Color.parseColor("#BB4C3B"));
        }
        if (Objects.equals(naming, "Elvis")) {
            phone.setText("18825653224");
            detailname.setText("Elvis");
            from.setText("手机 广东揭阳移动");
            detailtop.setBackgroundColor(Color.parseColor("#c48d30"));
        }
        if (Objects.equals(naming, "David")) {
            phone.setText("15052116654");
            detailname.setText("David");
            from.setText("手机 江苏无锡移动");
            detailtop.setBackgroundColor(Color.parseColor("#4469b0"));
        }
        if (Objects.equals(naming, "Edwin")) {
            phone.setText("18854211875");
            detailname.setText("Edvin");
            from.setText("手机 山东青岛移动");
            detailtop.setBackgroundColor(Color.parseColor("#20A17B"));
        }
        if (Objects.equals(naming, "Frank")) {
            phone.setText("13955188541");
            detailname.setText("Frank");
            from.setText("手机 安徽合肥移动");
            detailtop.setBackgroundColor(Color.parseColor("#BB4C3B"));
        }
        if (Objects.equals(naming, "Joshua")) {
            phone.setText("13621574410");
            detailname.setText("Joshua");
            from.setText("手机 江苏苏州移动");
            detailtop.setBackgroundColor(Color.parseColor("#c48d30"));
        }
        if (Objects.equals(naming, "Ivan")) {
            phone.setText("15684122771");
            detailname.setText("Ivan");
            from.setText("手机 山东烟台联通");
            detailtop.setBackgroundColor(Color.parseColor("#4469b0"));
        }
        if (Objects.equals(naming, "Mark")) {
            phone.setText("17765213579");
            detailname.setText("Mark");
            from.setText("手机 广东珠海电信");
            detailtop.setBackgroundColor(Color.parseColor("#20A17B"));
        }
        if (Objects.equals(naming, "Joseph")) {
            phone.setText("13315466578");
            detailname.setText("Joseph");
            from.setText("手机 河北石家庄电信");
            detailtop.setBackgroundColor(Color.parseColor("#BB4C3B"));
        }
        if (Objects.equals(naming, "Phoebe")) {
            phone.setText("17895466428");
            detailname.setText("Phoebe");
            from.setText("手机 山东东营移动");
            detailtop.setBackgroundColor(Color.parseColor("#c48d30"));
        }
        final List<Map<String, Object> > data = new ArrayList<>();
        String[] row = new String[] {"编辑联系人", "分享联系人", "加入黑名单", "删除联系人"};
        for (int i = 0; i < 4; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("row", row[i]);
            data.add(temp);
        }
        ListView listView = (ListView)findViewById(R.id.list_view2);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.textview2,
                new String[] {"row"}, new int[] {R.id.row});
        assert listView != null;
        listView.setAdapter(simpleAdapter);
    }
}
