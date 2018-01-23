package com.example.dg123.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ColorActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_color);
        changeTheme();
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
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.color_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        }
        else if (color == 2) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.color_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        }
        else if (color == 3) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.color_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
        }
        else if (color == 4) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.color_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }
    public void changeColor(View view) {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (view.getId() == R.id.RIKYUSHIRACHA)
            editor.putInt("color", 1);
        else if (view.getId() == R.id.NADESHIKO)
            editor.putInt("color", 2);
        else if (view.getId() == R.id.SHION)
            editor.putInt("color", 3);
        else if (view.getId() == R.id.MIZUASAGI)
            editor.putInt("color", 4);
        editor.commit();
        changeTheme();
    }
}

