package com.iot.rabbitmq.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;



public class Fanout_Receiver2 {

	private static final String EXCHANGE_NAME = "logs";
	private static final String HOST = "localhost";


	/**
	 * exchange type 이 fanout이니 multiple queue에 same message들이 전송 된다!
	 * 즉, work 작업들은 다르게 가져가면 된다.
	 * receiver1은 로그를 기록하고, receiver2는 모니터링 그래프를 그린다.
	 */
	public static void main(String[] args) throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

		//At that point queueName contains a random queue name.
		String queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages from EmitLog_receiver2. To exit press CTRL+C");

		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) 
					throws IOException {
				
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "' from RabbitMQ-receiver2" );

				// 메세지 수신 후 작업
				/*try {
					doWork(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println(" [x] Done");
				}*/
			}
		};
		boolean autoAck = true; // acknowledgment is covered below
		channel.basicConsume(queueName, autoAck, consumer);
	}
	
	public void dowork() {
		// do something
	}
}
