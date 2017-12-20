package com.example.dg123.lab8_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
    }

    public void add(View view) {
        Bundle bundle = new Bundle();
        EditText name = (EditText) findViewById(R.id.activity_item_name_edit);
        //如果名字为空，弹出信息提示
        if (name.getText().toString().trim().length() == 0){
            Toast.makeText(this, "名字为空，请完善", Toast.LENGTH_SHORT).show();
            return;
        }
        //如果名字重复，弹出信息提示
        if (MainActivity.nameSet.contains(name.getText().toString().trim())) {
            Toast.makeText(this, "名字重复啦，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        //将数据压入bundle返回给MainActivity将数据插入数据库
        bundle.putString("name", name.getText().toString());
        EditText birthday = (EditText) findViewById(R.id.activity_item_birthday_edit);
        bundle.putString("birthday", birthday.getText().toString());
        EditText gift = (EditText) findViewById(R.id.activity_item_gift_edit);
        bundle.putString("gift", gift.getText().toString());
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
