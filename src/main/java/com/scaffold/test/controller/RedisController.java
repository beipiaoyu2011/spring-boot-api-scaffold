package com.scaffold.test.controller;

import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.redis.RedisUtils;
import com.scaffold.test.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
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
        redisUtils.set(key, value, 60, TimeUnit.SECONDS);
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


    /**
     * 第一种方法
     * 获取IP并存入redis
     */
    @GetMapping("ip2")
    public Boolean setIp2(@RequestParam String ip) {
        ip = IpUtils.getIpAddress();
        // 判断IP是否在缓存数据中
        Object exist = redisUtils.get(ip);
        if (exist == null) {
            boolean isChinaIp = IpUtils.isChinaIp(ip);
            redisUtils.set(ip, isChinaIp);
            return isChinaIp;
        } else {
            return exist.equals(true);
        }
    }

    /**
     * 第二种方法（耗时）
     * 获取IP并存入redis, 判断是国内外IP
     */
    @GetMapping("ip")
    public Boolean setIp(@RequestParam String ip) {
        Map<String, String> ipData;
        // 从缓存中获取数据
        Object ip_map = redisUtils.get("ip_map");
        if (ip_map != null) {
            ipData = (Map<String, String>) ip_map;
        } else {
            ipData = IpUtils.getIpList();
            redisUtils.set("ip_map", ipData, 2, TimeUnit.HOURS);
        }
        return IpUtils.ipInChina(ipData, ip);
    }


}
