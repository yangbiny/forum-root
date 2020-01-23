package cn.pzhu.forum.realm;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author impassivey
 */
public class EasyTypeToken extends UsernamePasswordToken {

    private static final long serialVersionUID = -2564928913725078138L;

    @Getter
    @Setter
    private LoginType type;


    public EasyTypeToken() {
        super();
    }


    public EasyTypeToken(String username, String password, LoginType type, boolean rememberMe,
        String host) {
        super(username, password, rememberMe, host);
        this.type = type;
    }

    /**
     * 免密登录
     *
     * @param username 用户名
     */
    public EasyTypeToken(String username) {
        super(username, "", false, null);
        this.type = LoginType.NOPASSWD;
    }

    /**
     * 账号密码登录
     *
     * @param username 用户名
     * @param password 密码
     */
    public EasyTypeToken(String username, String password) {
        super(username, password, false, null);
        this.type = LoginType.PASSWORD;
    }
}

