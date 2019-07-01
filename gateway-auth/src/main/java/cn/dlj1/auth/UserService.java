package cn.dlj1.auth;

/**
 * 用户操作
 */
public interface UserService {

    User findUser(String id);

    interface User {
        String getUserId();
    }
}
