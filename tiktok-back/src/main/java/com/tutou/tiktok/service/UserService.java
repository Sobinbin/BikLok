package com.tutou.tiktok.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tutou.tiktok.entity.User;

/**
 * @author Sobinbin
 * @school BUPT
 * @create 2020-06-05 14:03
 */
public interface UserService extends IService<User> {

    User getByName(String name);
}
