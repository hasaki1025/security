package com.boot.security.mapper;

import com.boot.security.domain.Book;
import com.boot.security.domain.Collect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.security.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author creed
* @description 针对表【collect】的数据库操作Mapper
* @createDate 2022-05-26 12:52:10
* @Entity com.boot.security.domain.Collect
*/
public interface CollectMapper extends BaseMapper<Collect> {
    public List<Book> selectAllBookOfUser(User user);
    public int insertOneBook(@Param("u_id") Long u_id,@Param("b_id") Integer b_id);
    public Book selctOneBook(@Param("uId") Long uId,@Param("bId") Integer bId);
    public int updateCIsBeenDelete(@Param("uId") Long uId,@Param("bId") Integer bId);
}




