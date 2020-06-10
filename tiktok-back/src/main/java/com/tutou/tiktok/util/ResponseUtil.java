package com.tutou.tiktok.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sobinbin
 * @school BUPT
 * @create 2020-05-30 13:33
 */
public class ResponseUtil {

    public static Object ok(){
        Map<String, Object> obj = new HashMap<>();
        obj.put("errno",0);
        obj.put("errmsg","成功");
        return obj;
    }

    public static Object ok(String param, Object data) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("errno", 0);
        obj.put("errmsg", "成功");
        obj.put(param, data);
        return obj;
    }

    public static Object fail() {
        Map<String, Object> obj = new HashMap<>();
        obj.put("errno", -1);
        obj.put("errmsg", "错误");
        return obj;
    }

    public static Object fail(int errno, String errmsg) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("errno", errno);
        obj.put("errmsg", errmsg);
        return obj;
    }
}
