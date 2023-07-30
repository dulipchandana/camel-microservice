package com.camel.service.integration.route;

import com.camel.service.CamelMicroserviceApplication;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;
import java.util.function.Function;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@Testcontainers
@SpringBootTest(
        classes = CamelMicroserviceApplication.class,
        properties = {"route.camel.seda-handler.enabled=true"}
)
@CamelSpringBootTest
public class ActiveMqSedaRouterTest {

    private final static String SEND_DESTINATION = "my-activemq-queue";

    private final static String CONSUME_DESTINATION = "my-activemq-queue-audit";

    private final static String SERVICE_NAME_ACTIVEMQ = "activemq";
    private final static int ACTIVE_MQ_PORT = 61616;
    private final static String ACTIVEMQ_UP_LOG_MESSAGE = "Connector ws started";



    @Autowired
    private JmsTemplate jmsTemplate;

    //test is working without this section
    /*static Function<String, WaitStrategy> waitForLogMessageFunction = (String messageInLog) ->
            Wait.forLogMessage(messageInLog,1)
                    .withStartupTimeout(Duration.ofSeconds(30));*/

    @Container
    public static DockerComposeContainer DOCKER_ACTIVEMQ = new DockerComposeContainer(
            new File("src/main/resources/docker/docker-compose.yml"))
            .withExposedService(SERVICE_NAME_ACTIVEMQ,ACTIVE_MQ_PORT);
            //.waitingFor(SERVICE_NAME_ACTIVEMQ,waitForLogMessageFunction.apply(ACTIVEMQ_UP_LOG_MESSAGE));

    @BeforeAll
    public static void constructed() {
        DOCKER_ACTIVEMQ.start();
    }

    @AfterAll
    public static void afterAll() {
        DOCKER_ACTIVEMQ.stop();
    }

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
