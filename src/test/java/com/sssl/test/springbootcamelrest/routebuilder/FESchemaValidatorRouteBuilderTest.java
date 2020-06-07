package com.sssl.test.springbootcamelrest.routebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
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

        String validFacilityDTORequest = "{\"id\":\"BBLS0001\",\"turnOver\":10000}";

        FacilityDTO facilityDTO =(FacilityDTO) template.sendBody(VALIDATE_FRONT_END_REQUEST_ROUTE, ExchangePattern.InOut,objectMapper.readValue(validFacilityDTORequest,FacilityDTO.class));
        //ResponseEntity entity=(ResponseEntity) template.sendBody(VALIDATE_FRONT_END_REQUEST_ROUTE, ExchangePattern.InOut,validRequest);
        assertTrue(facilityDTO!=null);
        assertEquals("BBLS0001",facilityDTO.getId());
        assertEquals(10000,facilityDTO.getTurnOver());

    }

    @Test
    public void shouldReturnBadRequest_for_invalid_fronend_JsonString() throws Exception {

        String inValidFacilityDTORequest = "{\"id\":\"BBLS0001\"}";

        ResponseEntity responseEntity=(ResponseEntity) template.sendBody(VALIDATE_FRONT_END_REQUEST_ROUTE, ExchangePattern.InOut,objectMapper.readValue(inValidFacilityDTORequest,FacilityDTO.class));
        assertTrue(responseEntity!=null);
        assertEquals(HttpStatus.BAD_REQUEST.value(),responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        Set<ValidationMessage> validationMessages=(Set<ValidationMessage>) responseEntity.getBody();
        assertNotNull(validationMessages);
        assertEquals(1,validationMessages.size());
        ValidationMessage validationMessage= validationMessages.stream().findFirst().get();
        assertEquals("$.turnOver: must have a minimum value of 8000",validationMessage.getMessage());

    }

}