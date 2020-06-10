package com.tutou.bigwork;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tutou.bigwork.api.ApiService;
import com.tutou.bigwork.api.entity.CheckFavDto;
import com.tutou.bigwork.api.entity.Video;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tutou.bigwork.config.AuthConfig.TIKTOK_URI;
import static com.tutou.bigwork.config.AuthConfig.IS_LOGIN;
import static com.tutou.bigwork.config.AuthConfig.X_TIKTOK_TOKEN;

/**
 * @author Sobinbin,Songyang
 */
public class VideoActivity extends AppCompatActivity{

    public static Set<String> FAV_VIDEO_SET = new HashSet<>();

    private ViewPager2 viewPager;

    private VideoAdapter adapter;

    private List<Video> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if(IS_LOGIN){
            getMyFavVideo();
        }

        View decorView = getWindow().getDecorView();
        // SYSTEM_UI_FLAG_FULLSCREEN表示全屏的意思，也就是会将状态栏隐藏
        // 设置系统UI元素的可见性
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        viewPager = findViewById(R.id.vp);

        adapter = new VideoAdapter(list);
        viewPager.setAdapter(adapter);

        getData();


    }

    @Override
    protected void onStop() {
        super.onStop();
        IS_LOGIN = false;
        FAV_VIDEO_SET.clear();
    }

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getVideo().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                list.addAll(response.body());
                adapter.notifyDataSetChanged();     // 异步操作，获取完数据后需刷新
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
            }
        });
    }

    private void getMyFavVideo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TIKTOK_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.myFavVideo(X_TIKTOK_TOKEN).enqueue(new Callback<CheckFavDto>() {
            @Override
            public void onResponse(Call<CheckFavDto> call, Response<CheckFavDto> response) {
                CheckFavDto checkFavDto = response.body();
                if(checkFavDto.errno != 0){
                    Toast.makeText(VideoActivity.this,response.body().errmsg,Toast.LENGTH_SHORT).show();
                } else {
                    FAV_VIDEO_SET.clear();
                    FAV_VIDEO_SET.addAll(checkFavDto.videoIdList);
                }
            }

            @Override
            public void onFailure(Call<CheckFavDto> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
            }
        });
    }
}
