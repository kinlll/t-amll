package com.kinl.tmall.Realm;

import com.kinl.tmall.configuration.CustomizedToken;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.SysAdmin;
import com.kinl.tmall.service.SysAdminService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AdminWebShiroRealm extends AuthorizingRealm {

    @Autowired
    private SysAdminService sysAdminService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String adminName = principalCollection.getPrimaryPrincipal().toString();
        System.out.println("授权的用户名----------"+ adminName);
        //根据用户名去数据库查询相关用户的权限    权限信息如user:create
        SysAdmin sysAdmin = sysAdminService.findByName(adminName);
        if (sysAdmin == null) {
            return null;
        }
        List<String> permissions = sysAdminService.findPermissionById(sysAdmin.getId());

        //添加用户权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("AdminWebShiroRealm.doGetAuthenticationInfo()");
        CustomizedToken customizedToken = (CustomizedToken) token;
        //获取用户输入的账号
        String username = (String) customizedToken.getPrincipal();
//        String userpwd = new String((char[]) customizedToken.getCredentials());
        SysAdmin admin = sysAdminService.findByName(username);
        if (admin == null) {
            throw new UnknownAccountException();
        }
        ByteSource salt = ByteSource.Util.bytes(admin.getSalt());
            //将用户存放到登录认证info中，无需自己做密码对比Shiro会为我们进行密码对比校验
        /*String password = "123";  //假设的从数据库中获取的密码
        if (!userpwd.equals(password)){
            throw new AllException(ResultEnum.USERPWD_ERROR); //密码不正确
        }*/
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                admin.getName(), //用户名
                admin.getPassword(), //密码
                salt, //盐值
                getName()); //当前realm name

        return simpleAuthenticationInfo;
    }


   /* public static void main(String[] args){
        SimpleHash simpleHash = new SimpleHash("MD5", "123", ByteSource.Util.bytes("kinl"), 1);
        String s = simpleHash.toHex();
        System.out.println(s);
    }*/

}
