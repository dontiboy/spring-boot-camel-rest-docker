package com.sssl.test.springbootcamelrest.exception;

import com.networknt.schema.ValidationMessage;
import com.sssl.test.springbootcamelrest.error.ApiError;
import com.sssl.test.springbootcamelrest.error.FieldError;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.spi.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.*;
@Component
public class FESchemaValidationExceptionProcessor implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {
             JsonValidationException exception = exchange.getProperty("CamelExceptionCaught",JsonValidationException.class);

             List<FieldError> fieldErrorList= extractFieldErrors(exception.getErrors());
             ApiError error=ApiError.builder()
                                    .statusCode(HttpStatus.BAD_REQUEST.value())
                                    .message(FRONT_END_VALIDATION_FAILED_MESSAGE)
                                    .fieldErrors(fieldErrorList)
                                    .build();
             ResponseEntity responseEntity= ResponseEntity.badRequest().body(error);
             exchange.getIn().setBody(responseEntity);
             exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
             exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.BAD_REQUEST.value());
    }

    private List<FieldError> extractFieldErrors(Set<ValidationMessage> validationErrors) {

        List<FieldError> fieldErrors= new ArrayList<>();

        if(!CollectionUtils.isEmpty(validationErrors)){
            return validationErrors.stream().map(error -> FieldError.builder()
                                    .field(error.getPath().replaceAll("\\$.","")).message(error.getMessage().replaceAll("\\$.","")).build()).collect(Collectors.toList());
        }
        return fieldErrors;

    }


}
