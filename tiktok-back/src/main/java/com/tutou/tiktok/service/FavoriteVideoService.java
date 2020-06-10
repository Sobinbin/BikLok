package com.tutou.tiktok.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tutou.tiktok.entity.FavoriteVideo;

import java.util.List;

/**
 * @author Sobinbin
 * @school BUPT
 * @create 2020-06-05 17:16
 */
public interface FavoriteVideoService extends IService<FavoriteVideo> {

    boolean hasExist(FavoriteVideo fv);

    boolean remove(FavoriteVideo fv);

    List<String> getFavByUserId(int userId);
}
