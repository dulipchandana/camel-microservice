package com.camel.service.route;

import com.camel.service.service.ErrorHandlerBean;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.camel.LoggingLevel.INFO;

@Component
@ConditionalOnProperty(name="route.camel.error-handler.enabled2", havingValue = "true")
@AllArgsConstructor
public class ErrorHandlerRoute extends RouteBuilder {

    final static AtomicInteger counter = new AtomicInteger(1);

    private final ErrorHandlerBean errorHandlerBean;

    @Override
    public void configure() throws Exception {
        errorHandler(
                deadLetterChannel("direct:exceptionHandler")
                        .maximumRedeliveries(2)
        );

        from("timer:time?period=1000")
                .process(exchange-> exchange.getIn().setBody(new Date()))
                .choice()
                    .when(e -> counter.incrementAndGet() %2 ==0)
                        .setHeader("message", simple(counter.toString()))
                        .bean(errorHandlerBean,"getException")
                    .otherwise()
                        .setHeader("message", simple(counter.toString()))
                        .bean(errorHandlerBean,"logMessage")
                    .end()
                .log(INFO,">> ${header.firedTime}>> ${body}")
                .to("log:reply");


    }
}
