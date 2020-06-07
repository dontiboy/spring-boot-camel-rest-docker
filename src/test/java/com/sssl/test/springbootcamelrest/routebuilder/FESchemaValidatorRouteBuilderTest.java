package com.sssl.test.springbootcamelrest.routebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import com.sssl.test.springbootcamelrest.error.ApiError;
import com.sssl.test.springbootcamelrest.error.FieldError;
import com.sssl.test.springbootcamelrest.ui.dto.FacilityDTO;
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

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.FRONT_END_VALIDATION_FAILED_MESSAGE;
import static com.sssl.test.springbootcamelrest.common.FacilityConstants.VALIDATE_FRONT_END_REQUEST_ROUTE;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FESchemaValidatorRouteBuilderTest {

    @Autowired
    ObjectMapper objectMapper;

    @Produce(uri=VALIDATE_FRONT_END_REQUEST_ROUTE)
    protected ProducerTemplate template;

    @Test
    public void shouldReturnConvertedFacilityEntityDto_for_validFacility_JsonString() throws Exception {

        String validFacilityDTORequest = "{\"id\":\"BBLS0001\",\"turnOver\":10000,\"effectedByCovid19\":true}";

        FacilityDTO facilityDTO =(FacilityDTO) template.sendBody(VALIDATE_FRONT_END_REQUEST_ROUTE, ExchangePattern.InOut,objectMapper.readValue(validFacilityDTORequest,FacilityDTO.class));
        assertTrue(facilityDTO!=null);
        assertEquals("BBLS0001",facilityDTO.getId());
        assertEquals(10000,facilityDTO.getTurnOver());

    }

    @Test
    public void shouldReturnBadRequest_for_invalid_fronend_JsonString() throws Exception {

        String inValidFacilityDTORequest = "{\"id\":\"BBLS0001\",\"effectedByCovid19\":true}";

        ResponseEntity responseEntity=(ResponseEntity) template.sendBody(VALIDATE_FRONT_END_REQUEST_ROUTE, ExchangePattern.InOut,objectMapper.readValue(inValidFacilityDTORequest,FacilityDTO.class));
        assertTrue(responseEntity!=null);
        assertEquals(HttpStatus.BAD_REQUEST.value(),responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        ApiError apiError=(ApiError) responseEntity.getBody();


        assertNotNull(apiError);

        assertEquals(HttpStatus.BAD_REQUEST.value(),apiError.getStatusCode());
        assertEquals(FRONT_END_VALIDATION_FAILED_MESSAGE,apiError.getMessage());
        assertEquals(1,apiError.getFieldErrors().size());
        FieldError fieldError=apiError.getFieldErrors().stream().findFirst().get();
        assertNotNull(fieldError);
        assertEquals("turnOver",fieldError.getField());
        assertEquals("turnOver: must have a minimum value of 8000",fieldError.getMessage());

    }

}