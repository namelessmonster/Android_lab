package com.example.dg123.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableActivity extends AppCompatActivity implements View.OnClickListener{
    private InputMethodManager imm;
    private boolean mark;
    private ScrollView s1;
    private ScrollView s2;
    private RelativeLayout r1;
    private RelativeLayout r2;
    public Map<String, String> map;
    public List<Integer> powers;
    public List<Integer> propertys;
    public int power_pointer;
    public int property_pointer;
    private ScrollView scrollView;
    private  ScrollView heroScrollView;
    private int Y;
    private int heroY;

    private EditText ev;
    private EditText ev1;
    private ImageView iv;
    private Thread mThead;
    private Handler mHandler;
    private Button mButton1;
    private ImageView mImageView;
    private ImageView mImageView2;
    private Bitmap bitmap;
    private Bitmap image;
    private String action;


    private static final int REQUEST_CODE_PICK_IMAGE=222;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setupUI(findViewById(R.id.bg), 102);
        setupUI(findViewById(R.id.hero_scrollview), 101);
        mButton1=((Button)findViewById(R.id.select));
        mImageView=((ImageView)findViewById(R.id.rect));
        mImageView2=((ImageView)findViewById(R.id.hero_image));
        mButton1.setOnClickListener(this);
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        heroScrollView = (ScrollView) findViewById(R.id.hero_scrollview);
        Y = heroY = 0;

        Bundle extras = getIntent().getExtras();
        action = extras.getString("action");
        if (action.equals("view")){
            map.put("name", extras.getString("name"));
            map.put("nickname", extras.getString("nickname"));
            map.put("lines", extras.getString("lines"));
            map.put("power", extras.getString("power"));
            map.put("born", extras.getString("born"));
            map.put("dead", extras.getString("dead"));
            map.put("origin", extras.getString("origin"));
            map.put("property", extras.getString("property"));
            map.put("life", extras.getString("life"));
            map.put("comment", extras.getString("comment"));
            getData();
        }

        powers = new ArrayList<Integer>();
        powers.add(R.id.power1);
        powers.add(R.id.date1);
        powers.add(R.id.power2);
        powers.add(R.id.date2);
        powers.add(R.id.power3);
        powers.add(R.id.date3);
        powers.add(R.id.power4);
        powers.add(R.id.date4);
        powers.add(R.id.power5);
        powers.add(R.id.date5);
        powers.add(R.id.hero_power1);
        powers.add(R.id.hero_date1);
        powers.add(R.id.hero_power2);
        powers.add(R.id.hero_date2);
        powers.add(R.id.hero_power3);
        powers.add(R.id.hero_date3);
        powers.add(R.id.hero_power4);
        powers.add(R.id.hero_date4);
        powers.add(R.id.hero_power5);
        powers.add(R.id.hero_date5);
        power_pointer = 0;

        propertys = new ArrayList<Integer>();
        propertys.add(R.id.pot4);
        propertys.add(R.id.property1);
        propertys.add(R.id.value1);
        propertys.add(R.id.pot5);
        propertys.add(R.id.property2);
        propertys.add(R.id.value2);
        propertys.add(R.id.pot6);
        propertys.add(R.id.property3);
        propertys.add(R.id.value3);
        propertys.add(R.id.pot7);
        propertys.add(R.id.property4);
        propertys.add(R.id.value4);
        propertys.add(R.id.pot8);
        propertys.add(R.id.property5);
        propertys.add(R.id.value5);
        propertys.add(R.id.pot9);
        propertys.add(R.id.property6);
        propertys.add(R.id.value6);
        propertys.add(R.id.pot10);
        propertys.add(R.id.property7);
        propertys.add(R.id.value7);
        propertys.add(R.id.pot11);
        propertys.add(R.id.property8);
        propertys.add(R.id.value8);
        propertys.add(R.id.pot12);
        propertys.add(R.id.property9);
        propertys.add(R.id.value9);
        propertys.add(R.id.hero_pot4);
        propertys.add(R.id.hero_property1);
        propertys.add(R.id.hero_value1);
        propertys.add(R.id.hero_pot5);
        propertys.add(R.id.hero_property2);
        propertys.add(R.id.hero_value2);
        propertys.add(R.id.hero_pot6);
        propertys.add(R.id.hero_property3);
        propertys.add(R.id.hero_value3);
        propertys.add(R.id.hero_pot7);
        propertys.add(R.id.hero_property4);
        propertys.add(R.id.hero_value4);
        propertys.add(R.id.hero_pot8);
        propertys.add(R.id.hero_property5);
        propertys.add(R.id.hero_value5);
        propertys.add(R.id.hero_pot9);
        propertys.add(R.id.hero_property6);
        propertys.add(R.id.hero_value6);
        propertys.add(R.id.hero_pot10);
        propertys.add(R.id.hero_property7);
        propertys.add(R.id.hero_value7);
        propertys.add(R.id.hero_pot11);
        propertys.add(R.id.hero_property8);
        propertys.add(R.id.hero_value8);
        propertys.add(R.id.hero_pot12);
        propertys.add(R.id.hero_property9);
        propertys.add(R.id.hero_value9);
        property_pointer = 0;

        mark = false;
        s1 = (ScrollView) findViewById(R.id.scrollview);
        r1 = (RelativeLayout) findViewById(R.id.bottomBar);
        s2 = (ScrollView) findViewById(R.id.hero_scrollview);
        r2 = (RelativeLayout) findViewById(R.id.hero_bottomBar);
        imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        mHandler = new Handler() {
            private ImageView iv;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    iv = (ImageView) findViewById(R.id.edit);
                    if (mark)
                        iv.setImageResource(R.mipmap.edit_red);
                    else
                        iv.setImageResource(R.mipmap.edit);
                }
                else if (msg.what == 2){
                    r1.setVisibility(View.GONE);
                }
                else if (msg.what == 3){
                    r1.setVisibility(View.VISIBLE);
                }
                else if (msg.what == 4) {
                    r2.setVisibility(View.GONE);
                }
                else if (msg.what == 5) {
                    r2.setVisibility(View.VISIBLE);
                }
            }
        };
        mThead = new Thread(new Runnable() {
            private boolean nmark;
            private int ny;
            private int heroNy;
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ny = scrollView.getScrollY();
                    heroNy = heroScrollView.getScrollY();
                    if (ny > Y)
                        mHandler.obtainMessage(2).sendToTarget();
                    else if (ny < Y - 30)
                        mHandler.obtainMessage(3).sendToTarget();

                    Y = ny;
                    if (heroNy > heroY)
                        mHandler.obtainMessage(4).sendToTarget();
                    else if (heroNy < heroY - 30)
                        mHandler.obtainMessage(5).sendToTarget();
                    heroY = heroNy;

                    if (mark != nmark){
                        nmark = mark;
                        mHandler.obtainMessage(1).sendToTarget();
                    }
                }
            }
        });
        mThead.start();
    }

    private void setupUI(final View view, int code) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if(imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                                0);
                    }

                    return false;
                }
            });
        } else {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    r1.setVisibility(View.GONE);
                    return false;
                }
            });
            if (code == 101) {
                ev = (EditText) findViewById(view.getId());
                ev.setEnabled(false);
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, code);
            }
        }
    }

    public void setEdit(View view) {
        mark = !mark;
        if (mark){
            loadData();
            s1.setVisibility(View.VISIBLE);
            s2.setVisibility(View.GONE);
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
        }
        else {
            s1.setVisibility(View.GONE);
            s2.setVisibility(View.VISIBLE);
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
        }
    }

    public void back(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        ev = (EditText) findViewById(R.id.hero_name);
        bundle.putString("name", ev.getText().toString());
        ev = (EditText) findViewById(R.id.hero_nickname);
        bundle.putString("nickname", ev.getText().toString());
        ev = (EditText) findViewById(R.id.hero_lines);
        bundle.putString("lines", ev.getText().toString());
        String s = "";
        for (int i=10;i<20;i+=2) {
            ev = (EditText) findViewById(powers.get(i));
            if (ev.getVisibility() == View.GONE) break;
            s += ev.getText().toString();
            ev = (EditText) findViewById(powers.get(i+1));
            s += ":";
            if (ev.getVisibility() != View.GONE)
                s += ev.getText().toString();
            s += ";";
        }
        s = s.substring(0, s.length()-1);
        bundle.putString("power", s);
        ev = (EditText) findViewById(R.id.hero_born_date);
        bundle.putString("born", ev.getText().toString());
        ev = (EditText) findViewById(R.id.hero_dead_date);
        bundle.putString("dead", ev.getText().toString());
        s = "";
        for (int i=27;i<54;i+=3) {
            ev = (EditText) findViewById(powers.get(i+1));
            if (ev.getVisibility() == View.GONE) break;
            s += ev.getText().toString();
            ev = (EditText) findViewById(powers.get(i+2));
            s += ":";
            if (ev.getVisibility() != View.GONE)
                s += ev.getText().toString();
            s += ";";
        }
        s = s.substring(0, s.length()-1);
        bundle.putString("property", s);
        ev = (EditText) findViewById(R.id.hero_life_edit);
        bundle.putString("life", ev.getText().toString());

        ev = (EditText) findViewById(R.id.hero_comment_edit);
        bundle.putString("comment", ev.getText().toString());

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void ok(final View view) {
        if (check()){
            AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
            builder.setTitle("确认修改？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    updateData();
                    mark = false;
                    s2.setVisibility(View.VISIBLE);
                    s1.setVisibility(View.GONE);
                    r2.setVisibility(View.VISIBLE);
                    r1.setVisibility(View.GONE);
                    if (action.equals("add"))
                        back(view);
                }
            });
            builder.create().show();
        } else
            Toast.makeText(TableActivity.this, "名字不能为空", Toast.LENGTH_SHORT).show();
    }

    public void no(View view) {
        mark = false;
        s2.setVisibility(View.VISIBLE);
        s1.setVisibility(View.GONE);
        r2.setVisibility(View.VISIBLE);
        r1.setVisibility(View.GONE);
        if (action.equals("add"))
            back(view);
    }

    public boolean check() {
        ev = (EditText) findViewById(R.id.name);
        if (ev.getText().toString().length() == 0)
            return false;
        return true;
    }

    public void loadData() {
        bitmap = image;
        if (bitmap != null) mImageView.setImageBitmap(bitmap);
        mImageView.setBackgroundResource(R.drawable.image_background);
        ev = (EditText) findViewById(R.id.name);
        ev1 = (EditText) findViewById(R.id.hero_name);
        ev.setText(ev1.getText());
        ev = (EditText) findViewById(R.id.nickname);
        ev1 = (EditText) findViewById(R.id.hero_nickname);
        ev.setText(ev1.getText());
        ev = (EditText) findViewById(R.id.lines);
        ev1 = (EditText) findViewById(R.id.hero_lines);
        ev.setText(ev1.getText());
        int j = 0;
        for (int i=10;i<20;i+=2){
            ev = (EditText) findViewById(powers.get(i));
            ev1 = (EditText) findViewById(powers.get(i+1));
            if (ev.getVisibility() == View.GONE) break;
            if (ev.getText().toString().length() > 0) {
                ev1 = (EditText) findViewById(powers.get(j));
                ev1.setText(ev.getText());
                ev1.setVisibility(View.VISIBLE);
                ++j;
                ev = (EditText) findViewById(powers.get(i+1));
                ev1 = (EditText) findViewById(powers.get(j));
                ev1.setText(ev.getText());
                ev1.setVisibility(View.VISIBLE);
                ++j;
            }
        }
        power_pointer = j;
        while (j < 10) {
            ev = (EditText) findViewById(powers.get(j));
            ev.setText("");
            ev.setVisibility(View.GONE);
            ++j;
        }
        ev = (EditText) findViewById(R.id.born_date);
        ev1 = (EditText) findViewById(R.id.hero_born_date);
        ev.setText(ev1.getText());
        ev = (EditText) findViewById(R.id.dead_date);
        ev1 = (EditText) findViewById(R.id.hero_dead_date);
        ev.setText(ev1.getText());
        ev = (EditText) findViewById(R.id.age);
        ev1 = (EditText) findViewById(R.id.hero_age);
        ev.setText(ev1.getText());
        ev = (EditText) findViewById(R.id.address);
        ev1 = (EditText) findViewById(R.id.hero_address);
        ev.setText(ev1.getText());
        j = 0;
        for (int i=27;i<54;i+=3) {
            ev = (EditText) findViewById(propertys.get(i+1));
            ev1 = (EditText) findViewById(propertys.get(i+2));
            if (ev.getVisibility() == View.GONE) break;
            if (ev.getText().toString().length() > 0 && ev1.getText().toString().length() > 0) {
                iv = (ImageView) findViewById(propertys.get(j));
                iv.setVisibility(View.VISIBLE);
                ++j;
                ev1 = (EditText) findViewById(propertys.get(j));
                ev1.setText(ev.getText());
                ev1.setVisibility(View.VISIBLE);
                ++j;
                ev = (EditText) findViewById(propertys.get(i+2));
                ev1 = (EditText) findViewById(propertys.get(j));
                ev1.setText(ev.getText());
                ev1.setVisibility(View.VISIBLE);
                ++j;
            }
        }
        property_pointer = j;
        while (j < 27) {
            iv = (ImageView) findViewById(propertys.get(j));
            iv.setVisibility(View.GONE);
            ++j;
            ev = (EditText) findViewById(propertys.get(j));
            ev.setText("");
            ev.setVisibility(View.GONE);
            ++j;
            ev = (EditText) findViewById(propertys.get(j));
            ev.setText("");
            ev.setVisibility(View.GONE);
            ++j;
        }

        ev = (EditText) findViewById(R.id.life_edit);
        ev1 = (EditText) findViewById(R.id.hero_life_edit);
        ev.setText(ev1.getText());
        ev = (EditText) findViewById(R.id.comment_edit);
        ev1 = (EditText) findViewById(R.id.hero_comment_edit);
        ev.setText(ev1.getText());

    }

    public void updateData() {
        image = bitmap;
        if (image != null) mImageView2.setImageBitmap(image);
        mImageView2.setBackgroundResource(R.drawable.image_background);
        ev = (EditText) findViewById(R.id.name);
        ev1 = (EditText) findViewById(R.id.hero_name);
        ev1.setText(ev.getText());
        ev = (EditText) findViewById(R.id.nickname);
        ev1 = (EditText) findViewById(R.id.hero_nickname);
        ev1.setText(ev.getText());
        ev = (EditText) findViewById(R.id.lines);
        ev1 = (EditText) findViewById(R.id.hero_lines);
        ev1.setText(ev.getText());
        int j = 10;
        for (int i=0;i<10;i+=2){
            ev = (EditText) findViewById(powers.get(i));
            ev1 = (EditText) findViewById(powers.get(i+1));
            if (ev.getVisibility() == View.GONE) break;
            if (ev.getText().toString().length() > 0) {
                ev1 = (EditText) findViewById(powers.get(j));
                ev1.setText(ev.getText());
                ev1.setVisibility(View.VISIBLE);
                ++j;
                ev = (EditText) findViewById(powers.get(i+1));
                ev1 = (EditText) findViewById(powers.get(j));
                ev1.setText(ev.getText());
                if (ev1.getText().toString().length() > 0) ev1.setVisibility(View.VISIBLE);
                else ev1.setVisibility(View.GONE);
                ++j;
            }
        }
        while (j < 20) {
            ev = (EditText) findViewById(powers.get(j));
            ev.setText("");
            ev.setVisibility(View.GONE);
            ++j;
        }
        ev = (EditText) findViewById(R.id.born_date);
        ev1 = (EditText) findViewById(R.id.hero_born_date);
        ev1.setText(ev.getText());
        ev = (EditText) findViewById(R.id.dead_date);
        ev1 = (EditText) findViewById(R.id.hero_dead_date);
        ev1.setText(ev.getText());
        ev = (EditText) findViewById(R.id.age);
        ev1 = (EditText) findViewById(R.id.hero_age);
        ev1.setText(ev.getText());
        ev = (EditText) findViewById(R.id.address);
        ev1 = (EditText) findViewById(R.id.hero_address);
        ev1.setText(ev.getText());

        j = 27;
        for (int i=0;i<27;i+=3) {
            ev = (EditText) findViewById(propertys.get(i+1));
            ev1 = (EditText) findViewById(propertys.get(i+2));
            if (ev.getVisibility() == View.GONE) break;
            if (ev.getText().toString().length() > 0 && ev1.getText().toString().length() > 0) {
                iv = (ImageView) findViewById(propertys.get(j));
                iv.setVisibility(View.VISIBLE);
                ++j;
                ev1 = (EditText) findViewById(propertys.get(j));
                ev1.setText(ev.getText());
                ev1.setVisibility(View.VISIBLE);
                ++j;
                ev = (EditText) findViewById(propertys.get(i+2));
                ev1 = (EditText) findViewById(propertys.get(j));
                ev1.setText(ev.getText());
                ev1.setVisibility(View.VISIBLE);
                ++j;
            }
        }
        while (j < 54) {
            iv = (ImageView) findViewById(propertys.get(j));
            iv.setVisibility(View.GONE);
            ++j;
            ev = (EditText) findViewById(propertys.get(j));
            ev.setText("");
            ev.setVisibility(View.GONE);
            ++j;
            ev = (EditText) findViewById(propertys.get(j));
            ev.setText("");
            ev.setVisibility(View.GONE);
            ++j;
        }
        ev = (EditText) findViewById(R.id.life_edit);
        ev1 = (EditText) findViewById(R.id.hero_life_edit);
        ev1.setText(ev.getText());
        ev = (EditText) findViewById(R.id.comment_edit);
        ev1 = (EditText) findViewById(R.id.hero_comment_edit);
        ev1.setText(ev.getText());

    }

    public void add_power(View view) {
        if (power_pointer < 10) {
            ev = (EditText) findViewById(powers.get(power_pointer));
            ev.setVisibility(View.VISIBLE);
            ++power_pointer;
            ev = (EditText) findViewById(powers.get(power_pointer));
            ev.setVisibility(View.VISIBLE);
            ++power_pointer;
        }
    }

    public void add_property(View view) {
        if (property_pointer < 27) {
            iv = (ImageView) findViewById(propertys.get(property_pointer));
            iv.setVisibility(View.VISIBLE);
            ++property_pointer;
            ev = (EditText) findViewById(propertys.get(property_pointer));
            ev.setVisibility(View.VISIBLE);
            ++property_pointer;
            ev = (EditText) findViewById(propertys.get(property_pointer));
            ev.setVisibility(View.VISIBLE);
            ++property_pointer;
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            //获取照片
            case R.id.select:
                getImageFromAlbum();
                break;
        }
    }

    public  void getImageFromAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent,REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    bitmap = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageOnKitKat(this, data);        //ImgUtil是自己实现的一个工具类
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageBeforeKitKat(this, data);
                    }
                    ImageView view = (ImageView) findViewById(R.id.rect);
                    view.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }
    }

    public void love(View v) {
        iv = (ImageView) findViewById(R.id.hero_love);
        if (iv.getTag().toString().equals("0")){
            iv.setImageResource(R.mipmap.loved);
            iv.setTag("1");
        } else {
            iv.setImageResource(R.mipmap.love);
            iv.setTag("0");
        }
    }

    public void getData() {
        ev = (EditText) findViewById(R.id.name);
        ev.setText(map.get("hero_name"));
        ev = (EditText) findViewById(R.id.nickname);
        ev.setText(map.get("hero_nickname"));
        ev = (EditText) findViewById(R.id.lines);
        ev.setText(map.get("hero_lines"));
        String[] power = map.get("hero_power").split(";");
        String[] item;
        int j = 10;
        for (int i=0;i<power.length;++i) {
            item = power[i].split(":");
            ev = (EditText) findViewById(powers.get(j));
            ev.setText(item[0]);
            ev.setVisibility(View.VISIBLE);
            ++j;
            ev = (EditText) findViewById(powers.get(j));
            ev.setText(item[1]);
            ev.setVisibility(View.VISIBLE);
            ++j;
        }
        ev = (EditText) findViewById(R.id.hero_born_date);
        ev.setText(map.get("born"));
        ev = (EditText) findViewById(R.id.hero_dead_date);
        ev.setText(map.get("dead"));

        j = 27;
        String[] property = map.get("prperty").split(";");
        for (int i=0;i<property.length;++i) {
            item = property[i].split(":");
            iv = (ImageView) findViewById(propertys.get(j));
            iv.setVisibility(View.VISIBLE);
            ++j;
            ev = (EditText) findViewById(propertys.get(j));
            ev.setText(item[0]);
            ev.setVisibility(View.VISIBLE);
            ++j;
            ev = (EditText) findViewById(propertys.get(j));
            ev.setText(item[1]);
            ev.setVisibility(View.VISIBLE);
            ++j;
        }

        ev = (EditText) findViewById(R.id.hero_life_edit);
        ev.setText(map.get("life"));

        ev = (EditText) findViewById(R.id.hero_comment_edit);
        ev.setText(map.get("comment"));

    }

}
