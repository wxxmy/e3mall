package cn.e3.redis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class SpringRedis {
    
    //单机版redis
    @Test
    public void singleRedis(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml");
        Jedis jedis = app.getBean(Jedis.class);
        jedis.set("name", "李四");
        String name = jedis.get("name");
        jedis.close();
        System.out.println(name);
    }
    
    //单机版redisPool
    @Test
    public void JedisPool(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml");
        JedisPool jedisPool = app.getBean(redis.clients.jedis.JedisPool.class);
        Jedis jedis = jedisPool.getResource();
        jedis.set("name1", "王五");
        String name = jedis.get("name1");
        jedis.close();
        System.out.println(name);
    }
    
    // 单机版redis集群
    @Test
    public void JedisCluster(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml");
        JedisCluster jedisCluster = app.getBean(redis.clients.jedis.JedisCluster.class);
        jedisCluster.set("sex", "男");
        String sex = jedisCluster.get("sex");
        System.out.println(sex);
    }
}
