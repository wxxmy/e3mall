package cn.e3.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.domain.TbUser;
import cn.e3.sso.utils.CookieUtils;
import cn.e3.user.service.UserService;
import cn.e3.utils.E3mallResult;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Value("${E3_TOKEN}")
    private String E3_TOKEN;
    
    @Value("${E3_TOKEN_EXPIRE_TIME}")
    private Integer E3_TOKEN_EXPIRE_TIME;
    /**
     * 需求:测试
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3mallResult dataCheck(@PathVariable String param,
            @PathVariable Integer type){
        E3mallResult result = userService.dataCheck(param, type);
        return result;
    }
    /**
     * 需求:用户注册
     * 请求:/user/register
     * 参数:TbUser tbUser
     * 返回值:
     * 成功时:
     * {
            status: 200 //200 成功
            msg: "OK" // 返回信息消息
            data: false // 返回数据，true：数据可用，false：数据不可用
            }
          失败时:
            {
            status: 400
            msg: "注册失败. 请校验数据后请再提交数据."
            data: null
            }
            
            doSubmit:function() {
            $.post("/user/register",$("#personRegForm").serialize(), function(data){
                if(data.status == 200){
                    alert('用户注册成功，请登录！');
                    REGISTER.login();
                } else {
                    alert("注册失败！");
                }
            });
        },
        login:function() {
             location.href = "/page/login";
             return false;
        },

     */
    @RequestMapping("user/register")
    @ResponseBody
    public E3mallResult register(TbUser tbUser){
        // 调用远程service服务对象
        E3mallResult register = userService.register(tbUser);
        return register;
    }
    /**
     * 需求:用户登陆
     * 请求:/user/login
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
            业务:
            1.获取服务层返回的token
            2.把token写入到cookie
        */
    @RequestMapping("user/login")
    @ResponseBody
    public E3mallResult login(String username,String password,HttpServletRequest request,HttpServletResponse response){
        //调用远程service服务
        E3mallResult result = userService.login(username, password);
        //获取token
        String token = result.getData().toString();
        //把token放入cookie
        CookieUtils.setCookie(request, response, E3_TOKEN, token, E3_TOKEN_EXPIRE_TIME,true );
        return result;
    }
    /**
     * 需求:根据token查询redis服务器唯一session
     * 请求:http://localhost:8088/user/token/{token}
     */
    @RequestMapping("/user/token/{token}")
    @ResponseBody
    public Object findUserWithToken(@PathVariable String token,String callback){
        E3mallResult result = userService.findUserWithToken(token);
        // 判断ajax请求
        if (StringUtils.isBlank(callback)) {
            // 请求为空直接返回
            return result;
        }
        // 否则就是跨域请求
        // return "callback("++")"
        // Jackson支持跨域设置
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
        // 设置跨域函数
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }
}
