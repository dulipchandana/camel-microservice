package com.camel.service.route;

import com.camel.service.process.SimpleLoggingProcessor;
import com.camel.service.service.CurrentTimeService;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


//@Component
@AllArgsConstructor
public class TimerRouter extends RouteBuilder {
    private final CurrentTimeService currentTimeService;

    private final SimpleLoggingProcessor simpleLoggingProcessor;

    @Override
    public void configure() throws Exception {
        from("timer:test-timer")
                .log("${body}")
                .transform().constant("Test Transform"+ LocalDateTime.now())
                .log("${body}")
                .bean(currentTimeService)
                .log("${body}")
                .process(simpleLoggingProcessor)
                .to("log:test-timer");
    }
}
