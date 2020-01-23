package cn.pzhu.forum.realm;

import cn.pzhu.forum.content.Identify;
import cn.pzhu.forum.dao.UserDao;
import cn.pzhu.forum.entity.User;
import cn.pzhu.forum.util.ByteSourceUtils;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Shiro Realm,用于用户登录以及权限控制
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {


    /**
     * 用户数据库访问层
     */
    @Autowired
    private UserDao userDao;

    /**
     * 权限认证
     *
     * @param principals 用户身份
     * @return 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        log.info("权限认证");

        Object username = principals.getPrimaryPrincipal();

        User user = userDao.get(username.toString());

        Set<String> roles = new HashSet<>();
        roles.add("user");

        if (user.getIdentify().equals(Identify.ADMIN.getIdentify())) {
            roles.add("admin");
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        info.addRoles(roles);

        return info;
    }

    /**
     * 用户登录
     *
     * @param token 用户登录信息
     * @return 用户登录认证信息
     * @throws AuthenticationException 登录异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        log.info("用户登录");

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        String username = usernamePasswordToken.getUsername();

        User user = userDao.get(username);

        if (user == null || user.getStatus().equals(Identify.DEAD.getIdentify())) {
            throw new UnknownAccountException();
        }

        ByteSource bytes = new ByteSourceUtils(user.getUserId());

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUserId(),
            user.getPassword(), bytes, getName());

        log.info("认证通过");

        return info;
    }

    @Override
    public String getName() {
        return "ShiroRealm";
    }
}
