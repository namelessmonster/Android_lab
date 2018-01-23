package com.example.dg123.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyNewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<Map<String, String>> list;
    private String uid;
    private static final String baseUrl = "http://172.18.92.122:8080";

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
        setContentView(R.layout.activity_my_news);
        changeTheme();
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        list = new ArrayList<>();
        listAdapter = new ListAdapter(MyNewsActivity.this, R.layout.news_list_item, list) {
            @Override
            public void convert(ViewHolder holder, int pos) {
                TextView title = holder.getView(R.id.list_item_title);
                title.setText(mDatas.get(pos).get("title"));
                TextView view = holder.getView(R.id.list_item_view);
                view.setText(mDatas.get(pos).get("view"));
                TextView comment = holder.getView(R.id.list_item_comment);
                comment.setText(mDatas.get(pos).get("comment"));
                ImageView imageView = holder.getView(R.id.list_item_image);
                getImage(mDatas.get(pos).get("cover"), imageView);
            }
        };
        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MyNewsActivity.this, ArticleActivity.class);
                intent.putExtra("title", list.get(position).get("title"));
                intent.putExtra("content", list.get(position).get("content"));
                intent.putExtra("user", list.get(position).get("user"));
                intent.putExtra("date", list.get(position).get("date"));
                intent.putExtra("nid", list.get(position).get("nid"));
                intent.putExtra("uid", list.get(position).get("uid"));
                intent.putExtra("name", list.get(position).get("name"));
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.mynews_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
        getDatas(uid);
    }
    public void changeTheme() {
            SharedPreferences sharedPref;
            Context context;
            context = getApplicationContext();
            sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
            int color = sharedPref.getInt("color", 1);
            if (color == 1) {
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mynews_topbar);
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
            }
            else if (color == 2) {
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mynews_topbar);
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
            }
            else if (color == 3) {
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mynews_topbar);
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
            }
            else if (color == 4) {
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mynews_topbar);
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
            }
    }
    public void back(View view) {
        finish();
    }

    public void getImage(String url, ImageView imageView) {
        Glide.with(MyNewsActivity.this)
                .load(baseUrl + url)
                .crossFade()
                .error(R.color.main)
                .into(imageView);
    }

    public void getDatas(final String uid) {
        MobiusService service = ServiceFactory.createService();
        service.getNewsForUid(uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MyNewsActivity.this, e.hashCode() + "出现错误", Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", e.getMessage());
                    }

                    @Override
                    public void onNext(List<News> newsList) {
                        list.clear();
                        for (News news : newsList){
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("title", news.getTitle());
                            map.put("view", "浏览量: " + news.getView());
                            map.put("comment", "评论数: " + news.getComment());
                            map.put("cover", news.getCover());
                            map.put("content", news.getContent());
                            map.put("date", news.getDate());
                            map.put("user", news.getUser());
                            map.put("nid", news.getNid());
                            map.put("uid", uid);
                            map.put("name", news.getName());
                            list.add(map);
                        }
                        listAdapter.notifyDataSetChanged();
                    }

                });

    }
}
