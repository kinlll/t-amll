package com.kinl.tmall.Realm;

import com.kinl.tmall.configuration.CustomizedToken;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class UserWebShiroRealm extends AuthorizingRealm {
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /// TODO: 2020/4/3
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("UserWebShiroRealm.doGetAuthenticationInfo()");
        CustomizedToken customizedToken = (CustomizedToken) token;
        //获取用户输入的账号
        String username = (String) customizedToken.getPrincipal();
        String userpwd = new String((char[]) customizedToken.getCredentials());
        
        /// TODO: 2020/4/4

        String password = "123";  //假设的从数据库中获取的密码
        if (!userpwd.equals(password)){
            throw new AllException(ResultEnum.USERPWD_ERROR); //密码不正确
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                username, password, getName());
        
        return simpleAuthenticationInfo;
    }
}
