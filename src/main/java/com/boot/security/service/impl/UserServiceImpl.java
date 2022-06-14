package com.boot.security.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.security.domain.User;
import com.boot.security.service.UserService;
import com.boot.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
* @author creed
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-05-26 14:33:34
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @CreateCache(area="TokenCache",name="TokenCache",expire = 2,timeUnit = TimeUnit.HOURS)//expire（过期时间）默认单位为秒
    private Cache<String,String> tokenCache;

    @Override
    public int addUser(User user) {
        User one = userService.getOne(new QueryWrapper<User>().
                eq("u_name", user.getUName()).
                eq("u_email", user.getUEmail()));
        if(one !=null)
        {
            throw new RuntimeException("user is exists");
        }
        user.setUPassword(passwordEncoder.encode(user.getUPassword()));
        return userService.save(user) ? 1:0;
    }

    @Override
    public int updatePassword(User user,String newpassword) {
        if(tokenCache.get("Token"+user.getUId())!=null)
        {
            tokenCache.remove("Token"+user.getUId());
            String pwd = passwordEncoder.encode(newpassword);
            user.setUPassword(pwd);
            return userService.updateById(user)? 1:0;
        }
        else
        {
            throw new RuntimeException("alert pwd defeat");
        }

    }


}




