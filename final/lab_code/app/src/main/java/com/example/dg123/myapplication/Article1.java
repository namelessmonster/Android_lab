package com.example.dg123.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Article1 implements Serializable {
    private String title;
    private String content;
    private ArrayList<String> uriList;
    private ArrayList<String> path;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    String getContent() {
        return content;
    }
    void setContent(String content) {
        this.content = content;
    }
    ArrayList<String> getUriList() {
        return uriList;
    }
    void setUriList(ArrayList<String> uriList) {
        this.uriList = uriList;
    }
    ArrayList<String> getPath() {
        return path;
    }
    void setPath(ArrayList<String> path) {
        this.path = path;
    }
}
