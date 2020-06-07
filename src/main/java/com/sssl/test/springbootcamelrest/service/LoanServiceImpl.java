package com.sssl.test.springbootcamelrest.service;

import com.sssl.test.springbootcamelrest.model.FacilityEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LoanServiceImpl implements LoanService {


    private int MIN_TURNOVER=8000;
    private int MAX_TURNOVER=200000;

    public FacilityEntity processLoan(FacilityEntity facilityEntity){
        int turnover=MAX_TURNOVER;
        facilityEntity.setLoanApproved(false);
        if(facilityEntity.getTurnOver()<MIN_TURNOVER || facilityEntity.getTurnOver()<=0){
            facilityEntity.setLoanAmountApproved(0);
        }else {
            if(facilityEntity.getTurnOver()<turnover) {
                turnover=facilityEntity.getTurnOver();
            }
            facilityEntity.setLoanAmountApproved(turnover*25/100);
            facilityEntity.setLoanApproved(true);
        }
        return facilityEntity;

}
}
