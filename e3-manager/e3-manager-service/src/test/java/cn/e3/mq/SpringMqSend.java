package cn.e3.mq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class SpringMqSend {
    /**
     * 需求:点对点方式发送消息
     */
    @Test
    public void springQueue(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq-send.xml");
        JmsTemplate template = app.getBean(JmsTemplate.class);
        Destination destination = app.getBean(ActiveMQQueue.class);
        template.send(destination, new MessageCreator() {
            
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("北京昌平修正大厦");
                
                return textMessage;
            }
        });
    }
    /**
     * 需求:发布订阅方式发送消息
     */
    @Test
    public void springTopic(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq-send.xml");
        JmsTemplate template = app.getBean(JmsTemplate.class);
        Destination destination = app.getBean(ActiveMQTopic.class);
        template.send(destination, new MessageCreator() {
            
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("少林武当过");
                
                return textMessage;
            }
        });
    }
}
