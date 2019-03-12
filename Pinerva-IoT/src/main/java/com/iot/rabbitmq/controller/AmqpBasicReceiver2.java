package com.iot.rabbitmq.controller;

import java.io.IOException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;



public class AmqpBasicReceiver2 {

	private final static String QUEUE_NAME = "heoQueue";

	public static void main(String[] args) throws Exception {

		//create a connection to the server:
		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost("localhost");
		//factory.setHost("tcp://localhost:1883");

		//The connection abstracts the socket connection
		//takes care of protocol version negotiation and authentication and so on 
		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		/*  
		 *We're about to tell the server to deliver us the messages from the queue. Since it will push us messages asynchronously, 
		 *we provide a callback in the form of an object that will buffer the messages until we're ready to use them. 
		 *That is what a DefaultConsumer subclass does.
		 */
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
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	}
}
