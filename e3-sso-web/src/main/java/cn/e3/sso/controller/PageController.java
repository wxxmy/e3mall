package cn.e3.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    /**
     * 需求:跳转到注册页面
     */
    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }
    /**
     * 需求:跳转到登陆页面
     */
    @RequestMapping("/page/login")
    public String showLogin(){
        return "login";
    }
}
