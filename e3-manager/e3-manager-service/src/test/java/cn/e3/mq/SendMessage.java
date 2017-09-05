package cn.e3.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class SendMessage {
    
    /**
     * 需求:测试activeMQ点对点发送消息
     * @throws Exception 
     */
    @Test
    public void sendMessageP2P() throws Exception{
        // 创建工厂对象
        ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://192.168.32.129:61616");
        // 创建连接
        Connection connection = cf.createConnection();
        // 连接启动
        connection.start();
        // 获得session对象
        // 1.参数一:用户自定义事务,如果指定了自定义事务,那么activeMQ事务会被忽略
        // 2.参数二:activeMQ事务,现在是自动应答模式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建消息空间并且命名
        Queue queue = session.createQueue("myqueue");
        // 指定发送者,以及要发送的消息
        MessageProducer producer = session.createProducer(queue);
        // 创建消息
        TextMessage message = new ActiveMQTextMessage();
        message.setText("你好,activeMQ点对点模式Async");
        // 发送消息
        producer.send(message);
        // 释放资源
        producer.close();
        session.close();
        connection.close();
    }
    /**
     * 需求:测试activeMQ发布订阅方式发送消息
     * @throws Exception 
     */
    @Test
    public void sendMessagePS() throws Exception{
        // 创建工厂对象
        ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://192.168.32.129:61616");
        // 创建连接
        Connection connection = cf.createConnection();
        // 连接启动
        connection.start();
        // 获得session对象
        // 1.参数一:用户自定义事务,如果指定了自定义事务,那么activeMQ事务会被忽略
        // 2.参数二:activeMQ事务,现在是自动应答模式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建消息空间并且命名
        Topic topic = session.createTopic("mytopic");
        // 指定发送者,以及要发送的消息
        MessageProducer producer = session.createProducer(topic);
        // 创建消息
        TextMessage message = new ActiveMQTextMessage();
        message.setText("你好,activeMQ发布订阅模式");
        // 发送消息
        producer.send(message);
        // 释放资源
        producer.close();
        session.close();
        connection.close();
    }
}
