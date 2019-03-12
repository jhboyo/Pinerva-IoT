package com.iot.socket.server.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.socket.server.domain.Greeting;
import com.iot.socket.server.domain.HelloMessage;


/**
 * @author jookim
 * @Purpose message-handling controller: receive the hello message and send a greeting message.
 */
@Controller
public class GreetingController {

	//ensures that if a message is sent to destination "/hello"
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
        
		Thread.sleep(1000); // simulated delay

		System.out.println(message.getName());

		return new Greeting("Hello, " + message.getName() + " !");
	}
	
	@GetMapping("/sensor/test")
	@SendTo("/topic/greetings")
	public void getSensorData(HelloMessage message, Model model, @RequestParam("value") String value) {
		
		System.out.println(value);
		model.addAttribute("value", value);
		
		
	}
} 
