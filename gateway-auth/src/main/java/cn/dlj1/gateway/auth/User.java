package cn.dlj1.gateway.auth;

import cn.dlj1.auth.AuthCode;
import cn.dlj1.auth.UserDetail;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * @author fivewords
 * @date 2019/7/3 15:31
 */
@Entity
public class User implements UserDetail {

    @Id
    @GenericGenerator(name = "user-uuid", strategy = "uuid")
    @GeneratedValue(generator = "user-uuid")
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column
    private String username;
    @Column
    private String password;

    @Transient
    private List<AuthCode> authCodes;

    public User(String username, String password, List<AuthCode> authCodes) {
        this.username = username;
        this.password = password;
        this.authCodes = authCodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthCodes(List<AuthCode> authCodes) {
        this.authCodes = authCodes;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public List<AuthCode> getAuthCodes() {
        return authCodes;
    }
}
