package cn.dlj1.security.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fivewords
 * @date 2019/6/27 11:50
 */
@RestController
@RequestMapping("/book")
public class BookController {

    Map<String, String> map = new HashMap<>();

    {
        map.put("1", "C");
        map.put("2", "C++");
        map.put("3", "JAVA");
    }

    @RequestMapping("hello")
    public Object hello(@RequestHeader(required = false, value = "Hello") String Hello) {
        System.out.println(Hello);
        return "hello";
    }

    @RequestMapping("")
    public Object index() {
        return "你好";
    }

    @RequestMapping("item")
    public Object items() {
        return map;
    }

    @RequestMapping("item/{id}")
    public Object item(@PathVariable String id) {
        return map.get(id);
    }

}
