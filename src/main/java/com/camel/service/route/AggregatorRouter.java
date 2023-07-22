package com.camel.service.route;

import com.camel.service.strategy.CorrelationAggregationStrategy;
import lombok.AllArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

import static com.camel.service.process.SimpleLoggingProcessor.CORRELATION_ID;
//this work with ActiveMqSenderRouter
@AllArgsConstructor
@Service
public class AggregatorRouter extends RouteBuilder {

    private final CorrelationAggregationStrategy correlationAggregationStrategy;
    @Override
    public void configure() throws Exception {
        from("activemq:my-activemq-queue")

                .aggregate(header(CORRELATION_ID), correlationAggregationStrategy)
                .completionSize(10)
                .log(LoggingLevel.INFO,"${header."+CORRELATION_ID+"}${body}")

        ;
    }
}
