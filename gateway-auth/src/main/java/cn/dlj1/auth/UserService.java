package cn.dlj1.auth;

import reactor.core.publisher.Mono;

/**
 * 用户操作
 */
public interface UserService {

    Mono<UserDetail> findUser(String username, String password);

}
