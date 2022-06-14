package com.boot.security.service;

import com.boot.security.domain.Book;
import com.boot.security.domain.Collect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.security.domain.User;

import java.util.List;

/**
* @author creed
* @description 针对表【collect】的数据库操作Service
* @createDate 2022-05-26 12:52:10
*/
public interface CollectService extends IService<Collect> {

    public List<Book> getAllCollection(User user);

    int addBookInCollection(Integer bId,Long uId);

    Integer deleteBatchBookInCollection(List<Book> list,User user);
}
