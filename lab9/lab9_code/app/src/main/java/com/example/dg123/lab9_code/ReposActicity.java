package com.example.dg123.lab9_code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReposActicity extends AppCompatActivity {

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, String>> list;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        progressBar = (ProgressBar) findViewById(R.id.activity_repos_progressbar);
        listView = (ListView) findViewById(R.id.activity_repos_list);
        list = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(this, list, R.layout.repos_item,
                new String[] {"name", "language", "description"},
                new int[] {R.id.repos_item_info1, R.id.repos_item_info2, R.id.repos_item_info3});
        listView.setAdapter(simpleAdapter);
        getDatas(name);
    }

    public void getDatas(String name) {
        GithubService service = ServiceFactory.createService();
        progressBar.setVisibility(View.VISIBLE);
        service.getRepos(name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    @Override
                    public void onNext(List<Repos> repos) {
                        for (Repos repo : repos){
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("name", repo.getName());
                            map.put("description", repo.getDescription());
                            map.put("language", repo.getLanguage());
                            list.add(map);
                            simpleAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                    @Override
                    public void onCompleted() {
                        System.out.println("完成传输2");
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("Github-Demo", e.getMessage());
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

    }
}
