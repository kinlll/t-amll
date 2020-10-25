package com.kinl.tmall.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

 /*   public RedisUtil(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
*/
    //判断key是否存在
    public boolean hasKey(String key){
        try{
            if (key!=null)
                return redisTemplate.hasKey(key);
            else
                throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除缓存  可传一个或多个值
    public void del(String... key){
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            }else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    //普通缓存放入
    public boolean set(String key, Object value){
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
