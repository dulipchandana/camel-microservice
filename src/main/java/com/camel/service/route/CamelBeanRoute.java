package com.camel.service.route;

import com.camel.service.service.CurrentTimeService;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static com.camel.service.process.SimpleLoggingProcessor.CORRELATION_ID;
import static org.apache.camel.LoggingLevel.INFO;
import static org.apache.camel.component.bean.BeanConstants.BEAN_METHOD_NAME;

@Component
@ConditionalOnProperty(name="route.camel.bean-handler.enabled", havingValue = "true")
@AllArgsConstructor
public class CamelBeanRoute extends RouteBuilder {

    private final CurrentTimeService currentTimeService;
    @Override
    public void configure() throws Exception {
        from("timer:testTimer?period=10000")
                .setHeader(CORRELATION_ID,() ->(new Random().nextInt(10)))
                .to("bean:com.camel.service.service.CurrentTimeService?method=getCurrentTime")
                //.setHeader(BEAN_METHOD_NAME,()->"getCurrentTime")
                //enforcing the method name
                //.to("bean:com.camel.service.service.CurrentTimeService?method=appendMessage(${exchange},${header."+CORRELATION_ID+"})")
                .bean(currentTimeService,"appendMessage(${exchange},${header."+CORRELATION_ID+"})")
                .log(INFO,"${body}");
    }
}
