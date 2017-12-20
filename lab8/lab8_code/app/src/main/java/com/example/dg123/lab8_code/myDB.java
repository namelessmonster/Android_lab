package com.example.dg123.lab8_code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myDB extends SQLiteOpenHelper{
    private static final String DB_NAME = "Contacts.db";
    private static final String TABLE_NAME = "Contacts";
    private static final int DB_VERSION = 1;

    public myDB(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version){
        super(context, DB_NAME, cursorFactory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME
                + "(name text, "
                + "birthday text, "
                + "gift text);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // TO DO
    }

    public void insert(String name, String birthday, String gift) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birthday", birthday);
        values.put("gift", gift);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void update(String s, String name, String birthday, String gift) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = { s };
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birthday", birthday);
        values.put("gift", gift);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = { name };
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public List<Map<String, String>> query() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"name", "birthday", "gift"},
                null, null, null, null, null);

        List<Map<String, String>> list = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
                final String name = cursor.getString(cursor.getColumnIndex("name"));
                final String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                final String gift = cursor.getString(cursor.getColumnIndex("gift"));
                list.add(new HashMap<String, String>(){{
                    put("name", name);
                    put("birthday", birthday);
                    put("gift", gift);
                }});
        }
        db.close();
        return list;
    }
}
