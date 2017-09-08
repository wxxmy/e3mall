package cn.e3.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3.pojo.Person;
import freemarker.template.Configuration;
import freemarker.template.Template;

@RestController//相当于集合了ResponseBody跟Controller
public class FmSpringController {

    // 注入freemarker
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    /**
     * 需求:测试Spring整合freemarker
     * @throws Exception 
     */
    
    @RequestMapping("gen")
    public String freemarkerTest() throws Exception{
        // 1.获得配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        // 2.获得模版
        Template template = configuration.getTemplate("user.ftl");
        // 3.
        Person p = new Person();
        p.setAddress("北京");
        p.setAge(18);
        p.setSex("男");
        p.setUsername("陈冠希");
        Map<String, Object> maps = new HashMap<>();
        maps.put("p", p);
        Writer out = new FileWriter(new File("E:\\template\\out\\u.html"));
        template.process(maps, out);
        return "success";
    }
}
