package com.boot.security.service;

import org.springframework.http.ResponseEntity;

public interface SendMailService {

    public void sendMail(String email);


    public boolean checkCode(String email,String code);
}
