package com.camel.service.route;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.apache.camel.component.mock.MockEndpoint.assertIsSatisfied;

@MockEndpoints
class SplitRouteBuilderTest extends CamelTestSupport {
    @Test
    void splitTest() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:split");
        mock.expectedBodiesReceived("A","B","C");
        List<String> body = Arrays.asList("A","B","C");
        template.sendBody("direct:start",body);
        mock.expectedMessageCount(3);
        assertIsSatisfied(mock);
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new SplitRouteBuilder();
    }

}