package cn.e3.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.e3.pojo.Person;
import freemarker.core.Configurable;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class MyFreeMaker {
    /**
     * 需求:测试FreeMarker,获取对象类型数据
     * @throws Exception 
     */
    @Test
    public void test1() throws Exception{
        Configuration cf = new Configuration(Configuration.getVersion());
        cf.setDirectoryForTemplateLoading(new File("E:\\template"));
        // 设置字符集
        cf.setDefaultEncoding("UTF-8");
        // 获得模版
        Template template = cf.getTemplate("user.ftl");
        // 
        Person p1 = new Person();
        p1.setUsername("王宝强");
        p1.setAge(48);
        p1.setSex("男");
        p1.setAddress("河南人");
        // 
        Map<String, Object> maps = new HashMap<>();
        maps.put("p", p1);
        Writer out = new FileWriter(new File("E:\\template\\out\\user.html"));
        template.process(maps, out);
    }
    /**
     * 需求:测试FreeMarker,获取List对象类型数据
     * @throws Exception 
     */
    @Test
    public void test2() throws Exception{
        Configuration cf = new Configuration(Configuration.getVersion());
        cf.setDirectoryForTemplateLoading(new File("E:\\template"));
        // 设置字符集
        cf.setDefaultEncoding("UTF-8");
        // 获得模版
        Template template = cf.getTemplate("list.ftl");
        // 
        Person p1 = new Person();
        p1.setUsername("王宝强");
        p1.setAge(48);
        p1.setSex("男");
        p1.setAddress("河南人");
        Person p2 = new Person();
        p2.setUsername("马蓉");
        p2.setAge(40);
        p2.setSex("女");
        p2.setAddress("荡妇");
        Person p3 = new Person();
        p3.setUsername("宋謮");
        p3.setAge(38);
        p3.setSex("男");
        p3.setAddress("奸夫");
        // 
        List<Person> list = new ArrayList<Person>();
        list.add(p3);
        list.add(p2);
        list.add(p1);
        Map<String, Object> maps = new HashMap<>();
        maps.put("list", list);
        Writer out = new FileWriter(new File("E:\\template\\out\\list.html"));
        template.process(maps, out);
    }
}
