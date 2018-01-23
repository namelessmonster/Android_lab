package com.example.dg123.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagChoosingActivity extends AppCompatActivity {

    class tagsinfo {

        String tagName;
        boolean choose;

        tagsinfo(String name) {
            tagName = name;
            choose = false;
        }

    }
    List<tagsinfo> tags = new ArrayList<>();
    List<String> choseTags = new ArrayList<>();
    int color;
    Set<String> tagList = new HashSet<>();

    CommandAdapter<tagsinfo> tagsListAdapter;
    CommandAdapter<String> chosetagsAdapter;

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
        setContentView(R.layout.activity_tag_choosing);
        changeTheme();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet())
                tagList.add(key);
        }
        final String tagnames[] = {"要闻","财经","娱乐","体育","科技","军事","时尚","教育","文化","汽车","股票","电影","数码",
                "政务","国际","房产","理财","家居","健康","生活","旅游","综艺","美食","育儿","宠物","历史",
                "音乐","传媒","科普","校园"};

        for (int i=0; i<tagnames.length; i++) tags.add(new tagsinfo(tagnames[i]));

        /***********************************************/
        /*        the statement of the tag list        */
        /***********************************************/

        tagsListAdapter = new CommandAdapter<tagsinfo>(this,R.layout.choosingtag_tagitem,tags) {
            @Override
            public void convert(ViewHolder holder, tagsinfo s) {
                TextView tagname = (TextView) holder.getView(R.id.choosingtag_tagitem_tagname);
                tagname.setText(s.tagName);
                if (s.choose == false) {
                    tagname.setBackgroundResource(R.drawable.choosingtag_unchosetag);
                    tagname.setTextColor(getResources().getColor(color));
                }
                if (s.choose == true) {
                    tagname.setBackgroundColor(getResources().getColor(color));
                    tagname.setTextColor(getResources().getColor(R.color.primary_white));
                }
                if (tagList.contains(s.tagName)) {
                    s.choose = true;
                    tagList.remove(s.tagName);
                    choseTags.add(s.tagName);
                    tagname.setBackgroundColor(getResources().getColor(color));
                    tagname.setTextColor(getResources().getColor(R.color.primary_white));
                }
            }
        };

        RecyclerView tagslistview = (RecyclerView) findViewById(R.id.choosingtag_taglist);
        tagslistview.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        tagslistview.setAdapter(tagsListAdapter);

        tagsListAdapter.setOnItemClickListener(new CommandAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (choseTags.size() == 5 && tags.get(position).choose == false) {
                    Toast.makeText(TagChoosingActivity.this,"标签数量达到上限",Toast.LENGTH_SHORT).show();
                    return;
                }
                tags.get(position).choose = !tags.get(position).choose;
                if (tags.get(position).choose == true) {
                    choseTags.add(tags.get(position).tagName);
                } else {
                    choseTags.remove(tags.get(position).tagName);
                }
                tagsListAdapter.notifyDataSetChanged();
                chosetagsAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onLongClick(int position) {
                return false;
            }
        });

        /***************************************************/
        /*        the statement of the choosed list        */
        /***************************************************/

        chosetagsAdapter = new CommandAdapter<String>(this,R.layout.chosetag_item,choseTags) {
            @Override
            public void convert(ViewHolder holder, String s) {
                TextView tagname = (TextView) holder.getView(R.id.chosetag_item_tagname);
                tagname.setText(s);
            }
        };

        RecyclerView chosetagView = (RecyclerView) findViewById(R.id.choosingtag_chose_layout_choselist);
        chosetagView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        chosetagView.setAdapter(chosetagsAdapter);

        chosetagsAdapter.setOnItemClickListener(new CommandAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String name = choseTags.get(position);
                for (int i=0; i<tagnames.length; i++)
                    if (tagnames[i].equals(name)) tags.get(i).choose = false;
                choseTags.remove(position);
                tagsListAdapter.notifyDataSetChanged();
                chosetagsAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onLongClick(int position) {
                return false;
            }
        });

    }

    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        color = sharedPref.getInt("color", 1);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.tag_topbar);
        if (color == 1)
            color = R.color.RIKYUSHIRACHA;
        else if (color == 2)
            color = R.color.NADESHIKO;
        else if (color == 3)
            color = R.color.SHION;
        else if (color == 4)
            color = R.color.MIZUASAGI;
        relativeLayout.setBackgroundColor(getResources().getColor(color));
    }

    public void back(View view) {
        Intent intent = new Intent();
        for (String key : choseTags)
            intent.putExtra(key, true);
        setResult(RESULT_OK, intent);
        finish();
    }
}

