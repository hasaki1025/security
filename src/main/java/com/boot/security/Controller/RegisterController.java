package com.boot.security.Controller;

import com.boot.security.domain.LoginUser;
import com.boot.security.domain.User;
import com.boot.security.service.SendMailService;
import com.boot.security.service.UserService;
import com.boot.security.service.Util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    UserService userService;

    @Autowired
    SendMailService sendMailService;

    @PostMapping
    ResponseEntity<String> register(@Valid User user,String code)
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (!sendMailService.checkCode(user.getUEmail(),code) || userService.addUser(user)!=1) {
            httpHeaders.add("message","register defeat");
            return new ResponseEntity<>(null,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        httpHeaders.add("message","register successfully");
        return new ResponseEntity<>(null,httpHeaders, HttpStatus.OK);
    }


    @GetMapping("/sendmail")
    ResponseEntity<String> sendMail(String mail)
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            sendMailService.sendMail(mail);
            httpHeaders.add("message","send mail successfully");
            return new ResponseEntity<>(null,httpHeaders, HttpStatus.OK);
        }catch (Exception e)
        {
            e.printStackTrace();

            httpHeaders.add("message","send mail defeat");
            return new ResponseEntity<>(null,httpHeaders, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/resetpassword")
    @PreAuthorize("hasAnyAuthority('Admin','test')")
    ResponseEntity<String> ResetPwd(String code,String password)
    {
        try {
            Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) auth.getPrincipal();
            String email=loginUser.getUser().getUEmail();
            User user = loginUser.getUser();
            if (!sendMailService.checkCode(email,code) || userService.updatePassword(user,password)!=1) {
                throw new RuntimeException();
            }
            return  new ResponseUtil<String>().ok().createResponseEntity("alert password successfully");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseUtil<String>().isBad().createResponseEntity("alert password defeat");
        }

    }





}
