package com.camel.service.route;

import com.camel.service.service.ErrorHandlerBean;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.camel.LoggingLevel.ERROR;
import static org.apache.camel.LoggingLevel.INFO;

@Component
@ConditionalOnProperty(name="route.camel.error-handler.enabled", havingValue = "true")
@AllArgsConstructor
public class OnExceptionHandlerRoute extends RouteBuilder {

    final static AtomicInteger counter = new AtomicInteger(1);

    private final ErrorHandlerBean errorHandlerBean;

    @Override
    public void configure() throws Exception {


       onException(Exception.class)
               .log(ERROR,"OEH: ${exception}")
                       .handled(true)
                               .maximumRedeliveries(4)
                                       .to("direct:exceptionHandler");

        from("timer:time?period=1000")
                .process(exchange-> exchange.getIn().setBody(counter.incrementAndGet()))
                .choice()
                .when(e -> Integer.parseInt(e.getMessage().getBody().toString()) %2 ==0)
                .setHeader("message", simple("${body}"))
                .bean(errorHandlerBean,"getException")
                .otherwise()
                .setHeader("message", simple("${body}"))
                .bean(errorHandlerBean,"logMessage")
                .end()
                .log(INFO,">> ${header.firedTime}>> ${body}")
                .to("log:reply");


    }
}
