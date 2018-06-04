package com.tu.modules.login.shiro;

import com.tu.common.bean.User;
import com.tu.modules.login.dao.UserDao;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class CommunityRealm extends AuthorizingRealm {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (this.userDao == null) {
            return null;
        }
        String usercode = (String) token.getPrincipal();
        User user = userDao.findUserByUsercode(usercode);
        if (user == null) {
            return null;
        }

//        String password = new String(usernamePasswordToken.getPassword());
//        System.out.println("username-------------------->"+username);
//        System.out.println("username-------------------->"+username);
//        System.out.println("username-------------------->"+username);

        SimpleAuthenticationInfo simpleAuthenticationInfo
                = new SimpleAuthenticationInfo(user,user.getPassword(),"communityRealm");
        return simpleAuthenticationInfo;
    }

}
