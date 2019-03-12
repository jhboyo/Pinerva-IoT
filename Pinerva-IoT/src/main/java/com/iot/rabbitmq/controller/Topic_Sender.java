package com.iot.rabbitmq.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import lombok.extern.java.Log;

@Log
public class Topic_Sender {


	private static final String EXCHANGE_NAME = "topic_logs";

	private static final String HOST = "localhost";


	public void send(String message) throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);

		//The connection abstracts the socket connection
		//takes care of protocol version negotiation and authentication and so on 
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

		//exchange는 어떻게 queue 랑 연결 되나?
		//messages are routed to the queue with the name specified by routingKey - getSeverity 가 같다.

		String routinKey = "heo.iot.db";
		String routinKey2 = "heo.iot.csv";

		channel.basicPublish(EXCHANGE_NAME, routinKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		channel.basicPublish(EXCHANGE_NAME, routinKey2, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

		System.out.println(" [x] Sent '" + routinKey + " : " + message + "cm from mqtt subscriber' ");

		channel.close();
		connection.close();

	}

	 
}
