package com.sssl.test.springbootcamelrest.exception;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.spi.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class SchemaValidationExceptionProcessor implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {
            JsonValidationException exception = exchange.getProperty("CamelExceptionCaught",JsonValidationException.class);

             ResponseEntity responseEntity= ResponseEntity.badRequest().body(exception.getErrors());
              exchange.getIn().setBody(responseEntity);
              exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
              exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
    }
}
