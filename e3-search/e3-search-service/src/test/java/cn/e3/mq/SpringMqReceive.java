package cn.e3.mq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMqReceive {
    
    /**
     * 需求:spring整合activeMQ接收点对点消息
     * @throws Exception 
     */
    @Test
    public void springmqP2P() throws Exception{
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq-receive.xml");
        System.in.read();
    }
    
    /**
     * 需求:spring整合activeMQ接收发布订阅消息
     * @throws Exception 
     */
    @Test
    public void springmqPS() throws Exception{
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq-receive.xml");
        System.in.read();
    }
}
