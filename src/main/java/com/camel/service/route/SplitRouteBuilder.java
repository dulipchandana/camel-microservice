package com.camel.service.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SplitRouteBuilder  extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:start").id("start")
                .split(body())
                .log("Split line ${body}")
                .to("mock:split");
    }
}
