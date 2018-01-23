package com.example.dg123.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class PersonalActivity extends AppCompatActivity {
    String uid;
    TextView textView;
    ImageView imageView;
    private static final String baseUrl = "http://172.18.92.122:8080";
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
        setContentView(R.layout.activity_personal);
        changeTheme();
        setInfo();
    }
    public void back(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        int color = sharedPref.getInt("color", 1);
        if (color == 1) {
            Button button = (Button) findViewById(R.id.personal_button);
            button.setBackgroundResource(R.drawable.button_background);
            button.setTextColor(getResources().getColor(R.color.RIKYUSHIRACHA));
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.personal_article);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
            linearLayout = (LinearLayout) findViewById(R.id.personal_history);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
            linearLayout = (LinearLayout) findViewById(R.id.personal_color);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
            linearLayout = (LinearLayout) findViewById(R.id.personal_logout);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        }
        else if (color == 2) {
            Button button = (Button) findViewById(R.id.personal_button);
            button.setBackgroundResource(R.drawable.button_background_v2);
            button.setTextColor(getResources().getColor(R.color.NADESHIKO));
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.personal_article);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
            linearLayout = (LinearLayout) findViewById(R.id.personal_history);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
            linearLayout = (LinearLayout) findViewById(R.id.personal_color);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
            linearLayout = (LinearLayout) findViewById(R.id.personal_logout);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        }
        else if (color == 3) {
            Button button = (Button) findViewById(R.id.personal_button);
            button.setBackgroundResource(R.drawable.button_background_v3);
            button.setTextColor(getResources().getColor(R.color.SHION));
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.personal_article);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
            linearLayout = (LinearLayout) findViewById(R.id.personal_history);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
            linearLayout = (LinearLayout) findViewById(R.id.personal_color);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
            linearLayout = (LinearLayout) findViewById(R.id.personal_logout);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
        }
        else if (color == 4) {
            Button button = (Button) findViewById(R.id.personal_button);
            button.setBackgroundResource(R.drawable.button_background_v4);
            button.setTextColor(getResources().getColor(R.color.MIZUASAGI));
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.personal_article);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
            linearLayout = (LinearLayout) findViewById(R.id.personal_history);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
            linearLayout = (LinearLayout) findViewById(R.id.personal_color);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
            linearLayout = (LinearLayout) findViewById(R.id.personal_logout);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }

    public void changeInfo(View view) {
        Intent intent = new Intent(PersonalActivity.this, InfoActivity.class);
        intent.putExtra("uid", uid);
        startActivityForResult(intent, 1);
    }

    public void jump(View view) {
        if (view.getId() == R.id.personal_write) {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
        if (view.getId() == R.id.personal_article) {
            Intent intent = new Intent(PersonalActivity.this, MyNewsActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
        if (view.getId() == R.id.personal_color) {
            Intent intent = new Intent(PersonalActivity.this, ColorActivity.class);
            startActivityForResult(intent, 1);
        }
        if (view.getId() == R.id.personal_logout) {
            SharedPreferences sharedPref;
            Context context;
            context = getApplicationContext();
            sharedPref = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("uid", "");
            editor.putString("name", "");
            editor.commit();
            final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            changeTheme();
            setInfo();
        }
    }

    public void setInfo() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        uid = sharedPref.getString("uid", "");
        sharedPref = context.getSharedPreferences(uid, Context.MODE_PRIVATE);
        textView = (TextView) findViewById(R.id.personal_name);
        textView.setText(sharedPref.getString("name", ""));
        textView = (TextView) findViewById(R.id.personal_signature);
        textView.setText(sharedPref.getString("motto", ""));
        imageView = (ImageView) findViewById(R.id.personal_head);
        String head = sharedPref.getString("head", "");
        if (!head.equals(""))  getImage(head, imageView);
        imageView = (ImageView) findViewById(R.id.personal_bg);
        String cover = sharedPref.getString("cover", "");
        if (!cover.equals("")) getImage(cover, imageView);

    }

    public void getImage(String url, ImageView imageView) {
        Glide.with(PersonalActivity.this)
                .load(baseUrl + url)
                .crossFade()
                .error(R.color.main)
                .into(imageView);
    }
}
