package com.example.dg123.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditActivity extends AppCompatActivity {
    final String imgTagStart = "![](";
    final String imgTagEnd = ")";
    final int chooseFromAlbum = 99;
    final int chooseFromTag = 88;
    private String uid;
    ArrayList<Bundle> infoList = new ArrayList<>();
    ArrayList<Uri> uriList = new ArrayList<>();
    Set<String> tagList = new HashSet<>();
    int pos = 0;
    final resAdapter adapter = new resAdapter(EditActivity.this, R.layout.resource_item, infoList);
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
        setContentView(R.layout.activity_edit);
        changeTheme();
        Bundle bun = getIntent().getExtras();
        uid = bun.getString("uid");
        final EditText title = (EditText) findViewById(R.id.title);
        final Button show = (Button) findViewById(R.id.show);
        final EditText content = (EditText) findViewById(R.id.content);
        final Button add_res = (Button) findViewById(R.id.add_res);
        final RecyclerView res = (RecyclerView)findViewById(R.id.res);
        final Button publish = (Button) findViewById(R.id.publish);
        add_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int permission = ActivityCompat.checkSelfPermission(EditActivity.this, "android.permission.READ_EXTERNAL_STORAGE");
                if(permission != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(EditActivity.this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                else {
                    openAlbum();
                    ++pos;
                }
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(title.getText().toString().equals("")) Toast.makeText(EditActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                else if(content.getText().toString().equals("")) Toast.makeText(EditActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                else {
                    final Intent intent = new Intent(EditActivity.this, preViewActivity.class);
                    Article1 data = new Article1();
                    data.setTitle(title.getText().toString());
                    data.setContent(content.getText().toString());
                    final int len = infoList.size();
                    ArrayList<String> path = new ArrayList<>();
                    ArrayList<String> uri = new ArrayList<>();
                    for(int i = 0; i < len; ++i) {
                        path.add(infoList.get(i).getString("path"));
                        uri.add(uriList.get(i).toString());
                    }
                    data.setPath(path);
                    data.setUriList(uri);
                    intent.putExtra("info", data);
                    startActivity(intent);
                }
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(title.getText().toString().equals("")) Toast.makeText(EditActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                else if(content.getText().toString().equals("")) Toast.makeText(EditActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                else {
                    final int len = uriList.size();
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    final String contentName = content.getText().toString();
                    String contentPost = contentName;
                    for(int i = 0; i < len; ++i) {
                        final String imgName = "" + infoList.get(i).getString("path");
                        final String oldPath = imgTagStart + imgName + imgTagEnd;
                        String img_name = imgName;
                        while(img_name.contains("/")) img_name = img_name.substring(img_name.indexOf("/") + 1);
                        final String newPath = imgTagStart + img_name + imgTagEnd;
                        while (contentPost.contains(oldPath)) {
                            final int start = contentPost.indexOf(oldPath);
                            final int length = oldPath.length();
                            contentPost = contentPost.substring(0, start) + newPath + contentPost.substring(start + length);
                        }
                    }
                    builder.addFormDataPart("title", title.getText().toString())
                            .addFormDataPart("content", contentPost)
                            .addFormDataPart("user", uid);
                    for(int i = 0; i < len; ++i) {
                        final String filePath = getImgPath(uriList.get(i));
                        final String imgName = "" + infoList.get(i).getString("path");
                        if(contentName.contains(imgTagStart + imgName + imgTagEnd)) {
                            final File file = new File(filePath);
                            final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                            String name = file.getName();
                            while(name.contains("/")) name = name.substring(name.indexOf("/") + 1);
                            builder.addFormDataPart("file", name, requestBody);
                        }
                    }
                    String tags = "";
                    for (String key : tagList)
                        tags += key + ";";
                    if (tags != null && !tags.equals(""))
                        builder.addFormDataPart("tags", tags.substring(0, tags.length()-1));
                    else
                        builder.addFormDataPart("tags", "");
                    final RequestBody requestBody = builder.build();
                    final ProgressDialog dialog = new ProgressDialog(EditActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("正在上传，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();
                    PostService service = createRetrofit().create(PostService.class);
                    service.uploadFileWithPartMap(requestBody)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ResponseBody>() {
                                @Override
                                public void onCompleted() {
                                    dialog.dismiss();
                                    Toast.makeText(EditActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onError(final Throwable e) {
                                    dialog.dismiss();
                                    Toast.makeText(EditActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onNext(ResponseBody responseBody) {}
                            });
                }
            }
        });
        res.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new resAdapter.OnItemClickListener() {
            @Override
            public void onClick(final View view, final int position) {
                final int index = content.getSelectionStart();
                Editable edit_text = content.getEditableText();
                edit_text.insert(index, imgTagStart + infoList.get(position).getString("path") + imgTagEnd);
            }
            @Override
            public void onLongClick(final View view, final int position) {
                final String path = imgTagStart + infoList.get(position).getString("path") + imgTagEnd;
                infoList.remove(position);
                uriList.remove(position);
                String article = content.getText().toString();
                while(article.contains(path)) {
                    final String strStart = article.substring(0, article.indexOf(path));
                    final String strEnd = article.substring(strStart.length() + path.length());
                    article = strStart + strEnd;
                }
                content.setText(article);
                adapter.notifyDataSetChanged();
            }
        });
        res.setAdapter(adapter);
    }
    public void changeTheme() {
        SharedPreferences sharedPref;
        Context context;
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("COLOR", Context.MODE_PRIVATE);
        int color = sharedPref.getInt("color", 1);
        if (color == 1) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.RIKYUSHIRACHA));
        }
        else if (color == 2) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.NADESHIKO));
        }
        else if (color == 3) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.SHION));
        }
        else if (color == 4) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_topbar);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.MIZUASAGI));
        }
    }

    public void back(View view) {
        finish();
    }

    private void openAlbum() {
        final Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, chooseFromAlbum);
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String permissions[], @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 999:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) openAlbum();
                else Toast.makeText(EditActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch(requestCode) {
            case chooseFromAlbum:
                if(resultCode == RESULT_OK) {
                    final Uri uri = data.getData();
                    assert uri != null;
                    uriList.add(uri);
                    Bundle bundle = new Bundle();
                    bundle.putString("path", getImgPath(uri));
                    bundle.putString("Uri", uri.toString());
                    infoList.add(bundle);
                    adapter.notifyDataSetChanged();
                }
                break;
            case chooseFromTag:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    tagList.clear();
                    if (bundle != null) {
                        for (String key : bundle.keySet())
                            tagList.add(key);
                    }
                }
                break;
        }
    }
    @NonNull
    static Retrofit createRetrofit() {
        return new Retrofit.Builder().baseUrl("http://172.18.92.122:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .build())
                .build();
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

    public void tagChoose(View viiew) {
        Intent intent = new Intent(EditActivity.this, TagChoosingActivity.class);
        for (String str : tagList)
            intent.putExtra(str, true);
        startActivityForResult(intent, chooseFromTag);
    }
}
