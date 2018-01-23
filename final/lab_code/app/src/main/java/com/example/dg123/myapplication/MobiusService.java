package com.example.dg123.myapplication;

import android.graphics.Bitmap;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface MobiusService {
    @POST("getNews.jsp")
    @FormUrlEncoded
    Observable<List<News>> getNews(@Field("key") String key,
                                   @Field("offset") String offset,
                                   @Field("tot") String tot);

    @POST("registerUser.jsp")
    @FormUrlEncoded
    Observable<User> registerUser(@Field("uid") String uid,
                                  @Field("pass") String pass);

    @POST("login.jsp")
    @FormUrlEncoded
    Observable<User> login(@Field("uid") String uid,
                             @Field("pass") String pass);

    @POST("saveInfo.jsp")
    @FormUrlEncoded
    Observable<ResponseBody> saveInfo(@Field("uid") String uid,
                                      @Field("key") String key,
                                      @Field("value") String value);

    @POST("saveComment.jsp")
    @FormUrlEncoded
    rx.Observable<Comment> uploadComment(@Field("uid") String uid,
                                         @Field("nid") String nid,
                                         @Field("content") String content,
                                         @Field("reply") String reply);

    @POST("getComments.jsp")
    @FormUrlEncoded
    rx.Observable<List<Comment>> loadComment(@Field("nid") String nid);

    @POST("getArticle.jsp")
    @FormUrlEncoded
    rx.Observable<Article> getArticle(@Field("url") String url);

    @POST("getNewsForUid.jsp")
    @FormUrlEncoded
    Observable<List<News>> getNewsForUid(@Field("uid") String uid);

    @POST("getCommentsForUid.jsp")
    @FormUrlEncoded
    Observable<List<Comment>> getCommentsForUid(@Field("uid") String uid);
}
