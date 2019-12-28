package cn.dlj1.lock.jedis.config;

import com.github.jedis.lock.JedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;

@Aspect
public class Aop {

    @Autowired
    private JedisConnectionFactory factory;

    @Pointcut("@annotation(Lock)")
    public void pointcut() {}

    @Around(value = "@annotation(lock)")
    public Object printParam(ProceedingJoinPoint point, Lock lock){
        Jedis jedis = new Jedis(factory.getHostName());

        JedisLock jedisLock = new JedisLock(jedis, lock.value(),10000,10000);

        try {
            jedisLock.acquire();
            return point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            jedisLock.release();
        }

    }

}
