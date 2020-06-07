package com.sssl.test.springbootcamelrest.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sssl.test.springbootcamelrest.model.FacilityEntity;
import com.sssl.test.springbootcamelrest.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoanProcessor implements Processor {

    @Autowired
    LoanService loanService;

    @Override
    public void process(Exchange exchange) throws Exception {

        log.debug("Inside loan processor");

        FacilityEntity facilityEntity=exchange.getIn().getBody(FacilityEntity.class);
        facilityEntity=loanService.processLoan(facilityEntity);
        exchange.getIn().setBody(facilityEntity);
    }
}