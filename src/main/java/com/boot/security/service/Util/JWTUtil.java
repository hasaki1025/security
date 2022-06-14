package com.boot.security.service.Util;

import com.alicp.jetcache.anno.Cached;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JWTUtil {


    private final String JWT_key="wuhu";

    @Value("${JWT.defaultTTL}")
    private  long defaultTTL;

    @Autowired
    JWTUtil jwtUtil;

    public String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    private  JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=defaultTTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("wuhu")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    public  SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWT_key);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public  String createJWT(String subject, String id) throws JsonProcessingException {
        JwtBuilder builder = jwtUtil.getJwtBuilder(subject, defaultTTL, id);// 设置过期时间
        return builder.compact();
    }



    public  String createJWT(String subject,Long TTL,String id) throws JsonProcessingException {
        JwtBuilder builder = jwtUtil.getJwtBuilder(subject, TTL, id);// 设置过期时间
        return builder.compact();
    }


    public  String createJWT(String subject) throws JsonProcessingException {
        JwtBuilder builder = jwtUtil.getJwtBuilder(subject, defaultTTL, getUUID());// 设置过期时间
        return builder.compact();
    }

    public  Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)//传入私钥
                .parseClaimsJws(jwt)//传入JWT
                .getBody();
    }


}
