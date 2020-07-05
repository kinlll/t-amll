package com.kinl.tmall.Realm;

import com.kinl.tmall.configuration.CustomizedToken;
import com.kinl.tmall.pojo.User;
import com.kinl.tmall.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserWebShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /// TODO: 2020/4/3
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("UserWebShiroRealm.doGetAuthenticationInfo()");
        CustomizedToken customizedToken = (CustomizedToken) token;
        String principal = (String) customizedToken.getPrincipal();
        User user = userService.findByName(principal);
        if (user == null) {
            throw new UnknownAccountException();
        }
        ByteSource bytesSalt = ByteSource.Util.bytes(user.getSalt());
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                user.getName(), user.getPassword(), bytesSalt, getName());
        
        return simpleAuthenticationInfo;
    }
}
