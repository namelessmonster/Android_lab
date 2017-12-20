package com.example.dg123.lab7_code;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText newPassword;
    EditText confirmPassword;
    ConstraintLayout constraintLayout;
    Context context;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        sharedPref = context.getSharedPreferences("PAWWORD", Context.MODE_PRIVATE);
        String password = sharedPref.getString("password", "");
        if (password.length() > 0) {
            constraintLayout = (ConstraintLayout) findViewById(R.id.second);
            constraintLayout.setVisibility(View.VISIBLE);
            constraintLayout = (ConstraintLayout) findViewById(R.id.first);
            constraintLayout.setVisibility(View.GONE);
        } else {
            constraintLayout = (ConstraintLayout) findViewById(R.id.second);
            constraintLayout.setVisibility(View.GONE);
            constraintLayout = (ConstraintLayout) findViewById(R.id.first);
            constraintLayout.setVisibility(View.VISIBLE);
        }
    }
    public void buttonClick(View view) {
        if (view.getId() == R.id.first_ok) {    //当密码设置界面的ok按钮被点击
            newPassword = (EditText) findViewById(R.id.first_new_password);
            confirmPassword = (EditText) findViewById(R.id.first_confirm_password);
            //若密码条不为空且两个密码相同
            if (newPassword.getText().toString()
                    .equals(confirmPassword.getText().toString())
                    && newPassword.getText().toString().length() > 0){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("password", newPassword.getText().toString());
                editor.commit();    //将密码写入SharedPreferences的PASSWORD文件中，key为password
                constraintLayout = (ConstraintLayout) findViewById(R.id.second);
                constraintLayout.setVisibility(View.VISIBLE);
                constraintLayout = (ConstraintLayout) findViewById(R.id.first);
                constraintLayout.setVisibility(View.GONE);
                //进入密码输入界面
            }
            else {
                if (newPassword.getText().toString().length() == 0
                        || confirmPassword.getText().toString().length() == 0)
                    Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(MainActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (view.getId() == R.id.first_clear) {
            newPassword = (EditText) findViewById(R.id.first_new_password);
            confirmPassword = (EditText) findViewById(R.id.first_confirm_password);
            newPassword.setText("");
            confirmPassword.setText("");
        }
        else if (view.getId() == R.id.second_ok) {  //在密码输入界面点击ok按钮
            newPassword = (EditText) findViewById(R.id.second_password);
            String password = sharedPref.getString("password", "");
            //若输入密码等于存储在SharedPreferences的PASSWORD文件中的password
            if (password.equals(newPassword.getText().toString())) {
                //进入文件内容编辑界面
                Intent intent = new Intent(MainActivity.this, FileEdit.class);
                startActivity(intent);
            }
            else
                Toast.makeText(MainActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                //弹出密码不匹配提示
        }
        else if (view.getId() == R.id.second_clear) {
            newPassword = (EditText) findViewById(R.id.second_password);
            newPassword.setText("");
        }
    }
}
