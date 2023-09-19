package com.cerclex.epr.delivery.exception;

public class BrandAlreadyExistsException extends Exception{
    public BrandAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
