package com.cerclex.epr.exception;

public class EPRGatewayException extends Exception{
    public EPRGatewayException(String errorMessage){
        super(errorMessage);
    }
}
