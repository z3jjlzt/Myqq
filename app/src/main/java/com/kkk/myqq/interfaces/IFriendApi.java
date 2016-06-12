package com.kkk.myqq.interfaces;

import com.kkk.myqq.entity.Friend;
import com.kkk.myqq.entity.setHeadResult;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by kkk on 2016/5/23.
 * z3jjlzt.github.io
 */
public interface IFriendApi {

    /**
     * 得到好友列表
     * @return
     */
    @GET("/Myqq/getAllUser.php")
    Observable<List<Friend>> getFriend();

    @Multipart
    @POST("/Myqq/setHead.php")
    Observable<setHeadResult> setHead(@Part("image\"; filename=\"lzt.jpg") RequestBody file);
}
