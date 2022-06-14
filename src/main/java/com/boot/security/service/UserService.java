package com.boot.security.service;

import com.boot.security.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author creed
* @description 针对表【user】的数据库操作Service
* @createDate 2022-05-26 14:33:34
*/
public interface UserService extends IService<User> {
    public int addUser(User user);


    public int updatePassword(User user,String newpassword);
}
