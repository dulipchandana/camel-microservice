package com.camel.service.service;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CurrentTimeService {
    public String getCurrentTime() {

        return LocalDateTime.now().toString();
    }

    @Handler
    public  String appendMessage(Exchange exchange, String header){
        return this.getClass() + exchange.getMessage().getBody().toString()+header;
    }
}
