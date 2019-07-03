package cn.dlj1.gateway.auth;

import cn.dlj1.auth.UserDetail;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fivewords
 * @date 2019/7/3 15:31
 */
@Entity
public class User implements UserDetail {

    public static transient User EMPTY = new User();
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
    private Set<String> authCodes;

    public User() {

    }

    public User(String username, String password, Set<String> authCodes) {
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

    public void setAuthCodes(Set<String> authCodes) {
        this.authCodes = authCodes;
    }

    public Set<String> addAuthCode(String code) {
        if (null == this.authCodes) {
            this.authCodes = new HashSet<>();
        }
        this.authCodes.add(code);
        return this.authCodes;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Set<String> getAuthCodes() {
        return authCodes;
    }
}
