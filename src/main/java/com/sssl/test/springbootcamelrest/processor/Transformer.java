package com.sssl.test.springbootcamelrest.processor;

import com.sssl.test.springbootcamelrest.model.FacilityEntity;
import com.sssl.test.springbootcamelrest.ui.dto.FacilityDTO;
import org.apache.camel.Exchange;

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.LOAN_TYPE;

public class Transformer {

    public FacilityEntity dtoToEntity(Exchange exchange){
        FacilityDTO facilityDTO=exchange.getIn().getBody(FacilityDTO.class);
        FacilityEntity facilityEntity=FacilityEntity.builder()
                .requestId(facilityDTO.getId())
                .loanType(LOAN_TYPE)
                .effectedByCovid19(facilityDTO.isEffectedByCovid19())
                .turnOver(facilityDTO.getTurnOver()).build();
        return facilityEntity;
    }

    public FacilityDTO entityToDto(Exchange exchange){
        FacilityEntity facilityEntity=exchange.getIn().getBody(FacilityEntity.class);
        return FacilityDTO.builder()
                .id(facilityEntity.getRequestId())
                .turnOver(facilityEntity.getTurnOver())
                .effectedByCovid19(facilityEntity.isEffectedByCovid19())
                .loanAmountApproved(facilityEntity.getLoanAmountApproved()).build();

    }

}
