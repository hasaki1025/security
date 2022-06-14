package com.boot.security.Controller;

import com.boot.security.domain.User;
import com.boot.security.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Value("${JWT.FreeLoginTTL}")
    Long FreeLoginTTL;


    @PostMapping("/login")
    ResponseEntity<String> login(@Valid User user, Integer freeLogin)
    {
        try {
            if(freeLogin!=null)
            {
                return loginService.login(user,FreeLoginTTL);
            }
            else
            {
                return loginService.login(user,null);
            }

        }
         catch (Exception e) {
            e.printStackTrace();
             HttpHeaders headers = new HttpHeaders();
             headers.add("message","登录失败");
             return new ResponseEntity<>(null,headers,HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/logout")
    @PreAuthorize("hasAnyAuthority('test','Admin')")
    ResponseEntity<String> logout()
    {
        return loginService.logout();
    }


    @GetMapping("/b")
    @PreAuthorize("hasAnyAuthority('test','Admin')")
    String  b()
    {
        return "验证Token成功";
    }
}
