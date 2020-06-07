package com.sssl.test.springbootcamelrest.processor;

import com.sssl.test.springbootcamelrest.model.FacilityEntity;
import com.sssl.test.springbootcamelrest.service.LoanService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.sssl.test.springbootcamelrest.common.FacilityConstants.LOAN_TYPE;
import static org.mockito.Mockito.when;


@RunWith(CamelSpringBootRunner.class)
public class LoanProcessorTest {

    @Mock
    private CamelContext context;

    @InjectMocks
    private LoanProcessor loanProcessor;

    Exchange exchange;


    @Mock
    LoanService loanService;

    @Test
    public void shouldUpdateLoanAmountForValidRequestFromExchange() throws Exception {
        FacilityEntity facilityEntity = FacilityEntity.builder().requestId("Sample111").turnOver(10000).loanType(LOAN_TYPE).build();
        exchange = ExchangeBuilder.anExchange(context).withBody(facilityEntity).build();

        when(loanService.processLoan(any(FacilityEntity.class))).thenReturn(approvedLoanFacilityEntity());

        loanProcessor.process(exchange);

        FacilityEntity returnedFacilityEntity = exchange.getIn().getBody(FacilityEntity.class);
        assertEquals("Sample111", returnedFacilityEntity.getRequestId());
        assertEquals(LOAN_TYPE, returnedFacilityEntity.getLoanType());
        assertEquals(2500, returnedFacilityEntity.getLoanAmountApproved());
        assertTrue(returnedFacilityEntity.isEffectedByCovid19());
    }


    private FacilityEntity approvedLoanFacilityEntity() {
        return FacilityEntity.builder().requestId("Sample111").loanType(LOAN_TYPE).turnOver(10000).effectedByCovid19(true).loanAmountApproved(2500).build();
    }
}