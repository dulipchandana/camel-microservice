package com.camel.service.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="route.camel.split-handler.enabled", havingValue = "true")
public class  SplitRouteBuilder  extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:start").id("start")
                .split(body())
                .log("Split line ${body}")
                .to("mock:split");
    }
}
