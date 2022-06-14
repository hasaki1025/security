package com.boot.security.service;

import com.boot.security.domain.User;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    public ResponseEntity<String> login(User user, Long TTL) throws Exception;

    public ResponseEntity<String> logout();
}
