package com.sssl.test.springbootcamelrest.routebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import com.sssl.test.springbootcamelrest.error.ApiError;
import com.sssl.test.springbootcamelrest.error.FieldError;
import com.sssl.test.springbootcamelrest.model.FacilityEntity;
import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BESchemaValidatorRouteBuilderTest {

    @Autowired
    ObjectMapper objectMapper;

    @Produce(uri=VALIDATE_BACK_END_REQUEST_ROUTE)
    protected ProducerTemplate template;

    @Test
    public void shouldReturnConvertedFacilityEntityDto_for_validFacility_JsonString() throws Exception {

        String validFacilityEntityRequest = "{\"requestId\":\"BBLS0001\",\"turnOver\":10000,\"loanType\":\"BBLS Loan\",\"effectedByCovid19\":true}";

        FacilityEntity facilityEntity =(FacilityEntity) template.sendBody(VALIDATE_BACK_END_REQUEST_ROUTE, ExchangePattern.InOut,objectMapper.readValue(validFacilityEntityRequest, FacilityEntity.class));

        assertNotNull(facilityEntity);
        assertEquals("BBLS0001",facilityEntity.getRequestId());
        assertEquals(10000,facilityEntity.getTurnOver());
        assertEquals(LOAN_TYPE,facilityEntity.getLoanType());

    }

    @Test
    public void shouldReturnUnProcessableEntity_for_invalid_fronend_JsonString() throws Exception {

        String inValidFacilityDTORequest = "{\"requestId\":\"BBLS0001\",\"turnOver\":10000,\"loanType\":\"BBLS Loan\",\"effectedByCovid19\":false}";
        ResponseEntity responseEntity=(ResponseEntity) template.sendBody(VALIDATE_BACK_END_REQUEST_ROUTE, ExchangePattern.InOut,objectMapper.readValue(inValidFacilityDTORequest,FacilityEntity.class));

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(),responseEntity.getStatusCodeValue());

        assertNotNull(responseEntity.getBody());
        ApiError apiError=(ApiError) responseEntity.getBody();

        assertNotNull(apiError);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(),apiError.getStatusCode());
        assertEquals(BACK_END_VALIDATION_FAILED_MESSAGE,apiError.getMessage());
        assertEquals(1,apiError.getFieldErrors().size());
        FieldError fieldError=apiError.getFieldErrors().stream().findFirst().get();
        assertNotNull(fieldError);
        assertEquals("effectedByCovid19",fieldError.getField());
        assertEquals("effectedByCovid19: does not have a value in the enumeration [true]",fieldError.getMessage());

    }
}