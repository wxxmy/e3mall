package cn.e3.user.service;

import cn.e3.domain.TbUser;
import cn.e3.utils.E3mallResult;

public interface UserService {
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
    public E3mallResult dataCheck(String param,Integer type);
    /**
     * 需求:用户注册
     */
    public E3mallResult register(TbUser tbUser);
    /**
     * 需求:用户登陆
     */
    public E3mallResult login(String username,String password);
    /**
     * 需求:根据token查询redis服务器用户唯一session
     * 参数:String token
     * 返回值:E3mallResult.ok(user)
     */
    public E3mallResult findUserWithToken(String token);
}
