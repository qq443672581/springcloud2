package cn.dlj1.gateway.auth.dao;

import cn.dlj1.gateway.auth.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author fivewords
 * @date 2019/7/3 17:01
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByUsernameAndPassword(String username, String password);
}
