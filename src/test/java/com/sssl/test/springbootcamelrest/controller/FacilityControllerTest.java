package com.sssl.test.springbootcamelrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sssl.test.springbootcamelrest.model.FacilityEntity;
import com.sssl.test.springbootcamelrest.processor.Transformer;
import com.sssl.test.springbootcamelrest.service.LoanService;
import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.FACILITY_URI;
import static com.sssl.test.springbootcamelrest.common.FacilityConstants.LOAN_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class FacilityControllerTest {

    @MockBean
    LoanService loanService;

    @Autowired
    private MockMvc mockMvc;


    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldReturn_HttpStatus_200_whenValidJsonRequest() throws Exception {

      String validFacilityDTORequest = "{\"id\":\"BBLS0001\",\"turnOver\":10000,\"effectedByCovid19\":true}";

      when(loanService.processLoan(any(FacilityEntity.class))).thenReturn(approvedLoanFacilityEntity());

      this.mockMvc.perform(MockMvcRequestBuilders.post(FACILITY_URI)
                .content(validFacilityDTORequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("id").value("BBLS0001"))
                .andExpect(jsonPath("turnOver").value(10000))
                .andExpect(jsonPath("effectedByCovid19").value(true))
                .andExpect(jsonPath("loanAmountApproved").value(2500));

    }

    @Test
    public void shouldReturn_HttpStatus_400_with_Invalid_LoanAmountJsonRequest() throws Exception {

        String invalidFacilityDTORequest = "{\"id\":\"BBLS0001\",\"turnOver\":7000,\"effectedByCovid19\":true}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(FACILITY_URI)
                .content(invalidFacilityDTORequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Invalid Input"))
                .andExpect(jsonPath("fieldErrors[0].field").value("turnOver"))
                .andExpect(jsonPath("fieldErrors[0].message").value("turnOver: must have a minimum value of 8000"));

    }


    @Test
    public void shouldReturn_HttpStatus_422_with_Invalid_LoanAmountJsonRequest() throws Exception {

        String invalidFacilityDTORequest = "{\"id\":\"BBLS0001\",\"turnOver\":9000,\"effectedByCovid19\":false}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(FACILITY_URI)
                .content(invalidFacilityDTORequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("message").value("Unable to fulfil request with given data"))
                .andExpect(jsonPath("fieldErrors[0].field").value("effectedByCovid19"))
                .andExpect(jsonPath("fieldErrors[0].message").value("effectedByCovid19: does not have a value in the enumeration [true]"));

    }


    @Test
    public void shouldReturn_HttpStatus_500_whenThereisInternalServerError() throws Exception {

        String validFacilityDTORequest = "{\"id\":\"BBLS0001\",\"turnOver\":10000,\"effectedByCovid19\":true}";

        when(loanService.processLoan(any(FacilityEntity.class))).thenThrow(new IllegalStateException());

        this.mockMvc.perform(MockMvcRequestBuilders.post(FACILITY_URI)
                .content(validFacilityDTORequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("message").value("Application Specific Error Occurred"))
                .andExpect(jsonPath("statusCode").value(HttpStatus.INTERNAL_SERVER_ERROR.value()));


    }
    private FacilityEntity approvedLoanFacilityEntity(){
        return FacilityEntity.builder().requestId("BBLS0001").loanType(LOAN_TYPE).turnOver(10000).effectedByCovid19(true).loanAmountApproved(2500).build();
    }
}