package cn.dlj1.gateway.auth;

import cn.dlj1.auth.UserDetail;
import cn.dlj1.auth.UserService;
import cn.dlj1.gateway.auth.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

/**
 * @author fivewords
 * @date 2019/7/3 15:30
 */
@Service
public class MyUserService implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired(required = false)
//    @Qualifier("jdbcScheduler")
    private Scheduler jdbcScheduler;

    @Override
    public Mono<UserDetail> findUser(String username, String password) {
        return Mono
                .defer(() -> Mono.justOrEmpty(this.userRepository.findByUsernameAndPassword(username, password)))
                .subscribeOn(jdbcScheduler)
                .flatMap(u -> {
                    u.addAuthCode("book");
                    return Mono.just(u);
                });
    }

}
