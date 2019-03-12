package com.iot.rabbitmq.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Routing_Sender {


	private static final String EXCHANGE_NAME = "direct_logs";

	private static final String HOST = "localhost";


	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		
		for (int i=0; i< 20; i++) {
			String msg = String.valueOf(i);
			new Routing_Sender().send("msg " + msg);
			
			Thread.sleep(1000);
		}
	}

	public void send(String message) throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);

		//The connection abstracts the socket connection
		//takes care of protocol version negotiation and authentication and so on 
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

		//exchange는 어떻게 queue 랑 연결 되나?
		//messages are routed to the queue with the name specified by routingKey - getSeverity 가 같다.

		String[] severities = {"info"};

		channel.basicPublish(EXCHANGE_NAME, severities[0], MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

		System.out.println(" [x] Sent '" + message + " from Routing_Sender' ");

		channel.close();
		connection.close();

	}

	 
}
