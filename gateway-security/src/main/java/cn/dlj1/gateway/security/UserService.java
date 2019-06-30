package cn.dlj1.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * @author fivewords
 * @date 2019/6/26 14:17
 */
@Configuration
public class UserService implements ReactiveUserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        logger.info("用户的用户名: {}", username);

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("book_item");
        authorities.add(new SimpleGrantedAuthority("debug"));

        String userId = UUID.randomUUID().toString();

        User user = new User(userId, username, passwordEncoder.encode("123"), authorities);
        return Mono.just(user);
    }

}