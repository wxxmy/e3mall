package cn.e3.cart.jedis.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.JedisCluster;
import cn.e3.cart.jedis.JedisService;

@Repository
public class JedisClusterServiceImpl implements JedisService {

    // 注入集群对象
    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public String set(String key, String value) {
        String str = jedisCluster.set(key, value);
        return str;
    }

    @Override
    public String get(String key) {
        String str = jedisCluster.get(key);
        return str;
    }

    @Override
    public Long hset(String key, String field, String value) {
        Long hset = jedisCluster.hset(key, field, value);
        return hset;
    }

    @Override
    public String hget(String key, String field) {
        String str = jedisCluster.hget(key, field);
        return str;
    }

    @Override
    public Long hdel(String key, String fields) {
        Long hdel = jedisCluster.hdel(key, fields);
        return hdel;
    }

    @Override
    public Long expire(String key, int seconds) {
        Long expire = jedisCluster.expire(key, seconds);
        return expire;
    }

    @Override
    public Long ttl(String key) {
        Long ttl = jedisCluster.ttl(key);
        return ttl;
    }

    // hash判断,判断key,filed是否存在
    public Boolean hexists(String key, String field) {
        Boolean hexists = jedisCluster.hexists(key, field);
        return hexists;
    }
    
    // sort-set 排序集合,添加排序元素
    public Long zadd(String key,Double score,String member) {
        Long zadd = jedisCluster.zadd(key, score, member);
        return zadd;
    }
    
    // 从高到低获取有序的商品id
    public Set<String> zrevrange(String key,Long start,Long end) {
        Set<String> zrevrange = jedisCluster.zrevrange(key, start, end);
        return zrevrange;
    }
    
    // 删除sorted-set成员数据
    public Long zrem(String key,String member) {
        Long zrem = jedisCluster.zrem(key, member);
        return zrem;
    }
}