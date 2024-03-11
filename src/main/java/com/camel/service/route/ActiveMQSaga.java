package com.camel.service.route;

import com.camel.service.process.SimpleLoggingProcessor;
import com.camel.service.service.SagaManagerService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.InMemorySagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name="route.camel.saga-handler.enabled", havingValue = "true")
public class ActiveMQSaga extends RouteBuilder {

    @Autowired
    private SimpleLoggingProcessor simpleLoggingProcessor;

    @Autowired
    private SagaManagerService sagaManagerService;

    @Override
    public void configure() throws Exception {

        getContext().addService(new InMemorySagaService());

        from("activemq:my-activemq-queue")
                .process(simpleLoggingProcessor)
                .saga()
                .to("direct:start")
                .to("direct:middle")
                .to("direct:end");

        from("direct:start")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .option(SimpleLoggingProcessor.ID, header(SimpleLoggingProcessor.ID))
                .setBody(body())
                .compensation("direct:cancelOrder")
                .bean(sagaManagerService,"newOrder")
                .log("start order : ${header.id}");

        from("direct:cancelOrder")
                .setBody(body())
                .log("cancelRoute : ${header.id}")
                .bean(sagaManagerService,"cancelOrder");

        from ("direct:middle")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .option(SimpleLoggingProcessor.ID, header(SimpleLoggingProcessor.ID))
                .compensation("direct:cancelMiddle")
                .bean(sagaManagerService,"middleOrder");

        from("direct:cancelMiddle")
                .setBody(body())
                .log("cancelMiddle : ${header.id}")
                .bean(sagaManagerService,"cancelMiddle");

        from("direct:end")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .option(SimpleLoggingProcessor.ID, header(SimpleLoggingProcessor.ID))
                .bean(sagaManagerService,"end");



    }
}
