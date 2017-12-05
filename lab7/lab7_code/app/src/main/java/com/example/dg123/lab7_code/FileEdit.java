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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
    }

    public void buttonClick(View view) {
        name = (EditText) findViewById(R.id.name);
        content = (EditText) findViewById(R.id.body);
        if (view.getId() == R.id.save && name.getText().toString().length() > 0) {
            try (FileOutputStream fileOutputStream = openFileOutput(name.getText().toString().trim(), MODE_PRIVATE)) {
                fileOutputStream.write(content.getText().toString().getBytes());
                Toast.makeText(this, "Save successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Fail to save file", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.load) {
            try (FileInputStream fileInputStream = openFileInput(name.getText().toString().trim())) {
                byte[] contents = new byte[fileInputStream.available()];
                fileInputStream.read(contents);
                content.setText(new String(contents));
                Toast.makeText(this, "Load successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Fail to load file", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.clear) {
            content.setText("");
        }
        else if (view.getId() == R.id.delete) {
            try {
                Context context = getApplicationContext();
                context.deleteFile(name.getText().toString());
                Toast.makeText(this, "Delete successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Fail to delete file", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
