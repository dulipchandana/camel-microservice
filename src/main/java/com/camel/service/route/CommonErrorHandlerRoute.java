package com.camel.service.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static org.apache.camel.LoggingLevel.WARN;

@Component
@ConditionalOnProperty(name="route.camel.error-handler.enabled", havingValue = "true")
public class CommonErrorHandlerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:exceptionHandler")
                .log(WARN,"in exception handler ${body}");
    }
}

