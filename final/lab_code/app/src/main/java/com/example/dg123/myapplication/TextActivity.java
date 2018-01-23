package com.example.dg123.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TextActivity extends AppCompatActivity {
    private String uid;
    private String title;
    private String content;
    private TextView textView;
    private EditText editText;
    private RelativeLayout relativeLayout1;
    private RelativeLayout relativeLayout2;
    private Map<String, String> trans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_text);
        changeTheme();
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        title = bundle.getString("title");
        content = bundle.getString("content");
        trans = new HashMap<>();
        trans.put("昵称", "name");
        trans.put("个性签名", "motto");
        setInfo();
    }
    public void back(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
    public void save(View view) {
        MobiusService service = ServiceFactory.createService();
        if (trans.get(title).equals("name"))
            editText = (EditText) findViewById(R.id.text_name);
        else
            editText = (EditText) findViewById(R.id.text_content);
        content = editText.getText().toString().trim();
        service.saveInfo(uid, trans.get(title), content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TextActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Toast.makeText(TextActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPref;
                        Context context;
                        context = getApplicationContext();
                        sharedPref = context.getSharedPreferences(uid, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(trans.get(title), content);
                        editor.commit();
                    }
                });
    }
    public void setInfo() {
        relativeLayout1 = (RelativeLayout) findViewById(R.id.text_content1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.text_content2);
        textView = (TextView) findViewById(R.id.text_topbar_title);
        textView.setText(title);
        editText = (EditText) findViewById(R.id.text_content);
        editText.setText(content);
        if (title.equals("昵称")) relativeLayout1.setVisibility(View.GONE);
        else relativeLayout1.setVisibility(View.VISIBLE);
        editText = (EditText) findViewById(R.id.text_name);
        editText.setText(content);
        if (title.equals("昵称")) relativeLayout2.setVisibility(View.VISIBLE);
        else relativeLayout2.setVisibility(View.GONE);
    }
    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        int color = sharedPref.getInt("color", 1);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.text_topbar);
        if (color == 1) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        } else if (color == 2) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        } else if (color == 3) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
        } else if (color == 4) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }
}
