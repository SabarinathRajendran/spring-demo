package com.cerclex.epr.apiGateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionResponseModel {
    private String errCode;
    private String err;
    private String errDetails;
    private String message;
    private Date errDate;
}
