package com.demo.app2;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.app2.model.Payload;

@EnableJms
@RestController
public class AppController {

    @Autowired
    private JmsTemplate jmsTemplate;
    
	Logger logger=Logger.getLogger(this.getClass());

	@PostMapping("/test")
	void test(@RequestBody Payload payload) {
		logger.info("====================>"+payload.getId());
		logger.info("====================>Sending "+payload.toString());
		//this.jmsTemplate.convertAndSend("queue1",payload.toString());
		this.jmsTemplate.send("queue1", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(payload.toString());
				
			}
		});
	}
	
    @JmsListener(destination = "queue1")
    public void receiveMessage(String text) {
        System.out.println(String.format("Received '%s'", text));
    }	
	
}
