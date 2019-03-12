package com.iot.rabbitmq.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;



/**
 * @author jookim
 * The publisher will connect to RabbitMQ, send a single message, then exit.
 */
public class AmqpBasicSender {

	private final static String QUEUE_NAME = "heoQueue";

	public static void main(String[] argv) throws Exception {

		//new Sender().sendToRabbitMQoverAmqp();
	}

	public void sendToRabbitMQoverAmqp(String message) throws IOException, TimeoutException {
		//create a connection to the server:
		ConnectionFactory factory = new ConnectionFactory();
		//what is the protocol? amqp?
		factory.setHost("localhost");

		//The connection abstracts the socket connection
		//takes care of protocol version negotiation and authentication and so on 
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		//String message = "Hello HeoAcademy msg!";
		//fisrt parameter is for the name of exchange. nameless exchange or default exchange
		channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}
}
