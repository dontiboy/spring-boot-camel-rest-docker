package com.sssl.test.springbootcamelrest.routebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import com.sssl.test.springbootcamelrest.error.ApiError;
import com.sssl.test.springbootcamelrest.error.FieldError;
import com.sssl.test.springbootcamelrest.model.FacilityEntity;
import com.sssl.test.springbootcamelrest.ui.dto.FacilityDTO;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.*;
import static org.junit.jupiter.api.Assertions.*;


@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FacilityRouteBuilderTest {

    @Autowired
    ObjectMapper objectMapper;

    @Produce(uri=FACILITY_ROUTE)
    protected ProducerTemplate template;

    @Test
    public void shouldReturnFacilityEntityDto_with_LoanApproved_Amount_for_validFacilityDTO_JsonString() throws Exception {

        String validFacilityDTORequest = "{\"id\":\"BBLS0001\",\"turnOver\":10000,\"effectedByCovid19\":true}";
        ResponseEntity<FacilityDTO> facilityEntityResponseEntity =(ResponseEntity) template.sendBody(FACILITY_ROUTE, ExchangePattern.InOut,objectMapper.readValue(validFacilityDTORequest,FacilityDTO.class));


        assertNotNull(facilityEntityResponseEntity);
        assertEquals(HttpStatus.CREATED.value(),facilityEntityResponseEntity.getStatusCodeValue());

        FacilityDTO facilityDTO=facilityEntityResponseEntity.getBody();
        assertNotNull(facilityDTO.getId());
        assertEquals("BBLS0001",facilityDTO.getId());
        assertEquals(10000,facilityDTO.getTurnOver());
        assertTrue(facilityDTO.isEffectedByCovid19());
        assertEquals(2500,facilityDTO.getLoanAmountApproved());

    }

    @Test
    public void shouldReturnValidationErrors_with_Status_BadRequest_for_InvalidTurnOver_In_JsonString() throws Exception {
        String validFacilityDTORequest = "{\"id\":\"BBLS0002\",\"turnOver\":7000,\"effectedByCovid19\":true}";
        ResponseEntity<Set<ValidationMessage>> responseEntity = (ResponseEntity) template.sendBody(FACILITY_ROUTE, ExchangePattern.InOut, objectMapper.readValue(validFacilityDTORequest, FacilityDTO.class));
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

    @Test
    public void shouldReturnValidationErrors_With_Status_UnProceesableEntity_for_InvalidJsonString() throws Exception {
        String validFacilityDTORequest = "{\"id\":\"BBLS0002\",\"turnOver\":9000,\"effectedByCovid19\":false}";
        ResponseEntity responseEntity =(ResponseEntity) template.sendBody(FACILITY_ROUTE, ExchangePattern.InOut,objectMapper.readValue(validFacilityDTORequest,FacilityDTO.class));


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