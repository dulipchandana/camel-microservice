package com.camel.service.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ErrorHandlerBean {

    public void getException(@Header("message") String message) {
        log.error("getException {}", message);
        throw new RuntimeException();
    }

    public void logMessage(@Header("message") String message){
        log.info("ErrorHandlerBean {}",message);
    }
}
