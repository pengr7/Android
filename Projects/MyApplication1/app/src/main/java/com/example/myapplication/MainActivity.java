package com.example.myapplication;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = (Button) findViewById(R.id.button);
        Button register = (Button) findViewById(R.id.button2);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.ragiogroup);
        final EditText username = (EditText) findViewById(R.id.editText4);
        final EditText password = (EditText) findViewById(R.id.editText5);
        final RadioGroup radioGroup2;
        assert login != null;
        assert radioGroup != null;
        assert register != null;
        assert username != null;
        assert password != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    Snackbar.make(radioGroup, "用户名不能为空", Snackbar.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password.getText().toString())) {
                    Snackbar.make(radioGroup, "密码不能为空", Snackbar.LENGTH_SHORT).show();
                }
                else if (Objects.equals(username.getText().toString(), "Android") && Objects.equals(password.getText().toString(), "123456")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("登录成功");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Snackbar.make(radioGroup, "对话框“取消”按钮被点击", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Snackbar.make(radioGroup, "对话框“确定”按钮被点击", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("登录失败");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Snackbar.make(radioGroup, "对话框“取消”按钮被点击", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Snackbar.make(radioGroup, "对话框“确定”按钮被点击", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.radioButton4)
                    Snackbar.make(radioGroup, "学生身份注册功能尚未开启", Snackbar.LENGTH_SHORT).show();
                if (id == R.id.radioButton3)
                    Snackbar.make(radioGroup, "老师身份注册功能尚未开启", Snackbar.LENGTH_SHORT).show();
                if (id == R.id.radioButton2)
                    Snackbar.make(radioGroup, "社团身份注册功能尚未开启", Snackbar.LENGTH_SHORT).show();
                if (id == R.id.radioButton)
                    Snackbar.make(radioGroup, "管理者身份注册功能尚未开启", Snackbar.LENGTH_SHORT).show();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) MainActivity.this.findViewById(checkedId);
                assert radioButton != null;
                Snackbar.make(radioGroup, radioButton.getText() + "身份被选中", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
