package com.camel.service.integration.route;

import com.camel.service.CamelMicroserviceApplication;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest(
        classes = CamelMicroserviceApplication.class,
        properties = {"route.camel.seda-handler.enabled=true"}
)
@CamelSpringBootTest
public class ActiveMqSedaRouterTest {

    private final static String SEND_DESTINATION = "my-activemq-queue";

    private final static String CONSUME_DESTINATION = "my-activemq-queue-audit";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    void sendAndReceiveMessage() throws JMSException {
        jmsTemplate.send(SEND_DESTINATION,s ->
                s.createTextMessage(LocalDateTime.now().toString()));

        Message message = jmsTemplate.receive(CONSUME_DESTINATION);
        TextMessage textMessage = (TextMessage) message;
        String text = textMessage.getText();
        assertNotNull("Message should not be null",text);
        assertNotNull("id should be not null",message.getJMSMessageID());
        assertTrue("After Processor should be available",text.contains("After Processor-"));
    }
}
