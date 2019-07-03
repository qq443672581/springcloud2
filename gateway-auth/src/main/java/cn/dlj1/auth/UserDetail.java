package cn.dlj1.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface UserDetail extends Serializable {

    String getUsername();

    default List<AuthCode> getAuthCodes() {
        return new ArrayList<>(0);
    }

}
