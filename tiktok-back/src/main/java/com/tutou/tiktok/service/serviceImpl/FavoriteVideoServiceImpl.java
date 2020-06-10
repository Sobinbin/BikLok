package com.tutou.tiktok.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tutou.tiktok.dao.FavoriteVideoDao;
import com.tutou.tiktok.entity.FavoriteVideo;
import com.tutou.tiktok.service.FavoriteVideoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sobinbin
 * @school BUPT
 * @create 2020-06-05 17:16
 */
@Service
public class FavoriteVideoServiceImpl extends ServiceImpl<FavoriteVideoDao, FavoriteVideo> implements FavoriteVideoService {

    @Override
    public boolean hasExist(FavoriteVideo fv) {
        QueryWrapper<FavoriteVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",fv.getUserId()).eq("video_id",fv.getVideoId());
        return baseMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public boolean remove(FavoriteVideo fv){
        QueryWrapper<FavoriteVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",fv.getUserId()).eq("video_id",fv.getVideoId());
        return baseMapper.delete(queryWrapper) > 0;
    }

    @Override
    public List<String> getFavByUserId(int userId) {
        QueryWrapper<FavoriteVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_id").eq("user_id",userId);
        List<String> videoIdList = new ArrayList<>();
        for(Object videoId : baseMapper.selectObjs(queryWrapper)){
            videoIdList.add((String)videoId);
        }
        return videoIdList;
    }
}
