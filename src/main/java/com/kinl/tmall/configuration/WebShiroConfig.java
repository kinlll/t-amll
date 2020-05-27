package com.kinl.tmall.configuration;

import com.kinl.tmall.Realm.AdminWebShiroRealm;
import com.kinl.tmall.Realm.UserWebShiroRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class WebShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        System.out.println("ShiroConfiguration.shiroFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //拦截器
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置不会被拦截的链接 顺序判断
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");

        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/forelogout","logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //filterChainDefinitionMap.put("/**", "authc");        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/login","anon");
        filterChainDefinitionMap.put("/home","anon");


        filterChainDefinitionMap.put("/**", "authc");

        //设置登陆的url地址， 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;   没有权限默认跳转的页面，登录的用户访问了没有被授权的资源自动跳转到的页面。
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/401");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    //设置加密算法
    @Bean
    public CredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //散列次数
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }


    @Bean
    public UserWebShiroRealm userWebShiroRealm(){
        UserWebShiroRealm userWebShiroRealm = new UserWebShiroRealm();
        return userWebShiroRealm;
    }

    @Bean
    public AdminWebShiroRealm adminWebShiroRealm(){
        AdminWebShiroRealm adminWebShiroRealm = new AdminWebShiroRealm();
        adminWebShiroRealm.setCredentialsMatcher(credentialsMatcher());
        return adminWebShiroRealm;
    }

    @Bean
    public CustomizedModularRealmAuthenticator customizedModularRealmAuthenticator(){
        CustomizedModularRealmAuthenticator authenticator = new CustomizedModularRealmAuthenticator();
        AtLeastOneSuccessfulStrategy atLeastOneSuccessfulStrategy = new AtLeastOneSuccessfulStrategy();
        authenticator.setAuthenticationStrategy(atLeastOneSuccessfulStrategy);
        return authenticator;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        Collection<Realm> realms = new ArrayList<>();
        realms.add(userWebShiroRealm());
        realms.add(adminWebShiroRealm());
        securityManager.setAuthenticator(customizedModularRealmAuthenticator());
        securityManager.setRealms(realms);
        System.out.println("securityManager");
        return securityManager;
    }

    //bean的实例化
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    //aop
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    //开启注解
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

}
