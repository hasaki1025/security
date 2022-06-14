package com.boot.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.security.domain.Book;
import com.boot.security.service.BookService;
import com.boot.security.mapper.BookMapper;
import com.boot.security.service.ElasticsearchService;
import com.boot.security.service.Util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
* @author creed
* @description 针对表【book】的数据库操作Service实现
* @createDate 2022-05-26 12:52:01
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

    @Autowired
    ElasticsearchService elasticsearchService;

    @Value("${bookfile.localPath}")
    String fileprefix;

    @Value("${bookfile.deletedPath}")
    String deletedPath;

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper mapper;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int addBook(MultipartFile uploadfile,Book book) throws IOException {
        String FilebookName=uploadfile.getOriginalFilename();
        String bUri=fileprefix + FilebookName;
        book.setBUri(bUri);
        Book isExists = mapper.selectBookIsExists(book);
        if(isExists!=null)
        {
            if (isExists.getBIsBeenDeleted()==1) {
                mapper.deleteById(isExists.getBId());
            }
            else
            {
                throw new RuntimeException("图书已存在");
            }
        }



        if (!bookService.save(book)) {
            throw new RuntimeException("数据库添加失败");
        }
        elasticsearchService.addBookOfES(book);
        File file = new File(bUri);
        FileOutputStream os = new FileOutputStream(file);
        os.write(uploadfile.getBytes());
        return 1;
    }

    @Override
    public int updateBookById(MultipartFile uploadfile,Book book) throws Exception {
        if(uploadfile==null)
        {
            if(!bookService.updateById(book))
            {
                throw new RuntimeException("数据库更新失败");
            }
            elasticsearchService.updateBookOfES(book);
            return 1;
        }
        else
        {
            String FilebookName=uploadfile.getOriginalFilename();
            File oldFile = new File(book.getBUri());
            String bUri=fileprefix + FilebookName;
            book.setBUri(bUri);
            if (!bookService.updateById(book)) {
                throw new RuntimeException("数据库更新失败");
            }
            elasticsearchService.updateBookOfES(book);
            oldFile.deleteOnExit();
            File file = new File(bUri);
            FileOutputStream os = new FileOutputStream(file);
            os.write(uploadfile.getBytes());
            return 1;
        }

    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void deleteBookById(Book book) {


        if (bookService.removeById(book.getBId())) {
            throw new RuntimeException("database delete defeat");
        }
        String oldPath = book.getBUri();
        File file = new File(oldPath);
        String newPath = deletedPath+file.getName();
        if (FileUtil.copyFile(oldPath,newPath) && file.delete()) {
            throw new RuntimeException("delete book defeat");
        }

    }


}




