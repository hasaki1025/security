package com.boot.security.Controller;


import com.boot.security.domain.Book;

import com.boot.security.domain.User;
import com.boot.security.service.CollectService;
import com.boot.security.service.Util.GetNowUser;
import com.boot.security.service.Util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/collect")
@PreAuthorize("hasAnyAuthority('Admin','test')")
public class CollectController {

    @Autowired
    CollectService collectService;

    @Autowired
    GetNowUser getNowUser;

    @GetMapping
    ResponseEntity<List<Book>> getUserCollection()
    {
        User user = getNowUser.getNowUser().getUser();
        return new ResponseUtil<List<Book>>().ok().createResponseEntity("get collect successfully",collectService.getAllCollection(user));
    }

    @PostMapping
    ResponseEntity<Integer> addBookInUserCollection(@RequestBody Book book)
    {
        try{
            User user = getNowUser.getNowUser().getUser();
            if(collectService.addBookInCollection(book.getBId(),user.getUId())!=1)
            {
                throw new RuntimeException("add collect defeat");
            }
            return new ResponseUtil<Integer>().ok().createResponseEntity("add collect successfully");
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseUtil<Integer>().isBad().createResponseEntity("add collect defeat");
        }
    }

    @DeleteMapping
    ResponseEntity<Integer> deleteBookInUserCollection(@RequestBody List<Book> list)
    {
        try {
            User user = getNowUser.getNowUser().getUser();
            Integer rb = collectService.deleteBatchBookInCollection(list, user);
            return new ResponseUtil<Integer>().ok().createResponseEntity("remove "+rb+" book successfully");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseUtil<Integer>().isBad().createResponseEntity("remove defeat");
        }

    }
}
