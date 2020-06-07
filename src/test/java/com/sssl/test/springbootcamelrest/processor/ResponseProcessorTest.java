package com.sssl.test.springbootcamelrest.processor;

import com.sssl.test.springbootcamelrest.ui.dto.FacilityDTO;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(CamelSpringBootRunner.class)
public class ResponseProcessorTest {

    @Mock
    private CamelContext context;

    @InjectMocks
    private ResponseProcessor  responseProcessor;

    Exchange exchange;

    @Test
    public void shouldReturnResponseEntityForGivenFacilityEntityWithStatusCreated() throws Exception {
        FacilityDTO facilityDTO = FacilityDTO.builder().id("Sample111").turnOver(10000)
                            .loanAmountApproved(2500).loanApproved(true).build();
        exchange = ExchangeBuilder.anExchange(context)
                    .withBody(facilityDTO)
                    .withPattern(ExchangePattern.InOut)
                    .build();

        responseProcessor.process(exchange);

        ResponseEntity<FacilityDTO> facilityDtoResponseEntity = exchange.getIn().getBody(ResponseEntity.class);
        assertNotNull(facilityDtoResponseEntity);
        assertEquals(HttpStatus.CREATED.value(),facilityDtoResponseEntity.getStatusCodeValue());

        FacilityDTO facilityDTOFromResponse=facilityDtoResponseEntity.getBody();
        assertNotNull(facilityDTOFromResponse.getId());
        assertEquals("Sample111",facilityDTOFromResponse.getId());
        assertEquals(10000,facilityDTOFromResponse.getTurnOver());
        assertTrue(facilityDTOFromResponse.isLoanApproved());
        assertEquals(2500,facilityDTOFromResponse.getLoanAmountApproved());

    }
}