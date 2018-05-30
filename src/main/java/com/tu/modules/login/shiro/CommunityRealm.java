package com.tu.modules.login.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

@Component("communityRealm")
public class CommunityRealm extends AuthorizingRealm {
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
//        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = (String) token.getPrincipal();
//        String password = new String(usernamePasswordToken.getPassword());
        System.out.println("username-------------------->"+username);
        System.out.println("username-------------------->"+username);
        System.out.println("username-------------------->"+username);

        SimpleAuthenticationInfo simpleAuthenticationInfo
                = new SimpleAuthenticationInfo("111","222","communityRealm");
        return simpleAuthenticationInfo;
    }
}
