package cn.e3.cart.jedis;

import java.util.Set;

public interface JedisService {
    
    //String类型
    public String set(String key,String value);
    //String get
    public String get(String key);
    //hash类型
    public Long hset(String key, String field, String value);
    //hash get
    public String hget(String key, String field);
    //hash delete
    public Long hdel(String key,String fields);
    //设置数据过期
    public Long expire(String key,int seconds);
    //测试过期时间过程
    public Long ttl(String key);
    //hash判断,判断key,filed是否存在
    public Boolean hexists(String key,String field);
    // sort-set 排序集合,添加排序元素
    public Long zadd(String key,Double score,String member);
    // 从搞到第获取有序的商品id
    public Set<String> zrevrange(String key,Long start,Long end) ;
    // 上传sorted-set成员数据
    public Long zrem(String key,String member);
}
