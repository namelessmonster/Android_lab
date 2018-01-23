package com.example.dg123.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private LinearLayout dlLeft;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<Map<String, String>> list;
    private static final String baseUrl = "http://172.18.92.122:8080";
    private String maxNid;
    private RefreshLayout refreshLayout;
    private String uid;
    private TextView textView;
    private ImageView imageView;
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
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        changeTheme();
        setInfo();
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDatas("", "", "20");
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getDatas("", maxNid, "20");
                refreshlayout.finishLoadmore();
            }
        });
        maxNid = "";
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        dlLeft = (LinearLayout) findViewById(R.id.dl_left);
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        listAdapter = new ListAdapter(this, R.layout.news_list_item, list) {
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
                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                intent.putExtra("title", list.get(position).get("title"));
                intent.putExtra("content", list.get(position).get("content"));
                intent.putExtra("user", list.get(position).get("user"));
                intent.putExtra("date", list.get(position).get("date"));
                intent.putExtra("nid", list.get(position).get("nid"));
                intent.putExtra("uid", list.get(position).get("uid"));
                intent.putExtra("name", list.get(position).get("name"));
                intent.putExtra("tags", list.get(position).get("tags"));
                startActivityForResult(intent, Integer.parseInt(list.get(position).get("nid")));
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        recyclerView.setAdapter(listAdapter);
        getDatas("", "", "20");
    }
    public void getDatas(final String key, final String offset, final String tot) {
        MobiusService service = ServiceFactory.createService();
        service.getNews(key, offset, tot)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, e.hashCode() + "出现错误", Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", e.getMessage());
                    }

                    @Override
                    public void onNext(List<News> newsList) {
                        if (tot.equals("1")) {
                            News news = newsList.get(0);
                            Integer i = Integer.parseInt(offset);
                            i -= 1;
                            String s = String.valueOf(i);
                            for (Map map : list) {
                                if (map.get("nid").equals(s)) {
                                    map.put("view", "浏览量: " + news.getView());
                                    map.put("comment", "评论数: " + news.getComment());
                                    break;
                                }
                            }
                            listAdapter.notifyDataSetChanged();
                            return;
                        }
                        if (offset.equals("")) list.clear();
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
                            map.put("tags", news.getTags());
                            maxNid = news.getNid();
                            list.add(map);
                        }
                        listAdapter.notifyDataSetChanged();
                    }

                });

    }
    public void getImage(String url, ImageView imageView) {
        Glide.with(MainActivity.this)
                .load(baseUrl + url)
                .crossFade()
                .error(R.color.main)
                .into(imageView);
    }
    public void search(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
    public void pop(View view) {
        if (!drawerLayout.isDrawerOpen(dlLeft)) {
            drawerLayout.openDrawer(dlLeft);
        }
    }
    public void change(View view) {
        if (view.getId() == R.id.dl_left_info1) {
            Intent intent = new Intent(this, PersonalActivity.class);
            intent.putExtra("uid", uid);
            startActivityForResult(intent, 1);
        } else if (view.getId() == R.id.dl_left_color1) {
            Intent intent = new Intent(this, ColorActivity.class);
            startActivityForResult(intent, 1);
        } else if (view.getId() == R.id.dl_left_article1) {
            Intent intent = new Intent(this, MyNewsActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        } else if (view.getId() == R.id.dl_left_comment1) {

        } else if (view.getId() == R.id.dl_left_write1) {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
    }
    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        int color = sharedPref.getInt("color", 1);
        if (color == 1) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        }
        else if (color == 2) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        }
        else if (color == 3) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
        }
        else if (color == 4) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            changeTheme();
            setInfo();
            if (requestCode != 1) {
                getDatas("", String.valueOf(requestCode+1), "1");
            }
        }
    }
    public void setInfo() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        uid = sharedPref.getString("uid", "");
        sharedPref = context.getSharedPreferences(uid, Context.MODE_PRIVATE);
        textView = (TextView) findViewById(R.id.dl_left_name);
        textView.setText(sharedPref.getString("name", ""));
        textView = (TextView) findViewById(R.id.dl_left_motto);
        textView.setText(sharedPref.getString("motto", ""));
        imageView = (ImageView) findViewById(R.id.dl_left_head);
        String head = sharedPref.getString("head", "");
        if (!head.equals(""))  getImage(head, imageView);
        imageView = (ImageView) findViewById(R.id.dl_left_background);
        String cover = sharedPref.getString("cover", "");
        if (!cover.equals("")) getImage(cover, imageView);
    }

}
