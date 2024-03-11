package com.camel.service.process;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.UUID;


@Service
@Slf4j
public class SimpleLoggingProcessor implements Processor {

    public final static String CORRELATION_ID = "correlationId";

    public final static String ID = "id";

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getMessage().getBody(String.class);
        Message message = exchange.getMessage();
        message.setHeader(CORRELATION_ID, (new Random()).nextInt(10));
        message.setHeader(ID, exchange.getMessage().getMessageId());
        message.setBody("After Processor-"+body);
        exchange.setMessage(message);

        //log.info("Simple logging Component {}", exchange.getMessage().getBody());
    }
}
