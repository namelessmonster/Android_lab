package com.example.dg123.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class preViewActivity extends AppCompatActivity {
    final String imgTagStart = "![](";
    final String imgTagEnd = ")";
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
        setContentView(R.layout.pre_view);
        changeTheme();
        final Article1 data = (Article1) getIntent().getSerializableExtra("info");
        assert data != null;
        String content = data.getContent();
        final String title = data.getTitle();
        ArrayList<String> uriList = data.getUriList();
        ArrayList<String> path = data.getPath();
        final int len = path.size();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.article);
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
        TextView author = (TextView) findViewById(R.id.author);
        final String authorName = "作者:" + "123";
        author.setText(authorName);
        while(content != null) {
            int pos = 99;
            int img = 0;
            for(int i = 0; i < len; ++i) {
                final String imgPath = imgTagStart + path.get(i) + imgTagEnd;
                if(pos > content.indexOf(imgPath) && content.contains(imgPath)) {
                    img = i;
                    pos = content.indexOf(imgPath);
                }
            }
            if(pos != 99) {
                TextView text = new TextView(this);
                final String textContent = content.substring(0, pos);
                text.setText(textContent);
                text.setTextColor(getResources().getColor(R.color.black));
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                content = content.substring(pos);
                linearLayout.addView(text, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                final ImageView imgView = new ImageView(this);
                Uri uri = Uri.parse(uriList.get(img));
                imgView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Glide.with(this).load(uri).into(imgView);
                linearLayout.addView(imgView);
                final String imgPath = imgTagStart + path.get(img) + imgTagEnd;
                content = content.substring(imgPath.length());
            } else {
                TextView text = new TextView(this);
                final String textContent = content;
                text.setText(textContent);
                text.setTextColor(getResources().getColor(R.color.black));
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                linearLayout.addView(text, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                content = null;
            }
        }
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
        if (color == 1) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.preview_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        }
        else if (color == 2) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.preview_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        }
        else if (color == 3) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.preview_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
        }
        else if (color == 4) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.preview_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }
}
