package com.example.dg123.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    private EditText editText;
    private Button registerButton;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_register);
        changeTheme();
    }
    public void ok(View view) {
        editText = (EditText) findViewById(R.id.register_user_ed);
        String uid = "";
        if (editText.getText() != null)
            uid = editText.getText().toString();
        editText = (EditText) findViewById(R.id.register_pass_ed);
        String pass = "";
        if (editText.getText() != null)
            pass = editText.getText().toString();
        editText = (EditText) findViewById(R.id.register_confirm_ed);
        String confirm = "";
        if (editText.getText() != null)
            confirm = editText.getText().toString();
        if (uid.equals("")) {
            Toast.makeText(RegisterActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.equals("")) {
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(confirm)) {
            Toast.makeText(RegisterActivity.this, "密码不匹配", Toast.LENGTH_SHORT).show();
            return;
        }
        MobiusService service = ServiceFactory.createService();
        service.registerUser(uid, pass)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", e.getMessage());
                    }
                    @Override
                    public void onNext(User user) {
                        String s = user.getName();
                        if (s.equals("Success")) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void back(View view) {
        finish();
    }

    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        int color = sharedPref.getInt("color", 1);
        registerButton = (Button) findViewById(R.id.register_ok);
        backButton = (Button) findViewById(R.id.register_back);
        if (color == 1) {
            registerButton.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
            backButton.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        } else if (color == 2) {
            registerButton.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
            backButton.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        }
         else if (color == 3) {
            registerButton.setBackgroundColor(getResources().getColor(R.color.SHION));
            backButton.setBackgroundColor(getResources().getColor(R.color.SHION));
        }
        else if (color == 4) {
            registerButton.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
            backButton.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }
}
