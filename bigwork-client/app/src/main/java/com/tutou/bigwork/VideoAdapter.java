package com.tutou.bigwork;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tutou.bigwork.api.ApiService;
import com.tutou.bigwork.api.entity.ToggleFavDto;
import com.tutou.bigwork.api.entity.Video;
import com.tutou.bigwork.player.VideoPlayerListener;
import com.tutou.bigwork.util.ImageUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import com.tutou.bigwork.player.VideoPlayerIJK;

import org.json.JSONObject;

import static com.tutou.bigwork.VideoActivity.FAV_VIDEO_SET;
import static com.tutou.bigwork.config.AuthConfig.TIKTOK_URI;
import static com.tutou.bigwork.config.AuthConfig.IS_LOGIN;
import static com.tutou.bigwork.config.AuthConfig.X_TIKTOK_TOKEN;
import static com.tutou.bigwork.util.AnimatorUtil.alpha;
import static com.tutou.bigwork.util.AnimatorUtil.rotation;
import static com.tutou.bigwork.util.AnimatorUtil.scale;
import static com.tutou.bigwork.util.AnimatorUtil.translationY;

/**
 * @author Sobinbin,Songyang
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<Video> list;

    public VideoAdapter(List<Video> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, description, likeCount;  // 名称、简介、收藏数
        CircleImageView avatar;     // 头像
        VideoPlayerIJK player;      // 播放器
        ImageView isFav;            // 显示收藏
        GestureDetector detector;   // 监听器

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.video_name);
            description = itemView.findViewById(R.id.video_description);
            likeCount = itemView.findViewById(R.id.like_count);
            avatar = itemView.findViewById(R.id.video_avatar);
            player = itemView.findViewById(R.id.ijkPlayer);
            isFav = itemView.findViewById(R.id.is_fav);
        }

        public void bind(int position) {
            Video video = list.get(position);
            name.setText(video.name);
            description.setText(video.description);

            // 为防止收藏数显示过大 对于大于10000的数据显示w+
            StringBuilder sbl = new StringBuilder();
            if(video.likeCount > 10000){
                sbl.append(video.likeCount/10000);
                sbl.append("w+");
            } else {
                sbl.append(video.likeCount);
            }

            likeCount.setText(sbl.toString());
            ImageUtil.setImageToImageView(avatar, video.avatar);

            // 若收藏列表中含有该视频 桃心显示为红色
            if(FAV_VIDEO_SET.contains(video.id)){
                isFav.setColorFilter(Color.RED);
            }

            initDetector(video.id);
            player.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    detector.onTouchEvent(motionEvent);
                    return true;
                }
            });

            //加载native库
            try {
                IjkMediaPlayer.loadLibrariesOnce(null);
                IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.setListener(new VideoPlayerListener());
            player.setVideoPath(video.url);
        }

        private void initDetector(String videoId) {
            detector = new GestureDetector(new GestureDetector.OnGestureListener(){
                @Override
                public boolean onDown(MotionEvent motionEvent) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent motionEvent) {
                }

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent motionEvent) {

                }

                @Override
                public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                    return true;
                }
            });

            detector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                    Log.d("touch","单击");
                    if(player.isPlaying()){
                        player.pause();
                    } else {
                        player.start();
                    }
                    return true;
                }

                @Override
                public boolean onDoubleTap(MotionEvent motionEvent) {
                    Log.d("touch","双击");

                    // 如果已登陆则触发
                    if(IS_LOGIN){
                        toggleVideoFav(videoId);

                        // 过渡动画
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.play(scale(isFav, "scaleX", 1f, 0.5f, 100, 0))   // 缩放动画，X轴1倍至0.5倍
                                .with(scale(isFav, "scaleY", 1f, 0.5f, 100, 0))      // 缩放动画，Y轴1倍至0.5倍
                                .with(alpha(isFav, 1, 0, 100, 0))                                // 渐变透明度动画，透明度从1-0.
                                .with(scale(isFav, "scaleX", 0.5f, 1, 50, 150))     // 缩放动画，X轴0.5倍至1倍
                                .with(scale(isFav, "scaleY", 0.5f, 1, 50, 150))     // 缩放动画，Y轴0.5倍至1倍
                                .with(alpha(isFav, 0, 1, 300, 400));                             // 透明度动画，从0-1
                        animatorSet.start();
                    }
                    // 心跳动画
                    player.heartBeat((int) motionEvent.getX(), (int) motionEvent.getY());

                    return false;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                    return false;
                }
            });
        }

        private void toggleVideoFav(String videoId){

            Map<String,String> map = new HashMap<>(1);
            map.put("videoId", videoId);
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"), new JSONObject(map).toString());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TIKTOK_URI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            apiService.toggleVideoFavorite(X_TIKTOK_TOKEN,requestBody).enqueue(new Callback<ToggleFavDto>() {
                @Override
                public void onResponse(Call<ToggleFavDto> call, Response<ToggleFavDto> response) {
                    ToggleFavDto toggleFavDto = response.body();
                    if(toggleFavDto.errno==0){
                        if(toggleFavDto.isFav){
                            FAV_VIDEO_SET.add(videoId);
                            isFav.setColorFilter(Color.RED);
                        } else {
                            FAV_VIDEO_SET.remove(videoId);
                            isFav.setColorFilter(Color.WHITE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ToggleFavDto> call, Throwable t) {
                    Log.d("retrofit", t.getMessage());
                }
            });
        }

    }



}
