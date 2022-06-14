package com.boot.security.domain.BookException;

public class NotUserCanFindException extends RuntimeException {
    @Override
    public String getMessage() {
        return super.getMessage()+"no user can find";
    }
}
