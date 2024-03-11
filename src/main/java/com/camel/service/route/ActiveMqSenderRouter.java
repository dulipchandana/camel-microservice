package com.camel.service.route;

import com.camel.service.process.SimpleLoggingProcessor;
import com.camel.service.service.CurrentTimeService;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import static org.apache.camel.component.bean.BeanConstants.BEAN_METHOD_NAME;

@AllArgsConstructor
@Service
@ConditionalOnProperty(name="route.camel.sender-handler.enabled", havingValue = "true")
public class ActiveMqSenderRouter extends RouteBuilder {

    private final CurrentTimeService currentTimeService;

    private final SimpleLoggingProcessor simpleLoggingProcessor;
    @Override
    public void configure() throws Exception {
        from("timer:active-mq-timer?period=2000")
                .setHeader(BEAN_METHOD_NAME,()->"getCurrentTime")
                .bean(currentTimeService)
                .process(simpleLoggingProcessor)
                .log("id - ${id}")
                .to("activemq:my-activemq-queue");
    }
}
