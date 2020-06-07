package com.sssl.test.springbootcamelrest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacilityEntity {


    private String requestId;
    private String loanType;
    private int turnOver;
    private int loanAmountApproved;
    private boolean  loanApproved;

}
