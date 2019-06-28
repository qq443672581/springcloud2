package cn.dlj1.simpleboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fivewords
 * @date 2019/5/30 14:01
 */
@SpringBootApplication
@RestController
@RequestMapping("book")
public class SimpleBootStart {

    public static void main(String[] args) {
        SpringApplication.run(SimpleBootStart.class, args);
    }

    @GetMapping("")
    public String hello() {
        return "hello";
    }

    @GetMapping("list")
    public Object list() {
        Map<String, String> map = new HashMap<>();
        map.put("JAVA", "1");
        return map;
    }

}
