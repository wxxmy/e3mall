package cn.e3.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Service;

import cn.e3.domain.TbUser;
import cn.e3.domain.TbUserExample;
import cn.e3.domain.TbUserExample.Criteria;
import cn.e3.item.jedis.JedisService;
import cn.e3.mapper.TbUserMapper;
import cn.e3.user.service.UserService;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.JsonUtils;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;
    
    // 注入jedisDao服务
    @Autowired
    private JedisService jedisService;
    
    @Value("${SESSION_KEY}")
    private String SESSION_KEY;
    
    @Value("${SESSION_KEY_EXPIRE_TIME}")
    private Integer SESSION_KEY_EXPIRE_TIME;
    /**
     * 需求:校验数据是否可用.(校验用户名,手机号,邮箱是否被占有)
     * 参数:String param,Integer type
     * 返回值:
     * {
        status: 200 //200 成功
        msg: "OK" // 返回信息消息
        data: false // 返回数据，true：数据可用，false：数据不可用
        }
        业务:
        type=1:parem=username
        type=2:parem=phone
        type=3:parem=email
     */
    public E3mallResult dataCheck(String param, Integer type) {
        // 创建tbuser example对象
        TbUserExample example = new TbUserExample();
        // 
        Criteria createCriteria = example.createCriteria();
        //
        if (type==1) {
            //
            createCriteria.andUsernameEqualTo(param);
        }else if (type==2) {
            //
            createCriteria.andPhoneEqualTo(param);
        }else if (type==3) {
            createCriteria.andEmailEqualTo(param);
        }
        // 
        List<TbUser> list = tbUserMapper.selectByExample(example);
        //
        if (list!=null&&list.size()>0) {
            // 有值,说明此用户名,电话,邮箱已经被占有
            return E3mallResult.ok(false);
        }
        return E3mallResult.ok(true);
    }
    /**
     * 需求:用户注册
     */
    public E3mallResult register(TbUser tbUser) {
        try {
            // 用户数据密码需要被加密
            String newPwd = tbUser.getPassword();
            // 加密密码
            newPwd = DigestUtils.md5DigestAsHex(newPwd.getBytes());
            // 加密后设置到用户对象中
            tbUser.setPassword(newPwd);
            // 设置用户数据更新时间
            Date date = new Date();
            tbUser.setCreated(date);
            tbUser.setUpdated(date);
            
            
            // 插入数据库
            tbUserMapper.insert(tbUser);
            
            // 成功
            return E3mallResult.build(200, "注册成功!");
        } catch (Exception e) {
            // 失败
            e.printStackTrace();
            return E3mallResult.build(400, "注册失败. 请校验数据后请再提交数据");
        }
    }
    /**
     * 需求:用户登陆
     * 请求:/user/register
     * 参数:sername //用户名 ;password //密码
     * 返回值:
     * 成功时:
     * {
           status: 200
            msg: "OK"
            data: "fe5cb546aeb3ce1bf37abcb08a40493e" //登录成功，返回token

            }
          失败时:
            {
            status: 201
            msg: "登陆失败"
            data: null
            }
        业务流程:
        1.接受登陆用户名和密码
        2.根据用户查询数据库,判断此用户是否存在
        3.如果不存在,返回用户名或密码错误
        4.如果存在,获取用户数据,判断用户密码
        5.加密密码,同数据库密码匹配
        6.如果匹配不成功,返回用户名或密码错误
        7.否则登陆成功
        8.生成token
        9.把用户身份信息写入redis服务器
        10.给用户身份信息设置过期时间
        11.返回token
        把用户身份信息写入redis服务器:
        
     */
    @RequestMapping("/user/login")
    @ResponseBody
    public E3mallResult login(String username,String password){
        // 1.
        TbUserExample example = new TbUserExample();
        //
        Criteria createCriteria = example.createCriteria();
        //
        createCriteria.andUsernameEqualTo(username);
        //
        List<TbUser> list = tbUserMapper.selectByExample(example);
        // 判断用户名是否存在
        if (list.isEmpty()) {
            //
            return E3mallResult.build(201, "用户名或密码错误");
        }
        // 如果存在
        TbUser tbUser = list.get(0);
        //
        String oldPwd = tbUser.getPassword();
        //
        String newPwd = DigestUtils.md5DigestAsHex(password.getBytes());
        //
        if (!newPwd.equals(oldPwd)) {
            return E3mallResult.build(201, "用户名或密码错误");
        }
        //用户登陆成功
        // 生成UUID的token
        String token = UUID.randomUUID().toString();
        // 把用户身份信息写入redis服务器
        jedisService.set(SESSION_KEY+":"+token, JsonUtils.objectToJson(tbUser));
        // 设置用户身份信息过期时间
        jedisService.expire(SESSION_KEY+":"+token, SESSION_KEY_EXPIRE_TIME);
        //返回token
        return E3mallResult.ok(token);
    }
    /**
     * 需求:根据token查询redis服务器用户唯一session
     * 参数:String token
     * 返回值:E3mallResult.ok(user)
     */
    public E3mallResult findUserWithToken(String token) {
        // 根据token查询redis服务器
        String userJson = jedisService.get(SESSION_KEY+":"+token);
        // 判断用户身份信息在redis服务器中是否存在
        if (StringUtils.isBlank(userJson)) {
            return E3mallResult.build(401, "用户身份信息已经过期");
        }
        // 如果用户身份信息不为空
        // 把json格式数据转换成user对象
        TbUser tbUser = JsonUtils.jsonToPojo(userJson, TbUser.class);
        // 重置Redis中token过期时间
        jedisService.expire(SESSION_KEY+":"+token,SESSION_KEY_EXPIRE_TIME);
        // 返回
        return E3mallResult.ok(tbUser);
    }

}
