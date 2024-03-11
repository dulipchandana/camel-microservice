package com.camel.service.service;

import com.camel.service.process.SimpleLoggingProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SagaManagerService {

    public void newOrder(Exchange exchange) {
        log.info("newOrder {},{} ",getMessage(exchange),getCorrelationId(exchange));
        if(getCorrelationId(exchange)%2 ==1){
           throw  new RuntimeException("Error in new order"+getCorrelationId(exchange));
        }
    }

    public void cancelOrder(Exchange exchange) {
        log.info("cancelOrder {}",getMessage(exchange));
    }

    public void middleOrder(Exchange exchange){
        log.info("middleOrder {},{}",getMessage(exchange),getCorrelationId(exchange));
        if(getCorrelationId(exchange)%3 ==1){
            throw  new RuntimeException("Error in middle order "+getCorrelationId(exchange));
        }
    }

    public void cancelMiddle(Exchange exchange){
        log.info("cancelMiddle {}",getMessage(exchange));
    }

    public void end(Exchange exchange){
        log.info("end {},{}",getMessage(exchange),getCorrelationId(exchange));
    }

    private int getCorrelationId(Exchange exchange){
        return (int) getMessage(exchange).getHeader(SimpleLoggingProcessor.CORRELATION_ID);
    }

    private Message getMessage(Exchange exchange){
        return exchange.getMessage();
    }
}
