package com.boot.security.service.Util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil<T> {
    private  ResponseEntity<T> response;
    HttpStatus httpStatus;

    public ResponseUtil<T> ok()
    {
        httpStatus=HttpStatus.OK;
        return this;
    }

    public ResponseUtil<T> isBad()
    {
        httpStatus=HttpStatus.BAD_REQUEST;
        return this;
    }

   public  ResponseEntity<T> createResponseEntity()
   {
       response=new ResponseEntity<T>(httpStatus);
       return response;
   }

    public ResponseEntity<T> createResponseEntity(String message)
    {

        HttpHeaders headers = new HttpHeaders();
        headers.set("message",message);
        response=new ResponseEntity<T>(null,headers,httpStatus);
        return response;
    }


    public ResponseEntity<T> createResponseEntity(String message,T body)
    {

        HttpHeaders headers = new HttpHeaders();
        headers.set("message",message);
        response=new ResponseEntity<T>(body,headers,httpStatus);
        return response;
    }


}
