package com.example.dg123.lab9_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button clear;
    private Button fetch;
    private EditText content;
    private List<Map<String, String>> list;
    private CardAdapter cardAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardAdapter = new CardAdapter(this, R.layout.cardview_layout, list) {
            @Override
            public void convert(ViewHolder holder, int pos) {
                TextView id = holder.getView(R.id.card_view_info2);
                TextView login = holder.getView(R.id.card_view_info1);
                TextView blog = holder.getView(R.id.card_view_info3);
                id.setText(mDatas.get(pos).get("id"));
                login.setText(mDatas.get(pos).get("login"));
                blog.setText(mDatas.get(pos).get("blog"));
            }
        };
        cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (cardAdapter.tag == false) return;
                Intent intent = new Intent(MainActivity.this, ReposActicity.class);
                intent.putExtra("name", list.get(position).get("login"));
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                if (cardAdapter.tag == false) return;
                cardAdapter.tag = false;
                list.remove(position);
                cardAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                cardAdapter.tag = true;
            }
        });
        recyclerView.setAdapter(cardAdapter);
        clear = (Button) findViewById(R.id.activity_main_clear);
        fetch = (Button) findViewById(R.id.activity_main_fetch);
        content = (EditText) findViewById(R.id.activity_main_search);
        progressBar = (ProgressBar) findViewById(R.id.acticity_main_progressbar);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.activity_main_clear) {
            content.setText("");
            list.clear();
            cardAdapter.notifyDataSetChanged();
        }
        else {
            String user = content.getText().toString().trim();
            //获取输入框内容
            GithubService service = ServiceFactory.createService();
            //新建一个网络请求服务
            progressBar.setVisibility(View.VISIBLE);
            //设置等待条可见
            service.getUser(user)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Github>() {
                        @Override
                        public void onNext(Github github) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("login", github.getLogin());
                            map.put("blog", "blog: " + github.getBlog());
                            map.put("id", "id: " + github.getId());
                            list.add(map);
                            cardAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onCompleted() {
                            System.out.println("完成传输");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MainActivity.this, e.hashCode() + "请确认你搜索的用户存在", Toast.LENGTH_SHORT).show();
                            Log.e("Github-Demo", e.getMessage());
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

}
