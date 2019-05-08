package com.activeMq.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
	private static final int SEND_NUMBER = 5;

	public static void main(String[] args) {
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		// Session： 一个发送或接收消息的线程
		Session session;
		// MessageProducer：消息发送者
		MessageProducer messageProducer;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination;

		// TextMessage message;
		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");

		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取操作连接
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			// 获取session注意参数值FirstQueue是一个服务器的queue，须在在ActiveMq的console配置
			destination = session.createQueue("FirstQueue");
			// 得到消息生成者【发送者】
			messageProducer = session.createProducer(destination);
			// 设置不持久化，此处学习，实际根据项目决定
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 构造消息，此处写死，项目就是参数，或者方法获取
			sendMesage(session, messageProducer);

			session.commit();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != connection) {
				try {
					connection.close();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}

	public static void sendMesage(Session session, MessageProducer messageProducer) throws JMSException {

		for (int i = 0; i < SEND_NUMBER; i++) {
			TextMessage message = session.createTextMessage("ActiveMaq发出的消息" + i);
			System.out.println("发出的消息：" + "ActiveMaq发出的消息" + i);
			messageProducer.send(message);
		}
	}
}
