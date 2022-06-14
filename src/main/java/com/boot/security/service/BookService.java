package com.boot.security.service;

import com.boot.security.domain.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
* @author creed
* @description 针对表【book】的数据库操作Service
* @createDate 2022-05-26 12:52:01
*/
public interface BookService extends IService<Book> {
    public int addBook(MultipartFile uploadfile, Book book) throws IOException;
    public int updateBookById(MultipartFile uploadfile, Book book) throws Exception;
    public void deleteBookById(Book book);
}
