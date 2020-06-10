package com.tutou.tiktok.controller;

import com.tutou.tiktok.auth.LoginUser;
import com.tutou.tiktok.entity.FavoriteVideo;
import com.tutou.tiktok.service.FavoriteVideoService;
import com.tutou.tiktok.util.JacksonUtil;
import com.tutou.tiktok.util.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sobinbin
 * @school BUPT
 * @create 2020-06-05 17:22
 */
@RestController
@RequestMapping("tiktok/fav")
public class FavController {

    @Resource
    private FavoriteVideoService favoriteVideoService;

    @PostMapping("video/toggle")
    public Object toggleVideoFavorite(@LoginUser Integer userId, @RequestBody String body){
        if(userId==null)    return ResponseUtil.fail(301,"未登录");
        if(body==null)  return ResponseUtil.fail(101,"参数不对");

        String videoId = JacksonUtil.parseString(body,"videoId");

        if(videoId==null || userId==null || videoId.isEmpty())  return ResponseUtil.fail(101,"参数不对");

        boolean isFav;
        FavoriteVideo fv = new FavoriteVideo();
        fv.setUserId(userId);
        fv.setVideoId(videoId);


        if(favoriteVideoService.hasExist(fv)){
            favoriteVideoService.remove(fv);
            isFav = false;
        } else {
            favoriteVideoService.save(fv);
            isFav = true;
        }

        return ResponseUtil.ok("isFav", isFav);
    }

    @GetMapping("video/mine")
    public Object myFavVideo(@LoginUser Integer userId){
        if(userId==null)    return ResponseUtil.fail(301,"未登录");
        return ResponseUtil.ok("videoIdList",favoriteVideoService.getFavByUserId(userId));
    }

}
