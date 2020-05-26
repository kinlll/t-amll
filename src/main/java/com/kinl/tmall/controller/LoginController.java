package com.kinl.tmall.controller;

import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.VO.UserVO;
import com.kinl.tmall.configuration.CustomizedToken;
import com.kinl.tmall.enums.LoginType;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private static final String USER_LOGIN_TYPE = LoginType.USER.getType();
    private static final String ADMIN_LOGIN_TYPE = LoginType.ADMIN.getType();

    @GetMapping("/login")
    public String toLogin(HttpServletRequest request){
        return "fore/login";
    }

    @GetMapping("/res")
    public String dsa(){
        return null;
    }


    //登陆
    @PostMapping("/login")
    @ResponseBody
    public ResultVO login(@RequestBody UserVO user, HttpSession session){
        if ("0".equals(user.getLoginType())){
            user.setLoginType(USER_LOGIN_TYPE);
        } else if ("1".equals(user.getLoginType())){
            user.setLoginType(ADMIN_LOGIN_TYPE);
        } else {
            throw new AllException(ResultEnum.LOGINTYPE_ERROR);
        }

        //从SecurityUtils里创建一个subject
        Subject subject = SecurityUtils.getSubject();
        //在认证提交之前准备token（令牌）
        CustomizedToken token = new CustomizedToken(user.getName(), user.getPassword(), user.getLoginType());
        //执行认证登陆
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            return ResultVOUtil.error(ResultEnum.UNKNOWNACCOUNT);   // "未知账户";
        } catch (IncorrectCredentialsException ice) {
            return ResultVOUtil.error(ResultEnum.INCORRECTCREDENTIALS);   //"密码不正确";
        } catch (LockedAccountException lae) {
            return ResultVOUtil.error(ResultEnum.LOCKEDACCOUNT);   //"账户已锁定";
        } catch (ExcessiveAttemptsException eae) {
            return ResultVOUtil.error(ResultEnum.EXCESSIVEATTEMPTS);   //"用户名或密码错误次数过多";
        } catch (AuthenticationException ae) {
            return  ResultVOUtil.error(ResultEnum.AUTHENTICATION);   //"用户名或密码不正确";
        }
        if (subject.isAuthenticated()) {
            String loginType = user.getLoginType();
            session.setAttribute("user", user);
            return ResultVOUtil.success(loginType);
        } else {
            token.clear();
            return ResultVOUtil.error(1,"登陆失败"); //"登陆失败";
        }

    }

    //登出使用shiro实现

}
