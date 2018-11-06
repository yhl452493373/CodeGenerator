package ${cfg.packageShiro};

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm,在这里面比对用户信息(账号,密码等)
 *
 * @author 杨黄林
 */
public class ShiroRealm extends AuthorizingRealm {

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //权限获取

        Set<String> permisssionSets = new HashSet<>();
        System.out.println("数据库查询权限");
        permisssionSets.add("index:view");

        info.setStringPermissions(permisssionSets);

        //角色获取
        Set<String> rolenames = new HashSet<>();
        System.out.println("数据库查询角色");
        rolenames.add("admin");

        info.addRoles(rolenames);

        return info;
    }

    /**
    * 权限认证
    */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //TODO 这里仅用于测试,实际使用根据token中的用户信息与数据库的进行比较,不一致则跑出相关异常.在登录接口中捕获这些异常进行返回提示
        String username = "admin";
        String password = "admin";

        System.out.println("用户信息认证");

        //TODO 认证后,username请换成对应的实体.便于通过shiro标签获取用户对象
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, getName());

        return info;
    }
}