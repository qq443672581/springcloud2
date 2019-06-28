package cn.dlj1.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 网络配置
 *
 * @author fivewords
 * @date 2019/6/27 11:48
 */
@Configuration
public class HttpConfig {

    @Bean
    public PasswordEncoder md5PasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return encode(charSequence).equals(s);
            }
        };
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.formLogin();
        ServerHttpSecurity.AuthorizeExchangeSpec exchange = http.authorizeExchange();

        // 具体权限
        exchange.pathMatchers("/store/book/list").hasRole("book");
        // 所有人可以访问
        exchange.pathMatchers("/store/book").permitAll();
        // 其他接口不可访问
        exchange.anyExchange().denyAll();

        return http.build();
    }

}