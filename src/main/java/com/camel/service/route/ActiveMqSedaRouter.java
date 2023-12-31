package com.camel.service.route;

import com.camel.service.process.SimpleLoggingProcessor;
import com.camel.service.service.CurrentTimeService;
import lombok.AllArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.SECONDS;

@AllArgsConstructor
@Component
@ConditionalOnProperty(name="route.camel.seda-handler.enabled", havingValue = "true")
public class ActiveMqSedaRouter extends RouteBuilder {

    private final SimpleLoggingProcessor simpleLoggingProcessor;

    @Override
    public void configure() throws Exception {
        //consume from ActiveMqSenderRouter
        from("activemq:my-activemq-queue").id("my-activemq-queue")
                .wireTap("direct:audit-queue").id("audit-route")
                .to("seda:weightLifter?multipleConsumers=true");

        from("seda:weightLifter?multipleConsumers=true")
                .log(LoggingLevel.INFO,"${body}")
                .process(exchange -> SECONDS.sleep(2))
                .end();

        from("direct:audit-queue")
                .process(simpleLoggingProcessor)
                .to("activemq:my-activemq-queue-audit");

    }
}
