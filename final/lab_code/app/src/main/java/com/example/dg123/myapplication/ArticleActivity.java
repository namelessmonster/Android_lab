package com.example.dg123.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArticleActivity extends AppCompatActivity {

    private String uid;
    private String nid;
    private String title;
    private String content;
    private String date;
    private String user;
    private TextView titleView;
    private TextView time;
    private String url;
    private String name;
    private String tags;
    private List<String> tagList = new ArrayList<>();
    private static final int maxLen = 99999999;
    private RelativeLayout relativeLayout;

    private static final String baseUrl = "http://172.18.92.122:8080";


    final String imgTagStart = "![](";
    final String imgTagEnd = ")";

    CommandAdapter<commandInfo> commandsAdapter;

    // obtain the tags
    List<String> getTags() {
        List<String> ret = new ArrayList<>();
        ret.add("新闻");
        ret.add("时事");
        return tagList;
    }

    // the command class
    class commandInfo {

        String cid;
        String icon;
        String userName;
        String submitTime;
        String commandText;
        String replyUserName;
        String replySubmitTime;
        String replycommandText;

        commandInfo() {
            userName = null;
            submitTime = null;
            commandText = null;
            replycommandText = null;
            replySubmitTime = null;
            replyUserName = null;
        }

    }

    List<commandInfo> commandInfos = new ArrayList<>();

    int replyTo;

    public void getDatas() {
        MobiusService service = ServiceFactory.createService();
        service.loadComment(nid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ArticleActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        commandInfos.clear();
                        for (int i=0; i<comments.size(); i++) {
                            Comment comment = comments.get(i);
                            commandInfo commandinfo = new commandInfo();
                            commandinfo.cid = comment.getCid();
                            commandinfo.icon = comment.getHead();
                            commandinfo.userName = comment.getName();
                            commandinfo.commandText = comment.getContent();
                            commandinfo.submitTime = comment.getDate();
                            commandinfo.replySubmitTime = "";
                            commandInfos.add(commandinfo);
                        }
                        Map<String,Integer> mp = new HashMap<>();
                        for (int i=0; i<commandInfos.size(); i++)
                            mp.put(commandInfos.get(i).cid,i);
                        for (int i=0; i<comments.size(); i++) {
                            String reply = comments.get(i).getReply();
                            commandInfos.get(i).replycommandText = null;
                            if (!reply.equals("-1")) {
                                Integer pos = mp.get(reply);
                                if (pos == null) continue;
                                commandInfo replycommand = commandInfos.get(pos);
                                commandInfos.get(i).replySubmitTime = "";
                                commandInfos.get(i).replyUserName = replycommand.userName;
                                commandInfos.get(i).replycommandText = replycommand.commandText;
                            }
                        }
                        commandsAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void postComment(String commentText, String reply) {
        MobiusService service = ServiceFactory.createService();
        service.uploadComment(uid,nid,commentText,reply)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Comment>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ArticleActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Comment comment) {
                        getDatas();
                    }
                });
    }

    public void getImage(String url, ImageView imageView) {
        Glide.with(ArticleActivity.this)
                .load(baseUrl + url)
                .crossFade()
                .placeholder(R.mipmap.bg)
                .error(R.color.article_commanditem_replycommandlayout_framecolor)
                .into(imageView);
    }

    @SuppressLint("ClickableViewAccessibility")
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
        setContentView(R.layout.activity_article);
        relativeLayout = (RelativeLayout) findViewById(R.id.article_inputview);
        changeTheme();

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        url = bundle.getString("content");
        date = bundle.getString("date");
        user = bundle.getString("user");
        nid = bundle.getString("nid");
        uid = bundle.getString("uid");
        name = bundle.getString("name");
        tags = bundle.getString("tags");
        if (tags != null && !tags.equals(""))
            tagList = Arrays.asList(tags.split(";"));

        titleView = (TextView) findViewById(R.id.article_contentview_layout_title);
        titleView.setText(title);
        time = (TextView) findViewById(R.id.article_contentview_layout_submittime);
        time.setText("发布者: " + name);
        time = (TextView) findViewById(R.id.date);
        time.setText("发布日期: " + date);

        getContent(url);

        List<String> tags = getTags();
        CommandAdapter<String> tagsListAdapter = new CommandAdapter<String>(this,R.layout.article_tag_item,tags) {

            @Override
            public void convert(ViewHolder holder, String s) {
                TextView singleTag = (TextView) holder.getView(R.id.article_tag_item_tagname);
                singleTag.setText(s);
            }

        };
        RecyclerView tagsView = (RecyclerView) findViewById(R.id.article_contentview_layout_tagslist);
        tagsView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        tagsView.setAdapter(tagsListAdapter);

        /*******************************************************/
        /*        the statement of the command edittext        */
        /*******************************************************/

        replyTo = -1;
        final EditText commandBox = (EditText) findViewById(R.id.article_inputview_inputbox);
        commandBox.setHint("说点什么吧...");
        commandBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    commandBox.setHint("说点什么吧...");
                    replyTo = -1;
                }
                return false;
            }
        });

        /********************************************************/
        /*        the statement of of adding new command        */
        /********************************************************/

        TextView newcommandbox = (TextView) findViewById(R.id.article_contentview_layout_addnewcomand);
        newcommandbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.VISIBLE);
                commandBox.setFocusable(true);
                commandBox.requestFocus();
                InputMethodManager imm = (InputMethodManager) commandBox.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                replyTo = -1;
                commandBox.setHint("说点什么吧...");
            }
        });

        /****************************************************/
        /*        the statement for the command list        */
        /****************************************************/

        commandsAdapter = new CommandAdapter<commandInfo>(this,R.layout.article_command_item,commandInfos) {
            @Override
            public void convert(ViewHolder holder, commandInfo commandInfo) {
                ImageView icon = (ImageView) holder.getView(R.id.article_commanditem_icon);
                getImage(commandInfo.icon,icon);
                TextView username = (TextView) holder.getView(R.id.article_commanditem_username);
                TextView submitTime = (TextView) holder.getView(R.id.article_commanditem_time);
                TextView commandText = (TextView) holder.getView(R.id.article_commanditem_commandtext);
                username.setText(commandInfo.userName);
                submitTime.setText(commandInfo.submitTime);
                commandText.setText(commandInfo.commandText);
                if (commandInfo.replycommandText == null) {
                    ConstraintLayout replyBox = (ConstraintLayout) holder.getView(R.id.article_commanditem_commandreplylayout);
                    replyBox.setVisibility(View.GONE);
                    return;
                }
                ConstraintLayout replyBox = (ConstraintLayout) holder.getView(R.id.article_commanditem_commandreplylayout);
                replyBox.setVisibility(View.VISIBLE);
                TextView replyUsername = (TextView) holder.getView(R.id.article_commanditem_commandreplylayout_username);
                TextView replySubmitTime = (TextView) holder.getView(R.id.article_commanditem_commandreplylayout_submittime);
                TextView replyText = (TextView) holder.getView(R.id.article_commanditem_commandreplylayout_commandtext);
                replyUsername.setText(commandInfo.replyUserName);
                replySubmitTime.setText(commandInfo.replySubmitTime);
                replyText.setText(commandInfo.replycommandText);
            }
        };
        RecyclerView commandView = (RecyclerView) findViewById(R.id.article_contentview_layout_commandlist);
        commandView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        commandView.setAdapter(commandsAdapter);

        commandsAdapter.setOnItemClickListener(new CommandAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                relativeLayout.setVisibility(View.VISIBLE);
                commandBox.setFocusable(true);
                commandBox.requestFocus();
                InputMethodManager imm = (InputMethodManager) commandBox.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                replyTo = position;
                commandBox.setHint("回复 " + commandInfos.get(position).userName + ":");
            }

            @Override
            public boolean onLongClick(int position) {
                return false;
            }
        });

        Button submitButton = (Button) findViewById(R.id.article_inputview_submitbox);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (replyTo == -1) postComment(commandBox.getText().toString(),"-1");
                else postComment(commandBox.getText().toString(),commandInfos.get(replyTo).cid);
                commandBox.setText("");
                relativeLayout.setVisibility(View.GONE);
            }
        });

        getDatas();

    }

    public void translation() {
        ArrayList<String> uri = new ArrayList<>();
        String content_copy = content;
        while(!content_copy.equals("")) {
            if(content_copy.contains(imgTagStart)) {
                final int start = content_copy.indexOf(imgTagStart);
                final int end = content_copy.indexOf(imgTagEnd);
                uri.add(content_copy.substring(start+4, end));
                content_copy = content_copy.substring(end + 1);
            } else break;
        }
        final int len = uri.size();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.article);
        while(content != null) {
            int pos = maxLen;
            int img = 0;
            for(int i = 0; i < len; ++i) {
                final String imgPath = imgTagStart + uri.get(i) + imgTagEnd;
                if(pos > content.indexOf(imgPath) && content.contains(imgPath)) {
                    img = i;
                    pos = content.indexOf(imgPath);
                }
            }
            if(pos != maxLen) {
                TextView text = new TextView(this);
                text.setText(content.substring(0, pos));
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                text.setTextColor(getResources().getColor(R.color.black));
                content = content.substring(pos);
                linearLayout.addView(text, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ImageView imgView = new ImageView(this);
                imgView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Glide.with(this).load(baseUrl + uri.get(img)).into(imgView);
                linearLayout.addView(imgView);
                final String imgPath = imgTagStart + uri.get(img) + imgTagEnd;
                content = content.substring(imgPath.length());
            } else {
                TextView text = new TextView(this);
                text.setText(content);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                text.setTextColor(getResources().getColor(R.color.black));
                linearLayout.addView(text, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                content = null;
            }
        }
    }

    public void getContent(String url) {
        MobiusService service = ServiceFactory.createService();
        service.getArticle(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Article>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Article article) {
                        content = article.getContent();
                        translation();
                    }
                });
    }

    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        int color = sharedPref.getInt("color", 1);
        if (color == 1) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.article_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        }
        else if (color == 2) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.article_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        }
        else if (color == 3) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.article_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
        }
        else if (color == 4) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.article_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }

    public void back(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void fade(View view) {
        relativeLayout.setVisibility(View.GONE);
    }
}

