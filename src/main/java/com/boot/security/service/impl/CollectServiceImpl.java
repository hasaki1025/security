package com.boot.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.security.domain.Book;
import com.boot.security.domain.Collect;
import com.boot.security.domain.User;
import com.boot.security.service.CollectService;
import com.boot.security.mapper.CollectMapper;
import com.boot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author creed
* @description 针对表【collect】的数据库操作Service实现
* @createDate 2022-05-26 12:52:10
*/
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect>
    implements CollectService{

    @Autowired
    CollectMapper mapper;


    @Autowired
    CollectService collectService;


    @Override
    public List<Book> getAllCollection(User user) {

        return mapper.selectAllBookOfUser(user);
    }

    @Override
    public int addBookInCollection(Integer bId,Long uId) {
        Book book = mapper.selctOneBook(uId, bId);
        if(book==null)
        {
           return mapper.insertOneBook(uId,bId);
        }
        else
        {
            return mapper.updateCIsBeenDelete(uId,bId);
        }
    }

    @Override
    public Integer deleteBatchBookInCollection(List<Book> list, User user) {
        Long uId = user.getUId();
        int count=0;
        for (Book book : list) {
            Integer bId = book.getBId();
            QueryWrapper<Collect> eq = new QueryWrapper<Collect>().eq("b_id", bId).eq("u_id", uId);
            count+=collectService.remove(eq)? 1:0;
        }
        return count;
    }


}




