package com.boot.security.filter;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.boot.security.domain.BookException.illegalTokenExcepton;
import com.boot.security.domain.LoginUser;

import com.boot.security.service.Util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class LoginFileter extends OncePerRequestFilter {

    @Autowired
    JWTUtil jwtUtil;

    @CreateCache(area="TokenCache",name="TokenCache",expire = 2,timeUnit = TimeUnit.HOURS)//expire（过期时间）默认单位为秒
    private Cache<String,String> tokenCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取Token并判断
        String token = request.getHeader("Token");
        if(token==null || token.equals(""))
        {
            //没有token则向下执行之后该请求会被拦截并转至登录
            filterChain.doFilter(request, response);
            return;
        }
        //解析Token
        try {
            Claims claims = jwtUtil.parseJWT(token);
            String tokenId = claims.getId();
            String userId = claims.getSubject();
            String loginUserAsJson = tokenCache.get("Token" + userId);
            if(loginUserAsJson==null)
            {
                throw new RuntimeException("找不到该token");
            }
            //将jwt中的json转为loginUser
            LoginUser user= JSON.parseObject(loginUserAsJson, LoginUser.class);
            if(!tokenId.equals(user.getTokenId()))
            {
                throw new RuntimeException("TokenId不匹配");
            }
            // 传入第三个参数将该请求设置为已认证
            UsernamePasswordAuthenticationToken authentication=new
                    UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            //将User对象放置在SecurityContextHolder中
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //放行
            filterChain.doFilter(request, response);
        } catch (illegalTokenExcepton  e) {
            e.printStackTrace();
            throw new RuntimeException("非法Token");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
