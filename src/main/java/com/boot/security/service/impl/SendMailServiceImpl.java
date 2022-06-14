package com.boot.security.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.boot.security.service.SendMailService;
import com.boot.security.service.Util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender JavaMailSender;

    @CreateCache(name="emailCode",timeUnit= TimeUnit.MINUTES,expire = 5)
    Cache<String,String> emailCode;

    @Value("${spring.mail.username}")
    String sender;


    @Override
    public void sendMail(String email) {
        String code= CodeUtil.getCodeWithNoChar(6);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(sender);
        msg.setTo(email);
        msg.setSubject("验证码");
        msg.setText(code);
        JavaMailSender.send(msg);
        emailCode.put("code"+email,code);
    }



    @Override
    public boolean checkCode(String email, String code) {
        String realcode=emailCode.get("code"+email);
        if(realcode!=null && realcode.equals(code))
        {
            emailCode.remove("code"+email);
            return true;
        }
        return false;
    }


}
