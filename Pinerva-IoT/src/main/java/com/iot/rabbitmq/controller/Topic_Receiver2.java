package com.iot.rabbitmq.controller;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;



public class Topic_Receiver2 {

	private static final String EXCHANGE_NAME = "topic_logs";
	private static final String HOST = "localhost";

	/**
	 * 
	 */
	public static void main(String[] args) throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

		//At that point queueName contains a random queue name.
		//ex) Queue amq.gen-vWgFLIdqg8mxh6i90ExUbA
		String queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, EXCHANGE_NAME, "heo.*.csv");
		
		System.out.println(" [*] Waiting for messages from Topic_Receiver2. To exit press CTRL+C");

		final Consumer consumer = new DefaultConsumer(channel) {

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) 
					throws IOException {

				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "' from RabbitMQ-Topic_Sender" );
				
				// 메세지 수신 후 작업
				try {
					doWork(message);
				} finally {
					System.out.println(" [x] Done");
				}
			}
		};
		boolean autoAck = true; // acknowledgment is covered below
		channel.basicConsume(queueName, autoAck, consumer);
	}


	// Graph server(was) 로 전송해본다.
	public static String doWork(String message) {
		// do something
		
		return "redirect:/sendvalue.html?message=" + message;
		
	}
	
	 

	 

}
