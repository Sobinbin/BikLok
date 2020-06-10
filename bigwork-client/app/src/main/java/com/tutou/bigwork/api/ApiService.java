package com.tutou.bigwork.api;

import com.tutou.bigwork.api.entity.CheckFavDto;
import com.tutou.bigwork.api.entity.LoginDto;
import com.tutou.bigwork.api.entity.RegisterDto;
import com.tutou.bigwork.api.entity.ToggleFavDto;
import com.tutou.bigwork.api.entity.Video;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * @author Sobinbin
 */
public interface ApiService {

    /**
     * @Description: 获取视频
     */
    @GET("api/invoke/video/invoke/video")
    Call<List<Video>> getVideo();

    /**
     * @Description: 登陆 返回JWT
     */
    @POST("tiktok/user/login")
    Call<LoginDto> login(@Body RequestBody body);

    /**
     * @Description: 注册 errno为0 注册成功
     */
    @POST("tiktok/user/register")
    Call<RegisterDto> register(@Body RequestBody body);

    /**
     * @Description: 添加或移除收藏
     */
    @POST("tiktok/fav/video/toggle")
    Call<ToggleFavDto> toggleVideoFavorite(@Header("X-TIKTOK-Token")String token,
                                           @Body RequestBody body);

    /**
     * @Description: 获取我喜欢的视频id列表
     */
    @GET("tiktok/fav/video/mine")
    Call<CheckFavDto> myFavVideo(@Header("X-TIKTOK-Token")String token);
}
