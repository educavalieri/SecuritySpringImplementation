package com.educavalieri.dscatolog.services.exceptions;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String msg){

        super(msg);
    }
}
