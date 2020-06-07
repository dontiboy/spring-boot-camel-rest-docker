package com.sssl.test.springbootcamelrest.common;

public class FacilityConstants {


    public static final String FACILITY_URI="/applyFacility";

    public static final String FACILITY_ROUTE="direct:facilityRoute";
    public static  final String FACILITY_ROUTE_ID="FacilityRouteId";

    public static  final  String VALIDATE_FRONT_END_REQUEST_ROUTE="direct:validateFrontEndRequest";
    public static  final String VALIDATE_FE_ROUTEID="ValidateFERouteId";

    public static  final String VALIDATE_BACK_END_REQUEST_ROUTE="direct:validateBackEndRequest";
    public static  final String VALIDATE_BE_ROUTEID="ValidateBERouteId";

    public static  final String FRONT_END_VALIDATION_FAILED_MESSAGE="Invalid Input";
    public static  final String BACK_END_VALIDATION_FAILED_MESSAGE="Unable to fulfil request with given data";


    public static final String LOAN_TYPE="BBLS Loan";


    public static final String JSON_VALIDATOR_ENDPOINT ="json-validator:";
    public static final String FRONT_END_JSON_VALIDATION_ENDPOINT= JSON_VALIDATOR_ENDPOINT +"facility_ui_request.json";
    public static final String BACK_END_JSON_VALIDATION_ENDPOINT= JSON_VALIDATOR_ENDPOINT +"back_end_schema.json";

}



