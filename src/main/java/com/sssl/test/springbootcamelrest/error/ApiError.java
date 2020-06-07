package com.sssl.test.springbootcamelrest.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
public class ApiError {

    private int  statusCode;
    private String message;
    private List<FieldError> fieldErrors;


}
