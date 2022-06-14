package com.boot.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.security.domain.BookException.NotBookCanFind;
import com.boot.security.domain.BookException.NotUserCanFindException;
import com.boot.security.domain.LoginUser;
import com.boot.security.domain.User;
import com.boot.security.mapper.UserMapper;
import com.boot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
* @author creed
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-05-26 12:52:16
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.getOne(new QueryWrapper<User>().eq("u_name",s));
        if(user == null)
        {
            throw new NotUserCanFindException();
        }
        //添加权限信息
        List<String> authority = new ArrayList<>(Collections.singletonList("test"));
        if(user.getAdmin()==1)
        {
            authority.add("Admin");
        }
        return new LoginUser(user,authority);
    }
}




