package com.example.dg123.myapplication;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InfoActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<Map<String, String>> list;
    private String uid;
    private TextView textView;
    private ImageView imageView;
    private static final int chooseFromAlbum1 = 99;
    private static final int chooseFromAlbum2 = 98;
    private static final String baseUrl = "http://172.18.92.122:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        setContentView(R.layout.activity_info);
        changeTheme();
        recyclerView = (RecyclerView) findViewById(R.id.info_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        listAdapter = new ListAdapter(InfoActivity.this, R.layout.info_list_item, list) {
            @Override
            public void convert(ViewHolder holder, int pos) {
                TextView textView = holder.getView(R.id.info_key);
                ImageView imageView = holder.getView(R.id.info_image);
                imageView.setVisibility(View.GONE);
                String key = mDatas.get(pos).get("key");
                textView.setText(key);
                String value = mDatas.get(pos).get("value");
                textView = holder.getView(R.id.info_content);
                if (key.equals("头像") || key.equals("封面")) {
                    textView.setVisibility(View.GONE);
                    if (key.equals("头像")) {
                        imageView.setVisibility(View.VISIBLE);
                        if (!value.equals("")) getImage(value, imageView);
                    }
                }
                else textView.setText(value);
            }
        };
        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (position == 0) {
                    openAlbum(chooseFromAlbum1);
                } else if (position == 1) {
                    Intent intent = new Intent(InfoActivity.this, TextActivity.class);
                    intent.putExtra("title", list.get(position).get("key"));
                    intent.putExtra("content", list.get(position).get("value"));
                    intent.putExtra("uid", uid);
                    startActivityForResult(intent, 1);
                } else if (position == 3) {
                    Intent intent = new Intent(InfoActivity.this, TextActivity.class);
                    intent.putExtra("title", list.get(position).get("key"));
                    intent.putExtra("content", list.get(position).get("value"));
                    intent.putExtra("uid", uid);
                    startActivityForResult(intent, 1);
                } else if (position == 4) {
                    openAlbum(chooseFromAlbum2);
                }
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        recyclerView.setAdapter(listAdapter);
        getDatas();
        setInfo();
    }
    public void back(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
    public void getDatas() {
        final SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(uid, Context.MODE_PRIVATE);
        list.add(new HashMap<String, String>(){{
            put("key", "头像");
            put("value", sharedPref.getString("head", ""));
        }});
        list.add(new HashMap<String, String>(){{
            put("key", "昵称");
            put("value", sharedPref.getString("name", ""));
        }});
        list.add(new HashMap<String, String>(){{
            put("key", "UID");
            put("value", sharedPref.getString("uid", ""));
        }});
        list.add(new HashMap<String, String>(){{
            put("key", "个性签名");
            put("value", sharedPref.getString("motto", ""));
        }});
        list.add(new HashMap<String, String>(){{
            put("key", "封面");
            put("value", "");
        }});
        listAdapter.notifyDataSetChanged();
    }
    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        int color = sharedPref.getInt("color", 1);
        relativeLayout = (RelativeLayout) findViewById(R.id.info_topbar);
        if (color == 1) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        } else if (color == 2) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        } else if (color == 3) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
        } else if (color == 4) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }

    public void getImage(String url, ImageView imageView) {
        Glide.with(InfoActivity.this)
                .load(baseUrl + url)
                .crossFade()
                .error(R.color.main)
                .into(imageView);
    }

    public void setInfo() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        uid = sharedPref.getString("uid", "");
        sharedPref = context.getSharedPreferences(uid, Context.MODE_PRIVATE);
        Map<String, String> map;
        map = list.get(1);
        map.put("value", sharedPref.getString("name", ""));
        map = list.get(2);
        map.put("value", sharedPref.getString("uid", ""));
        map = list.get(3);
        map.put("value", sharedPref.getString("motto", ""));
        map = list.get(0);
        map.put("value", sharedPref.getString("head", ""));
        map = list.get(4);
        map.put("value", sharedPref.getString("cover", ""));
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if (resultCode == RESULT_OK){
                changeTheme();
                setInfo();
            }
        } else if (requestCode == chooseFromAlbum1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                assert uri != null;
                save(uid, "head", uri);
            }
        }
        else if (requestCode == chooseFromAlbum2) {
            if(resultCode == RESULT_OK) {
                Uri uri = data.getData();
                assert uri != null;
                save(uid, "cover", uri);
            }
        }
    }

    private void openAlbum(int chooseFromAlbum) {
        final Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, chooseFromAlbum);
    }

    private void save(final String uid, final String key, final Uri uri) {
        PostService service = ServiceFactory.createPostService();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(getImgPath(uri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        builder.addFormDataPart("file", "img", requestBody);
        builder.addFormDataPart("uid", uid);
        builder.addFormDataPart("key", key);
        final RequestBody request = builder.build();
        service.uploadImage(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImageUrl>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(InfoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(ImageUrl imageUrl) {
                        Toast.makeText(InfoActivity.this, "上传图片成功", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPref;
                        Context context;
                        context = getApplicationContext();
                        sharedPref = context.getSharedPreferences(uid, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(key, imageUrl.getUri());
                        editor.commit();
                        setInfo();
                    }
                });
    }

    String getImgPath(final Uri uri) {
        if(Build.VERSION.SDK_INT >= 19) {
            String filePath = null;
            if(DocumentsContract.isDocumentUri(this, uri)) {
                final String documentId = DocumentsContract.getDocumentId(uri);
                if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    final String id = documentId.split(":")[1];
                    final String selection = MediaStore.Images.Media._ID + "=?";
                    final String[] selectionArgs = { id };
                    filePath = getDataColumn(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
                } else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                    filePath = getDataColumn(this, contentUri, null, null);
                }
            } else if("content".equalsIgnoreCase(uri.getScheme())) filePath = getDataColumn(this, uri, null, null);
            else if("file".equals(uri.getScheme())) filePath = uri.getPath();
            return filePath;
        } else return getDataColumn(this, uri, null, null);
    }
    private static String getDataColumn(final Context context, final Uri uri, final String selection, final String[] selectionArgs) {
        String path = null;
        final String[] projection = new String[] { MediaStore.Images.Media.DATA };
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if(cursor != null && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch(Exception e) {
            if(cursor != null) cursor.close();
        }
        return path;
    }
}
