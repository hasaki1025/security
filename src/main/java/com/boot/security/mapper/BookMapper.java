package com.boot.security.mapper;

import com.boot.security.domain.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author creed
* @description 针对表【book】的数据库操作Mapper
* @createDate 2022-05-26 12:52:01
* @Entity com.boot.security.domain.Book
*/
public interface BookMapper extends BaseMapper<Book> {
    public Book selectBookIsExists(Book book);
    public Book deleteByBIdBook(@Param("b_id") String b_id);
    public Book selectBookByISBN(@Param("ISBN") String ISBN);
    public Book deleteByISBN(@Param("ISBN") String ISBN);

}




