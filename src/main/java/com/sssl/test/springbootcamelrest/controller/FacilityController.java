package com.sssl.test.springbootcamelrest.controller;


import com.sssl.test.springbootcamelrest.ui.dto.FacilityDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.FACILITY_ROUTE;
import static com.sssl.test.springbootcamelrest.common.FacilityConstants.FACILITY_URI;

@RestController
public class FacilityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityController.class);

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @PostMapping(value=FACILITY_URI, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity applyFacility(@RequestBody FacilityDTO facilityDTO){
       return producerTemplate.requestBody(FACILITY_ROUTE,facilityDTO,ResponseEntity.class);
    }


}


