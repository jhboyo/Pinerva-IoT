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



public class Routing_Receiver {

	private static final String EXCHANGE_NAME = "direct_logs";
	private static final String HOST = "localhost";

	/**
	 * severity "error"만 수신한다.
	 */
	public static void main(String[] args) throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

		//At that point queueName contains a random queue name.
		//ex) Queue amq.gen-vWgFLIdqg8mxh6i90ExUbA
		String queueName = channel.queueDeclare().getQueue();

		//binding name is severity
		//String[] severities = {"error"};
		
		//for(String severity : severities){
			channel.queueBind(queueName, EXCHANGE_NAME, "error");
		//}

		System.out.println(" [*] Waiting for messages from Routing_Receiver. To exit press CTRL+C");


		final Consumer consumer = new DefaultConsumer(channel) {

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) 
					throws IOException {

				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "' from RabbitMQ-Routing_Receiver" );
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
