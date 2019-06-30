package cn.dlj1.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * 网络配置
 *
 * @author fivewords
 * @date 2019/6/27 11:48
 */
@Configuration
public class HttpConfig implements CommandLineRunner {


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

    @Autowired
    ApplicationContext context;

    @Override
    public void run(String... args) {
        Map<String, RedisSerializationContext> beansOfType = context.getBeansOfType(RedisSerializationContext.class);
        System.out.println();
    }

    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver() {
            @Override
            public void setSessionId(ServerWebExchange exchange, String id) {
                Mono<WebSession> mono = exchange.getSession();
                Map<String, Object> map = mono.block().getAttributes();
                if (null != map && null != map.get("SPRING_SECURITY_CONTEXT")) {
                    SecurityContext securityContext = (SecurityContext) map.get("SPRING_SECURITY_CONTEXT");
                    Authentication authentication = securityContext.getAuthentication();
                    // 用户
                    User principal = (User) authentication.getPrincipal();
                }

                super.setSessionId(exchange, id);
            }
        };
        resolver.setCookieName("GATEWAY-SESSION");
        resolver.setCookieMaxAge(Duration.ofHours(24));
        return resolver;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.cors().disable();
        http.csrf().disable();

        http.formLogin();
        ServerHttpSecurity.AuthorizeExchangeSpec exchange = http.authorizeExchange();

        exchange.pathMatchers("/**/favicon.ico").permitAll();

        // 具体权限
        exchange.pathMatchers("/debug").hasAuthority("debug");
        exchange.pathMatchers("/book/item/*").hasAuthority("book_item");
        // 所有人可以访问
        exchange.pathMatchers("/book/list").permitAll();
        // 其他接口不可访问
        exchange.anyExchange().denyAll();

        return http.build();
    }

}