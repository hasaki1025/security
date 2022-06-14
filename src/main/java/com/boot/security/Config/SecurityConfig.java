package com.boot.security.Config;

import com.boot.security.filter.LoginFileter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)//开启权限设置
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    private LoginFileter loginFileter;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()

                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/*.eot",
                        "/*.svg",
                        "/*.ttf",
                        "/*.ico",
                        "/*.jpg",
                        "/*.woff",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.jpg",
                        "/**/*.eot",
                        "/**/*.svg",
                        "/**/*.ttf",
                        "/**/*.woff"



                ).permitAll()
                // anonymous方法对于获取token的rest api要允许匿名访问，也只能匿名访问
                .antMatchers(HttpMethod.POST,"/book/download")
                .permitAll()
                .antMatchers(HttpMethod.GET,"/book")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/user/login")
                .anonymous()
                .antMatchers(HttpMethod.POST,"/register")
                .anonymous()
                .antMatchers(HttpMethod.GET,"/register/sendmail")
                .permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 禁用缓存
        httpSecurity.headers().cacheControl();

        //将登录认证过滤器添加在登录过滤器前
        httpSecurity.addFilterBefore(loginFileter, UsernamePasswordAuthenticationFilter.class);
    }

}
