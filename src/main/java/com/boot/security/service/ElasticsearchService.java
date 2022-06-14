package com.boot.security.service;

import com.boot.security.domain.Book;

import java.io.IOException;
import java.util.List;

public interface ElasticsearchService {
    public List<Book> listBook(String keyword);
    public void deleteBookOfES(Integer bId) throws IOException;
    public void updateBookOfES(Book book) throws Exception;
    public void addBookOfES(Book book) throws IOException;
}
