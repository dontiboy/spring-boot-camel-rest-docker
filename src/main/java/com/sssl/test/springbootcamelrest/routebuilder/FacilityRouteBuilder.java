package com.sssl.test.springbootcamelrest.routebuilder;

import com.sssl.test.springbootcamelrest.processor.LoanProcessor;
import com.sssl.test.springbootcamelrest.processor.ResponseProcessor;
import com.sssl.test.springbootcamelrest.processor.Transformer;
import com.sssl.test.springbootcamelrest.ui.dto.FacilityDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.*;

@Component
public class FacilityRouteBuilder extends RouteBuilder {

    @Autowired
    LoanProcessor loanProcessor;

    @Autowired
    ResponseProcessor responseProcessor;

    @Override
    public void configure() throws Exception {

        from(FACILITY_ROUTE)
                    .routeId(FACILITY_ROUTE_ID)
                    .tracing()
                    .log("id :${body.id}")
                    .log("turn over amount :${body.turnOver}")
                    .to(VALIDATE_FRONT_END_REQUEST_ROUTE)
                    .bean(new Transformer(),"dtoToEntity")
                    .to(VALIDATE_BACK_END_REQUEST_ROUTE)
                    .process(loanProcessor)
                    .bean(new Transformer(),"entityToDto")
                    .process(responseProcessor);


    }
}
