package com.tutou.tiktok.controller;

import com.tutou.tiktok.auth.UserTokenManager;
import com.tutou.tiktok.entity.User;
import com.tutou.tiktok.service.UserService;
import com.tutou.tiktok.util.JacksonUtil;
import com.tutou.tiktok.util.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sobinbin
 * @school BUPT
 * @create 2020-06-05 14:07
 */
@RestController
@RequestMapping("tiktok/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("register")
    public Object register(@RequestBody String body) throws NoSuchAlgorithmException {

        if(body==null)  return ResponseUtil.fail(101,"参数不对");

        String username = JacksonUtil.parseString(body,"username");
        String password = JacksonUtil.parseString(body,"password");

        if(username==null || password==null || username.isEmpty() || password.isEmpty())
            return ResponseUtil.fail(101,"参数不对");

        if(userService.getByName(username)!=null)
            return ResponseUtil.fail(102,"用户已存在");

        User user = new User();
        user.setUsername(username);
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(password.getBytes());
        user.setPassword(new BigInteger(md.digest()).toString(32));

        return userService.save(user)? ResponseUtil.ok() : ResponseUtil.fail();
    }

    @PostMapping("login")
    public Object login(@RequestBody String body) throws NoSuchAlgorithmException {

        if(body==null)  return ResponseUtil.fail(101,"参数不对");

        String username = JacksonUtil.parseString(body,"username");
        String password = JacksonUtil.parseString(body,"password");

        if(username==null || password==null || username.isEmpty() || password.isEmpty())
            return ResponseUtil.fail(101,"参数不对");

        User user = userService.getByName(username);

        if(user==null)  return ResponseUtil.fail(103,"用户不存在");

        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(password.getBytes());
        password = new BigInteger(md.digest()).toString(32);

        if(!user.getPassword().equals(password))    return ResponseUtil.fail(104,"密码不对");

        String token = UserTokenManager.generateToken(user.getUserId());

        return ResponseUtil.ok("token",token);
    }



}
