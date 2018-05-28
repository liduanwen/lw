package com.bee.lw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * ********************************************************.<br>
 *
 * @author ldw <br>
 * @classname RedisController <br>
 * @description 集成redis <br>
 * @created 2018/5/28 15:11 <br>
 * ********************************************************.<br>
 */
@RestController
public class RedisController extends BaseController{
    @Autowired
    private StringRedisTemplate template;

    @RequestMapping(value ="/save")
    public String test(HttpServletRequest request) throws Exception {
        redisDao.setKey("name","forezp");
        redisDao.setKey("age","11");
        logger.info(redisDao.getValue("name"));
        logger.info(redisDao.getValue("age"));
        return "1";
    }
}
