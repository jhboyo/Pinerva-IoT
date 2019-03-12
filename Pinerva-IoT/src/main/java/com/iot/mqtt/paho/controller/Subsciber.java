package com.iot.mqtt.paho.controller;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.iot.rabbitmq.controller.Topic_Sender;
import lombok.extern.java.Log;



@Log
public class Subsciber {

	private final String brokerUrl = "tcp://localhost:1883";
	private final String clientId = "sanbon1";
	
	private MqttClient client;
	
	//private AmqpBasicSender sender;
	
	private Topic_Sender sender; 
	
	
	public static void main(String[] args) {
		new Subsciber().subscribe();
	}
	
	public void subscribe() {
		
		//sender = new AmqpBasicSender();
		
		sender = new Topic_Sender();
		
		try {
			client = new MqttClient(brokerUrl, clientId);
			client.connect();
			client.setCallback(new MqttCallback() {
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					
					log.info("distance value : " + message.toString() + "cm");
					
					//sender.sendToRabbitMQoverAmqp(message.toString());
					sender.send(message.toString());
				}	
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					
				}
				
				@Override
				public void connectionLost(Throwable cause) {
					
				}
			});
			client.subscribe("/heo", 1);
			
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	
}
