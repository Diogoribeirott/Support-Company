package com.suport.api.exceptions.details;

import com.suport.api.exceptions.defaultExceptionDetails.ExceptionDetails;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {
    private final String fields;        
    private final String fieldsMessage;


}
