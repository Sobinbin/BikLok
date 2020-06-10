package com.tutou.tiktok.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tutou.tiktok.dao.UserDao;
import com.tutou.tiktok.entity.User;
import com.tutou.tiktok.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Sobinbin
 * @school BUPT
 * @create 2020-06-05 14:04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Override
    public User getByName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",name);
        return baseMapper.selectOne(queryWrapper);
    }

}
