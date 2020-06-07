package com.sssl.test.springbootcamelrest.service;

import com.sssl.test.springbootcamelrest.model.FacilityEntity;
import org.springframework.stereotype.Component;

@Component
public interface LoanService {


    public FacilityEntity processLoan(FacilityEntity facilityEntity);}
