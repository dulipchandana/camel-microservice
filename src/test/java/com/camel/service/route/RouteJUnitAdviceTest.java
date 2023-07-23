package com.camel.service.route;

import com.camel.service.process.SimpleLoggingProcessor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.apache.camel.builder.AdviceWith.adviceWith;

@MockEndpoints
public class RouteJUnitAdviceTest extends CamelTestSupport {

    @Spy
    private SimpleLoggingProcessor simpleLoggingProcessor;

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new SplitRouteBuilder();
    }

    @Test
    public void testMockEndPoints() throws Exception {
        RouteDefinition route = context.getRouteDefinition("start");

        adviceWith(route, context,
                new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                      weaveAddLast().to("mock:finish");
                    }
                });

        context.start();
        MockEndpoint mock = getMockEndpoint("mock:finish");
        mock.expectedMessageCount(1);

        template.sendBody("direct:start","Test");
        mock.assertIsSatisfied();
    }
}
