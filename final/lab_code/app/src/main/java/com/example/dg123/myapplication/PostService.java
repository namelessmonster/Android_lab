package com.example.dg123.myapplication;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface PostService {
    @POST("saveNews.jsp")
    Observable<ResponseBody> uploadFileWithPartMap(@Body RequestBody file);
    @POST("saveImage.jsp")
    Observable<ImageUrl> uploadImage(@Body RequestBody file);
}
