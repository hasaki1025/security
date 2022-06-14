package com.boot.security.Controller;

import com.boot.security.domain.Book;
import com.boot.security.service.BookService;
import com.boot.security.service.ElasticsearchService;
import com.boot.security.service.Util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    ElasticsearchService elasticsearchService;



    @PostMapping("/download")
    ResponseEntity<Resource> download( @RequestBody Book book, HttpServletRequest request) throws UnsupportedEncodingException {
        File file = new File(book.getBUri());
        String filename= URLEncoder.encode(file.getName().replaceAll(" ",""),"utf-8");
        if(file.exists())
        {
            FileSystemResource resource = new FileSystemResource(file);
            String mimeType = request.getServletContext().getMimeType(file.getPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            System.out.println(file.getName());
            System.out.println(mimeType);
            return ResponseEntity.ok().
                    header(HttpHeaders.CONTENT_TYPE,mimeType+";charset=utf-8").
                    header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+filename).
                    body(resource);
        }
        return new ResponseUtil<Resource>().isBad().createResponseEntity("download defeat");
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('Admin')")
    ResponseEntity<String> upload(MultipartFile uploadfile, @Valid Book book)
    {

        ResponseUtil<String> response = new ResponseUtil<>();
        try {
            bookService.addBook(uploadfile,book);
            return response.ok().createResponseEntity("create successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return response.isBad().createResponseEntity("create book defeat");
        }
    }

    @GetMapping
    ResponseEntity<List<Book>> searchBook( String keyword) {
        ResponseUtil<List<Book>> response = new ResponseUtil<>();
        try{
            List<Book> books = elasticsearchService.listBook(keyword);
            return response.ok().createResponseEntity("查找成功",books);
        }catch(Exception e){
            e.printStackTrace();
            return response.isBad().createResponseEntity("查找失败");
        }
    }


    @PutMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    ResponseEntity<String> updateBook(MultipartFile uploadfile, @Valid Book book)
    {
        ResponseUtil<String> response = new ResponseUtil<>();
        try {
            if(bookService.updateBookById(uploadfile,book)!=1)
            {
                throw  new RuntimeException("更新失败");
            }
            return response.ok().createResponseEntity("更新成功");
        }catch(Exception e){
            e.printStackTrace();
            return response.isBad().createResponseEntity("更新失败");
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    ResponseEntity<String> deleteBook(Integer bId)
    {
        ResponseUtil<String> response = new ResponseUtil<>();
        try {
            if (!bookService.removeById(bId)) {
                throw new RuntimeException("删除失败");
            }
            elasticsearchService.deleteBookOfES(bId);
            return response.ok().createResponseEntity("删除成功");
        }catch(Exception e){
            e.printStackTrace();
            return response.isBad().createResponseEntity("删除失败");
        }
    }
}
