package com.tutou.bigwork.player;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tutou.bigwork.R;

import java.io.IOException;
import java.util.Random;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.tutou.bigwork.util.AnimatorUtil.*;

public class VideoPlayerIJK extends FrameLayout {
    /**
     * 由ijkplayer提供，用于播放视频，需要给他传入一个surfaceView
     */
    private IMediaPlayer mMediaPlayer = null;

    private boolean hasCreateSurfaceView = false;
    /**
     * 视频文件地址
     */
    private String mPath = "";
    private int resId = 0;

    private SurfaceView surfaceView;

    private VideoPlayerListener listener;
    private Context mContext;

    final float[] num = {-30, -20, 0, 20, 30};

    public void heartBeat(int x, int y){
        // 创建一个展示心形图片的 ImageView
        final ImageView imageView = new ImageView(mContext);

        // 设置图片展示的位置
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 300);
        params.leftMargin = x - 150;
        params.topMargin = y - 300;

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.details_icon_like_pressed,null));
        imageView.setLayoutParams(params);
        addView(imageView);

        // 设置 imageView 动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scale(imageView, "scaleX", 2f, 0.9f, 100, 0))   // 缩放动画，X轴2倍缩小至0.9倍
                .with(scale(imageView, "scaleY", 2f, 0.9f, 100, 0))      // 缩放动画，Y轴2倍缩小至0.9倍
                .with(rotation(imageView, 0, 0, num[new Random().nextInt(4)]))         // 旋转动画，随机旋转角度num={-30.-20，0，20，30}
                .with(alpha(imageView, 0, 1, 100, 0))                                // 渐变透明度动画，透明度从0-1.
                .with(scale(imageView, "scaleX", 0.9f, 1, 50, 150))     // 缩放动画，X轴0.9倍缩小至1倍
                .with(scale(imageView, "scaleY", 0.9f, 1, 50, 150))     // 缩放动画，Y轴0.9倍缩小至1倍
                .with(translationY(imageView, 0, -600, 800, 400))                    // 平移动画，Y轴从0向上移动600单位
                .with(alpha(imageView, 1, 0, 300, 400))                              // 透明度动画，从1-0
                .with(scale(imageView, "scaleX", 1, 3f, 700, 400))      // 缩放动画，X轴1倍放大至3倍
                .with(scale(imageView, "scaleY", 1, 3f, 700, 400));     // 缩放动画，Y轴1倍放大至3倍
        animatorSet.start();

        // 我们不可能无限制的增加 view，在 view 消失之后，需要手动的移除改 imageView
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeViewInLayout(imageView);
            }
        });
    }

    public VideoPlayerIJK(@NonNull Context context) {
        super(context);
        initVideoView(context);
    }

    public VideoPlayerIJK(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public VideoPlayerIJK(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initVideoView(Context context) {
        mContext = context;
        setFocusable(true);
    }

    /**
     * 设置视频地址。
     * 根据是否第一次播放视频，做不同的操作。
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
        mPath = path;
        createSurfaceView();
        load();
    }

    public void setVideoResource(int resourceId) {
        resId = resourceId;
        createSurfaceView();
        load(resId);
    }

    /**
     * 新建一个surfaceview
     */
    private void createSurfaceView() {
        if (hasCreateSurfaceView) {
            return;
        }
        //生成一个新的surface view
        surfaceView = new SurfaceView(mContext);
        surfaceView.getHolder().addCallback(new PlayerSurfaceCallback());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        surfaceView.setLayoutParams(layoutParams);
        this.addView(surfaceView);

        hasCreateSurfaceView = true;
    }

    /**
     * surfaceView的监听器
     */
    private class PlayerSurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //surfaceview创建成功后，加载视频
            //给mediaPlayer设置视图
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    /**
     * 加载视频
     */
    private void load() {
        //每次都要重新创建IMediaPlayer
        createPlayer();
        try {
            mMediaPlayer.setDataSource(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载视频
     */
    private void load(int resourceId) {
        //每次都要重新创建IMediaPlayer
        createPlayer();
        AssetFileDescriptor fileDescriptor = mContext.getResources().openRawResourceFd(resourceId);
        RawDataSourceProvider provider = new RawDataSourceProvider(fileDescriptor);
        mMediaPlayer.setDataSource(provider);
    }

    /**
     * 创建一个新的player
     */
    private void createPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
//        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        //开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);

        mMediaPlayer = ijkMediaPlayer;
        ((IjkMediaPlayer) mMediaPlayer).setSpeed(3f);
        if (listener != null) {
            mMediaPlayer.setOnPreparedListener(listener);
            mMediaPlayer.setOnInfoListener(listener);
            mMediaPlayer.setOnSeekCompleteListener(listener);
            mMediaPlayer.setOnBufferingUpdateListener(listener);
            mMediaPlayer.setOnErrorListener(listener);
        }
    }

    public void setListener(VideoPlayerListener listener) {
        this.listener = listener;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(listener);
        }
    }

    /**
     * -------======--------- 下面封装了一下控制视频的方法
     */

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
        }
    }
}
