package com.camel.service.route;

import com.camel.service.process.SimpleLoggingProcessor;
import com.camel.service.service.CurrentTimeService;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class ActiveMqSenderRouter extends RouteBuilder {

    private final CurrentTimeService currentTimeService;

    private final SimpleLoggingProcessor simpleLoggingProcessor;
    @Override
    public void configure() throws Exception {
        from("timer:active-mq-timer?period=200")
                .bean(currentTimeService)
                .process(simpleLoggingProcessor)
                //.log("${body}")
                .to("activemq:my-activemq-queue");
    }
}
