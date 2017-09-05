package cn.e3.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ReceiveMessage {
    /**
     * 需求:同步模式接收activeMQ消息
     * 
     * @throws Exception
     */
    @Test
    public void receiveMessageSync() throws Exception {
        // 创建工厂对象
        ConnectionFactory cf = new ActiveMQConnectionFactory(
                "tcp://192.168.32.129:61616");
        // 创建连接
        Connection connection = cf.createConnection();
        // 连接启动
        connection.start();
        // 获得session对象
        // 1.参数一:用户自定义事务,如果指定了自定义事务,那么activeMQ事务会被忽略
        // 2.参数二:activeMQ事务,现在是自动应答模式
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        // 创建消息空间并且命名
        Queue queue = session.createQueue("myqueue");
        // 指定接收者
        MessageConsumer consumer = session.createConsumer(queue);
        // 设置消息延时
        Message message = consumer.receive(1000);
        // 强转message
        if (message instanceof TextMessage) {
            TextMessage m = (TextMessage) message;
            System.out.println("接收到的消息是:" + m.getText());
        }
        // 释放资源
        consumer.close();
        session.close();
        connection.close();
    }

    /**
     * 需求:异步模式接收activeMQ消息,即监听器模式
     * 
     * @throws Exception
     */
    @Test
    public void receiveMessageAsync() throws Exception {
        // 创建工厂对象
        ConnectionFactory cf = new ActiveMQConnectionFactory(
                "tcp://192.168.32.129:61616");
        // 创建连接
        Connection connection = cf.createConnection();
        // 连接启动
        connection.start();
        // 获得session对象
        // 1.参数一:用户自定义事务,如果指定了自定义事务,那么activeMQ事务会被忽略
        // 2.参数二:activeMQ事务,现在是自动应答模式
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        // 创建消息空间并且命名
        Queue queue = session.createQueue("myqueue");
        // 指定接收者
        MessageConsumer consumer = session.createConsumer(queue);
        // 使用监听器
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                // 强转message
                if (message instanceof TextMessage) {
                    TextMessage m = (TextMessage) message;
                    try {
                        System.out.println("接收到的消息是:" + m.getText());
                    } catch (JMSException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        // 模拟服务器监听状态
        System.in.read();
        // 释放资源
        consumer.close();
        session.close();
        connection.close();
    }
    
    /**
     * 需求:异步模式接收activeMQ消息,即监听器模式
     * 
     * @throws Exception
     */
    @Test
    public void receiveMessagePS() throws Exception {
        // 创建工厂对象
        ConnectionFactory cf = new ActiveMQConnectionFactory(
                "tcp://192.168.32.129:61616");
        // 创建连接
        Connection connection = cf.createConnection();
        // 连接启动
        connection.start();
        // 获得session对象
        // 1.参数一:用户自定义事务,如果指定了自定义事务,那么activeMQ事务会被忽略
        // 2.参数二:activeMQ事务,现在是自动应答模式
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        // 创建消息空间并且命名
        Topic topic = session.createTopic("mytopic");
        // 指定接收者
        MessageConsumer consumer = session.createConsumer(topic);
        // 使用监听器
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                // 强转message
                if (message instanceof TextMessage) {
                    TextMessage m = (TextMessage) message;
                    try {
                        System.out.println("接收到的消息是:" + m.getText());
                    } catch (JMSException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        // 模拟服务器监听状态
        System.in.read();
        // 释放资源
        consumer.close();
        session.close();
        connection.close();
    }
}
