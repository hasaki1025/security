package com.boot.security.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.boot.security.domain.LoginUser;
import com.boot.security.domain.User;
import com.boot.security.service.LoginService;
import com.boot.security.service.Util.JWTUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JWTUtil jwtUtil;

    @CreateCache(area="TokenCache",name="TokenCache",expire = 2,timeUnit = TimeUnit.HOURS)//expire（过期时间）默认单位为秒
    private Cache<String,String> tokenCache;

    @Value("${JWT.defaultTTL}")
    private Long defaultTTL;

    @Override
    public ResponseEntity<String> login(User user, Long TTL) throws Exception {
        //传入认证对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUName(), user.getUPassword());
        //开始认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证成功
        if(authenticate==null)
        {
            throw new RuntimeException("Authentication failed");
        }
        //获取用户对象,这里authenticate的Principal是我们在UserDetailsService中返回的对象
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //随机得到一个JWTID
        String TokenId=jwtUtil.getUUID();
        //在loginUser中放置jwtId
        loginUser.setTokenId(TokenId);
        if(TTL==null)
        {
            TTL=defaultTTL;
        }
        //创建JWT
        String jwt = jwtUtil.createJWT(String.valueOf(loginUser.getUser().getUId()),TTL,TokenId);
        //将Token放置在缓存中
        tokenCache.put("Token"+loginUser.getUser().getUId(),new ObjectMapper().writeValueAsString(loginUser));
        //返回响应，其中包含jwt
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Message","Login successful");

        return new ResponseEntity<>(jwt,httpHeaders,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> logout() {

        try {
            //获取到SecurityContextHolder中的LoginUser信息
            LoginUser user = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //删除Redis中的User信息
            Long uId=user.getUser().getUId();
            tokenCache.remove("Token"+uId);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Logout failed");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("messsage","Logout successful");
        return new ResponseEntity<>(null,headers,HttpStatus.OK);
    }
}
