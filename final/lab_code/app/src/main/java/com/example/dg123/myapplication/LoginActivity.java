package com.example.dg123.myapplication;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private EditText editText;
    private Button loginButton;
    private Button registerButton;
    private RelativeLayout relativeLayout;

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
        setContentView(R.layout.activity_login);
        changeTheme();
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        String uid = sharedPref.getString("uid", "");
        String uname = sharedPref.getString("name", "");
        if (!uid.equals("")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
            finish();
        }
    }
    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }
    public void login(View view) {
        editText = (EditText) findViewById(R.id.login_user_ed);
        String uid = editText.getText().toString();
        editText = (EditText) findViewById(R.id.login_pass_ed);
        String pass = editText.getText().toString();
        if (uid.equals("")) {
            Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.equals("")) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        MobiusService service = ServiceFactory.createService();
        service.login(uid, pass)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    String uname = "";
                    String uid = "";
                    String motto = "";
                    String head = "";
                    String cover = "";
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", e.getMessage());
                    }
                    @Override
                    public void onNext(User user) {
                        uid = user.getUid();
                        uname = user.getName();
                        motto = user.getMotto();
                        head = user.getHead();
                        cover = user.getCover();
                        if (!uid.equals("") && !uname.equals("密码不正确")) {
                            SharedPreferences sharedPref;
                            Context context;
                            context = getApplicationContext();
                            sharedPref = context.getSharedPreferences(uid, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("uid", uid);
                            editor.putString("name", uname);
                            editor.putString("motto", motto);
                            editor.putString("head", head);
                            editor.putString("cover", cover);
                            editor.commit();
                            sharedPref = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
                            editor = sharedPref.edit();
                            editor.putString("uid", uid);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        int color = sharedPref.getInt("color", 1);
        relativeLayout = (RelativeLayout) findViewById(R.id.login_bg);
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.register_button);
        if (color == 1) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
            loginButton.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
            registerButton.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        }
        else if (color == 2) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
            loginButton.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
            registerButton.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        }
        else if (color == 3) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
            loginButton.setBackgroundColor(getResources().getColor(R.color.SHION));
            registerButton.setBackgroundColor(getResources().getColor(R.color.SHION));
        }
        else if (color == 4) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
            loginButton.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
            registerButton.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }
}
