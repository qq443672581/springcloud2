package cn.dlj1.auth;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 权限处理
 */
public interface AuthCheckService {

    /**
     * 检查 <br>
     * 会返回三种状态 <br>
     * -1,0,1 <br>
     * -1:  没有登录
     *  0:  没有权限
     *  1:  拥有权限
     *
     * @param exchange
     * @return
     */
    Mono<Integer> check(ServerWebExchange exchange);

}
