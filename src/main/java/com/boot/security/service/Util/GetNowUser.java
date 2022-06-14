package com.boot.security.service.Util;

import com.boot.security.domain.LoginUser;
import com.boot.security.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class GetNowUser {

    public LoginUser getNowUser() {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        return  (LoginUser) auth.getPrincipal();
    }
}
