package com.sssl.test.springbootcamelrest.routebuilder;

import com.sssl.test.springbootcamelrest.exception.SchemaValidationExceptionProcessor;
import com.sssl.test.springbootcamelrest.model.FacilityEntity;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sssl.test.springbootcamelrest.common.FacilityConstants.*;

@Component
public class BESchemaValidatorRouteBuilder extends RouteBuilder {

    @Autowired
    SchemaValidationExceptionProcessor schemaValidationExceptionProcessor;

    @Override
    public void configure() throws Exception {


        onException(JsonValidationException.class).handled(true).process(schemaValidationExceptionProcessor);
        from(VALIDATE_BACK_END_REQUEST_ROUTE)
                .routeId(VALIDATE_BE_ROUTEID)
                .log("validating Json with BackEnd Schema body before :${body}")
                .marshal().json(JsonLibrary.Jackson)
                .to(BACK_END_JSON_VALIDATION_ENDPOINT)
                .log("body after : ${body}")
                .unmarshal().json(JsonLibrary.Jackson, FacilityEntity.class).log("body after unmashalling ${body}");

    }
}
