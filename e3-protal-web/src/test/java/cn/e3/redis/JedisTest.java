package cn.e3.redis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisTest {

    @Test
    public void SingleRedis(){
        Jedis jedis = new Jedis("192.168.32.129", 6379);
        jedis.set("user", "马永贞");
        String string = jedis.get("user");
        System.out.println(string);
    }
    
    @Test
    public void jedisPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(2000);
        jedisPoolConfig.setMaxIdle(20);
        
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.32.129", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("age", "18");
        String ageString = jedis.get("age");
        System.out.println(ageString);
    }
    
    @Test
    public void redisCluster(){
        //创建封装主机ip和端口nodes的set集合
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.32.129", 7001));
        nodes.add(new HostAndPort("192.168.32.129", 7002));
        nodes.add(new HostAndPort("192.168.32.129", 7003));
        nodes.add(new HostAndPort("192.168.32.129", 7004));
        nodes.add(new HostAndPort("192.168.32.129", 7005));
        nodes.add(new HostAndPort("192.168.32.129", 7006));
        nodes.add(new HostAndPort("192.168.32.129", 7007));
        nodes.add(new HostAndPort("192.168.32.129", 7008));
        // 创建jedis集群对象
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("address", "北京昌平新区");
        String address = jedisCluster.get("address");
        jedisCluster.close();
        System.out.println(address);
    }
}
