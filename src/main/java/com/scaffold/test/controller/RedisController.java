package com.scaffold.test.controller;

import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author alex
 */

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取
     *
     * @param key
     * @return
     */
    @GetMapping("get")
    @PassToken
    public Object getRedis(@RequestParam String key) {
        return redisUtils.get(key);
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    @GetMapping("set")
    @PassToken
    public void setRedis(@RequestParam String key, @RequestParam String value) {
        redisUtils.set(key, value, 60 , TimeUnit.SECONDS);
    }

    /**
     * 删除
     *
     * @param key 可以是单个，也可以是多个
     * @return
     */
    @GetMapping("delete")
    @PassToken
    public Object deleteRedis(@RequestParam String key) {
        return redisUtils.delete(key);
    }


}
