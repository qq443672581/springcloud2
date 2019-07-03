package cn.dlj1.auth;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 用户信息
 */
public interface UserDetail extends Serializable {

    /**
     * 获取用户唯一标识
     *
     * @return
     */
    String getUsername();

    /**
     * 获取用户权限代码
     *
     * @return
     */
    Set<String> getAuthCodes();

}
