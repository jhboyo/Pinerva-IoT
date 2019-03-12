package com.iot.rabbitmq.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;



public class Fanout_Sender {

	private static final String EXCHANGE_NAME = "logs";
	
	private static final String HOST = "localhost";

	
	public static void main(String[] args) throws IOException, TimeoutException {
		//new EmitLog_Producer().send(message);
	}
	
	public void send(String message) throws IOException, TimeoutException {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);

		//The connection abstracts the socket connection
		//takes care of protocol version negotiation and authentication and so on 
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
		
		//exchange는 어떻게 queue 랑 연결 되나?
		//messages are routed to the queue with the name specified by routingKey
		//fanout은 동일한 message를  multiple queue로 전달하기 때문에 queue를 구분하기 위한 routingKey를 따로 지정하지 않는 것 같다.
		channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		
		System.out.println(" [x] Sent '" + message + " from EmitLog_Producer' ");
		
		channel.close();
		connection.close();

	}
}
