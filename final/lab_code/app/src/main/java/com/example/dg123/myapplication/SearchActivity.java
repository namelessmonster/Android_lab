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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class SearchActivity extends AppCompatActivity {
    private ListAdapter resultAdapter;
    private List<Map<String, String>> resultList;
    private RecyclerView resultRecycler;
    private AutoCompleteTextView atv;
    private ArrayAdapter<String> arrayAdapter;
    private String uid;
    private String minNid;
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
        setContentView(R.layout.activity_search);
        changeTheme();
        minNid = "";
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        atv = (AutoCompleteTextView) findViewById(R.id.activity_search_topbar_searchbar);
        arrayAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        atv.setAdapter(arrayAdapter);
        atv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.clear();
                arrayAdapter.add(s.toString());
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDatas(arrayAdapter.getItem(position), "", "10000000");
            }
        });
        resultRecycler = (RecyclerView) findViewById(R.id.search_result_list);
        resultRecycler.setLayoutManager(new LinearLayoutManager(this));
        resultList = new ArrayList<>();
        resultAdapter = new ListAdapter(SearchActivity.this, R.layout.news_list_item, resultList) {
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
        resultAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
                intent.putExtra("title", resultList.get(position).get("title"));
                intent.putExtra("content", resultList.get(position).get("content"));
                intent.putExtra("user", resultList.get(position).get("user"));
                intent.putExtra("date", resultList.get(position).get("date"));
                intent.putExtra("nid", resultList.get(position).get("nid"));
                intent.putExtra("uid", resultList.get(position).get("uid"));
                intent.putExtra("name", resultList.get(position).get("name"));
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        resultRecycler.setAdapter(resultAdapter);

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
                        Toast.makeText(SearchActivity.this, e.hashCode() + "出现错误", Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", e.getMessage());
                    }

                    @Override
                    public void onNext(List<News> newsList) {
                        if (offset.equals("")) resultAdapter.mDatas.clear();
                        for (News news : newsList){
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("title", news.getTitle());
                            map.put("view", "浏览量: " + news.getView());
                            map.put("comment", "评论数: " + news.getComment());
                            map.put("cover", news.getCover());
                            map.put("date", news.getDate());
                            map.put("content", news.getContent());
                            map.put("user", news.getUser());
                            map.put("nid", news.getNid());
                            map.put("uid", uid);
                            map.put("name", news.getName());
                            minNid = news.getNid();
                            resultList.add(map);
                        }
                        resultAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void back(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void getImage(String url, ImageView imageView) {
        Glide.with(SearchActivity.this)
                .load(baseUrl + url)
                .crossFade()
                .error(R.color.main)
                .into(imageView);
    }

    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        int color = sharedPref.getInt("color", 1);
        if (color == 1) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_search_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        }
        else if (color == 2) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_search_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        }
        else if (color == 3) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_search_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
        }
        else if (color == 4) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_search_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }
}
