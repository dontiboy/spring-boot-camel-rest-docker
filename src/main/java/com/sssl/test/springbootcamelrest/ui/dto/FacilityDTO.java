package com.sssl.test.springbootcamelrest.ui.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDTO {

    private String id;
    private int turnOver;
    private boolean loanApproved;
    private int loanAmountApproved;


}
