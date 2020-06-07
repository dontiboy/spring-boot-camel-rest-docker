package com.sssl.test.springbootcamelrest.processor;

import com.sssl.test.springbootcamelrest.ui.dto.FacilityDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity <FacilityDTO> responseEntity = new ResponseEntity<>(exchange.getIn().getBody(FacilityDTO.class),responseHeaders,HttpStatus.CREATED);
        exchange.getIn().setBody(responseEntity);
    }
}
