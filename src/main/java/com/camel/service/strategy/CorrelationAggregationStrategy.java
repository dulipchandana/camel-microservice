package com.camel.service.strategy;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class CorrelationAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if(oldExchange==null){
            return newExchange;
        }

        String oldBody = oldExchange.getIn().getBody(String.class);
        String newBody = oldExchange.getIn().getBody(String.class);

        String aggBody = oldBody + "->" + newBody;
        oldExchange.getIn().setBody(aggBody);
        return oldExchange;
    }
}
