package cn.dlj1.eurekaclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BookController {

    static List<Map<String, String>> list = new ArrayList<>();

    @GetMapping("list")
    public List<Map<String, String>> list() {
        return list;
    }

    @GetMapping("item/{id}")
    public Map<String, String> item(@PathVariable String id) {
        return list.stream().filter(stringStringMap -> stringStringMap.get("id").equals(id)).collect(Collectors.toList()).get(0);
    }

    static {
        Map<String, String> map;

        map = new HashMap<>();
        map.put("id", "1");
        map.put("name", "JAVA");
        list.add(map);

        map = new HashMap<>();
        map.put("id", "2");
        map.put("name", "C语言");
        list.add(map);

        map = new HashMap<>();
        map.put("id", "3");
        map.put("name", "C++");
        list.add(map);

        map = new HashMap<>();
        map.put("id", "4");
        map.put("name", "Python");
        list.add(map);
    }

}
