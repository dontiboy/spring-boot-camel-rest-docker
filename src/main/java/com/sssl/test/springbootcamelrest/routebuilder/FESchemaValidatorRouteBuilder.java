package com.sssl.test.springbootcamelrest.routebuilder;

import com.sssl.test.springbootcamelrest.exception.FESchemaValidationExceptionProcessor;
import com.sssl.test.springbootcamelrest.ui.dto.FacilityDTO;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.*;

@Component
public class FESchemaValidatorRouteBuilder extends RouteBuilder {

    @Autowired
    FESchemaValidationExceptionProcessor feSchemaValidationExceptionProcessor;

    @Override
    public void configure() throws Exception {

        onException(JsonValidationException.class).handled(true).process(feSchemaValidationExceptionProcessor);
        from(VALIDATE_FRONT_END_REQUEST_ROUTE)
                .routeId(VALIDATE_FE_ROUTEID)
                .log("validating Json with FrontEnd Schema")
                .marshal().json(JsonLibrary.Jackson)
                .to(FRONT_END_JSON_VALIDATION_ENDPOINT)
                .unmarshal().json(JsonLibrary.Jackson, FacilityDTO.class);
    }
}
