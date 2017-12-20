package com.example.dg123.lab7_code;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEdit extends AppCompatActivity {

    private EditText name;
    private EditText content;
    private final int SIZE = 1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
    }

    public void buttonClick(View view) {
        name = (EditText) findViewById(R.id.name);
        content = (EditText) findViewById(R.id.body);
        if (view.getId() == R.id.save) {    //点击SAVE按钮
            try (FileOutputStream fileOutputStream
                         = openFileOutput(name.getText().toString().trim(),
                    MODE_PRIVATE)) {
                //将文本编辑区的内容输出到文件名指定的文件
                fileOutputStream.write(content.getText().toString().getBytes());
                fileOutputStream.flush();
                Toast.makeText(this, "Save successfully", Toast.LENGTH_SHORT).show();
                fileOutputStream.close();
            } catch (IOException e) {
                Toast.makeText(this, "Fail to save file", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.load) {   //点击LOAD按钮
            try (FileInputStream fileInputStream
                         = openFileInput(name.getText().toString().trim())) {
                byte[] contents = new byte[SIZE];
                StringBuilder sb = new StringBuilder();
                int len;
                //一次读取1K数据，循环读取直到文件所有内容读取完毕
                while ((len = fileInputStream.read(contents)) != -1)
                    sb.append(new String(contents, 0, len));
                //将读取的文件内容写入文本编辑区
                content.setText(sb.toString());
                Toast.makeText(this, "Load successfully", Toast.LENGTH_SHORT).show();
                fileInputStream.close();
            } catch (IOException e) {
                Toast.makeText(this, "Fail to load file", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.clear) {
            content.setText("");
        }
        else if (view.getId() == R.id.delete) { //点击delete按钮
            try {
                Context context = getApplicationContext();
                //获取context，调用deleteFile方法删除文件名指定文件
                context.deleteFile(name.getText().toString().trim());
                Toast.makeText(this, "Delete successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Fail to delete file", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
