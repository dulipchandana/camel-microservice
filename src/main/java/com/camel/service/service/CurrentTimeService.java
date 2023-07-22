package com.camel.service.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CurrentTimeService {
    public String getCurrentTime() {
        return "Time-"+ LocalDateTime.now();
    }
}
