package com.boot.security.domain.BookException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotBookCanFind extends BookException{
    @Override
    public void printStackTrace() {
        super.printStackTrace();
        log.error("Not book can find");
    }
}
