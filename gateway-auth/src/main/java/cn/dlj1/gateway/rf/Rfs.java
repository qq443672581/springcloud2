package cn.dlj1.gateway.rf;

import cn.dlj1.auth.UserService;
import cn.dlj1.auth.session.DefaultWebSession;
import cn.dlj1.gateway.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 断言
 */
@Configurable
@Controller
public class Rfs {

    @Autowired
    UserService userService;

    @GetMapping("/index.html")
    @ResponseBody
    public Mono<String> index() {
        return Mono.just("index");
    }

    @GetMapping("/login.html")
    public Mono<String> loginHtml() {
        return Mono.just("login");
    }

    @PostMapping("/login")
    @ResponseBody
    public Mono<Map> login(ServerWebExchange exchange) {
        Mono<WebSession> session = exchange.getSession();
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);

        return exchange.getFormData()
                .flatMap(mp ->
                        userService.findUser(
                                mp.getFirst("username"),
                                mp.getFirst("password")
                        )
                )
                .defaultIfEmpty(User.EMPTY)
                .flatMap(userDetail -> {
                    if (User.EMPTY == userDetail) {
                        map.put("success", false);
                        map.put("message", "用户为空!");
                        return Mono.just(map);
                    }

                    return session.flatMap(webSession -> {
                        User user = ((User) userDetail);
                        user.addAuthCode("book");
                        ((DefaultWebSession) webSession).setUser(user);
                        return Mono.just(map);
                    });
                });
    }

    @GetMapping("/no_access.html")
    public Mono<String> noAccess() {
        return Mono.just("no_access");
    }

}
