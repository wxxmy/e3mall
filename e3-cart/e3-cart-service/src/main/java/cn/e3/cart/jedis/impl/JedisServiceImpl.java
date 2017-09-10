package cn.e3.cart.jedis.impl;
/*package cn.e3.content.jedis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.e3.content.jedis.JedisService;
//单机版jedisPoll
@Repository
public class JedisServiceImpl implements JedisService {
    
    @Autowired
    private JedisPool jedisPool;
    
    @Override
    public String set(String key, String value) {
    Jedis jedis = jedisPool.getResource();
    String str = jedis.set(key, value);
    return str;
    }
    @Override
    public String get(String key) {
    Jedis jedis = jedisPool.getResource();
    String str = jedis.get(key);
    return str;
    }
    @Override
    public Long hset(String key, String field, String value) {
    Jedis jedis = jedisPool.getResource();
    Long hset = jedis.hset(key, field, value);
    return hset;
    }
    @Override
    public String hget(String key, String field) {
    Jedis jedis = jedisPool.getResource();
    String string = jedis.hget(key, field);
    return string;
    }
    @Override
    public Long hdel(String key, String fields) {
    Jedis jedis = jedisPool.getResource();
    Long hdel = jedis.hdel(key, fields);
    return hdel;
    }
    @Override
    public Long expire(String key, int seconds) {
    Jedis jedis = jedisPool.getResource();
    Long expire = jedis.expire(key, seconds);
    return expire;
    }
    @Override
    public Long ttl(String key) {
    Jedis jedis = jedisPool.getResource();
    Long ttl = jedis.ttl(key);
    return ttl;
    }

}
*/