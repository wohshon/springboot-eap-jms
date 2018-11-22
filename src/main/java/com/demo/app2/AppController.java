package com.demo.app2;

import javax.jms.ConnectionFactory;
import javax.servlet.MultipartConfigElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.app2.model.Payload;

@EnableJms
@RestController

public class AppController {

	//private ConnectionFactory cf=new JmsConnectionFactory("admin","admin","amqp://broker-amq-amqp.amq.svc.cluster.local:5672");
	private ConnectionFactory cf=new JmsConnectionFactory("admin","admin");
//    @Autowired
    private JmsTemplate jmsTemplate=new JmsTemplate(cf);
    
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory (ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        //configurer.configure(factory, connectionFactory);
        configurer.configure(factory, cf);
        return factory;
    }    
	//Logger logger=Logger.getLogger(this.getClass());
    Logger logger=LogManager.getLogger(getClass());

	@PostMapping("/test")
	void test(@RequestBody Payload payload) {
		logger.info("Payload: "+payload.getId());
		logger.info("Sending: "+payload.toString());
		System.out.println(this.jmsTemplate.getConnectionFactory());
		this.jmsTemplate.convertAndSend("queue1",payload.toString());
		logger.info("SENT");

	}
	
    //@JmsListener(destination = "queue1", containerFactory="jmsListenerContainerFactory")
    public void receiveMessage(Message<String> text) {
        logger.info(String.format("Received '%s'", text.getPayload()));
    }	
	
    
    @Bean
    public MultipartConfigElement multipartConfigElement() {
         MultipartConfigFactory factory = new MultipartConfigFactory();
         factory.setMaxFileSize(DataSize.ofMegabytes(25));
         factory.setMaxRequestSize(DataSize.ofMegabytes(25));
         return factory.createMultipartConfig();
    }    
}
