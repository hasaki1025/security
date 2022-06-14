package com.boot.security;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.boot.security.domain.Book;
import com.boot.security.domain.Collect;
import com.boot.security.domain.User;
import com.boot.security.mapper.BookMapper;
import com.boot.security.mapper.CollectMapper;
import com.boot.security.mapper.UserMapper;
import com.boot.security.service.CollectService;
import com.boot.security.service.ElasticsearchService;
import com.boot.security.service.UserService;
import com.boot.security.service.Util.JWTUtil;
import com.boot.security.service.Util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SecurityApplicationTests {

    @Autowired
    PasswordEncoder password;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CollectMapper collectMapper;

    @Autowired
    ResponseUtil<String> responseUtil;

    @CreateCache(area="TokenCache",name="TokenCache",expire = 2,timeUnit = TimeUnit.HOURS)//expire（过期时间）默认单位为秒
    private Cache<String,String> tokenCache;

    @Test
    void contextLoads() throws JsonProcessingException {
        String bookUri="D:\\devBookFile\\MyBatis.md";
        System.out.println(new ObjectMapper().writeValueAsString(bookUri));
    }

    @Test
    void testPasswordEncode()
    {
        String encode = password.encode("10001");//加密
        System.out.println(encode);
        System.out.println(password.matches("10001", encode));
    }

    @Test
    public void testJWT() throws Exception {
        String jwt="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlMGEzMWYwMmRjMGQ0Yzg5YTJkZTE5YmJlZTlmNGE5NSIsInN1YiI6IjciLCJpc3MiOiJ3dWh1IiwiaWF0IjoxNjUzNTcwOTkzLCJleHAiOjYwMDAxNjUzNTcwOTkzfQ.EMQdQKcW18Rt3AEM9QCSJKNcNu_zTgQYySzl14LIyYU";
        System.out.println(jwtUtil.parseJWT(jwt).getId());
    }

    @Test
    void testjetcache()
    {
        System.out.println(tokenCache.get("Token" + "e0a31f02dc0d4c89a2de19bbee9f4a95"));
    }


    @Test
    void testid()
    {

        System.out.println("1530113430155993090".length());
    }

    @Test
    void testResponse()
    {
        System.out.println(responseUtil.ok().createResponseEntity("123"));
    }

    @Autowired
    BookMapper bookMapper;



    @Autowired
    ElasticsearchService elasticsearchService;
    @Test
    void searchBook()
    {
        elasticsearchService.listBook("乡土中国").forEach(System.out::println);
    }

    @Test
    void addBook() throws IOException {
        Book book = new Book();
        book.setBId(1000);
        book.setBName("afafafasfasfa");
        book.setBAuthor("afajjkhafkjaf");
        elasticsearchService.addBookOfES(book);
    }

    //User(uId=7, uName=123, uEmail=1943863178@qq.com, uIsBeenDeleted=0, uVersion=1, uPassword=10001, admin=1)
    @Test
    void testuser()
    {
        User user = new User();
        user.setUId(7L);
        user.setUName("123");
        user.setUEmail("1943863178@qq.com");
        user.setUIsBeenDeleted(0);
        user.setUVersion(1);
        user.setUPassword("10001");
        user.setAdmin(1);
        System.out.println(userMapper.updateById(user));
    }

    @Test
    void getAllbooks()
    {
        User user = new User();
        user.setUId(2L);
        collectMapper.selectAllBookOfUser(user).forEach(System.out::println);
    }

    @Test
    void testCollect()
    {
        User user = new User();
        user.setUId(7L);
        collectMapper.selectAllBookOfUser(user).forEach(System.out::println);
    }

    @Autowired
    CollectService collectService;

    @Test
    void testsaveCollection()
    {
        System.out.println(collectService.addBookInCollection(1, 7L));
    }

}
