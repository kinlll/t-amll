package com.kinl.tmall.controller.web;

import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.*;
import com.kinl.tmall.comparator.*;
import com.kinl.tmall.configuration.CustomizedToken;
import com.kinl.tmall.enums.LoginType;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.pojo.*;
import com.kinl.tmall.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.HttpServletSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/web")
public class ForeRestController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/forecategory/{cid}")
    public ResultVO forecategory(@PathVariable("cid") Integer cid, String sort){
        try {
            Category category = categoryService.findById(cid);
            if (category == null)
                return ResultVOUtil.error(1, "查询分类失败");
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            List<Product> products = productService.findbyCid(cid);
            categoryVO.setProducts(products);
            if (sort != null) {
                switch (sort){
                    case "all" :
                        Collections.sort(categoryVO.getProducts(), new ProductAllComparator());
                        break;
                    case "review" :
                        Collections.sort(categoryVO.getProducts(), new ProductReviewComparator());
                        break;
                    case "date" :
                        Collections.sort(categoryVO.getProducts(), new ProductDataComparator());
                        break;
                    case "saleCount" :
                        Collections.sort(categoryVO.getProducts(), new ProductSaleCountComparator());
                        break;
                    case "price" :
                        Collections.sort(categoryVO.getProducts(), new ProductPriceComparator());
                        break;
                }
            }
            return ResultVOUtil.success(categoryVO);
        } catch (BeansException e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    @GetMapping("/foreproduct/{pid}")
    public ResultVO foreproduct(@PathVariable("pid") Integer pid){
        try {
            ForeProductVO foreProductVO = new ForeProductVO();
            Product product = productService.findById(pid);
            if (product == null)
                return ResultVOUtil.error(1, "找不到该产品");

            List<PropertyValueVO> propertyValueVOS = propertyValueService.findVOByPid(product.getId());


            List<ReviewVO> reviewVOS = reviewService.findByPid(product.getId());

            foreProductVO.setProduct(product);
            foreProductVO.setPvs(propertyValueVOS);
            foreProductVO.setReviews(reviewVOS);

            return ResultVOUtil.success(foreProductVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    @GetMapping("/forecart")
    public ResultVO forecart(){
        try {
            String username = (String) SecurityUtils.getSubject().getPrincipal();
            User user = userService.findByName(username);
            List<OrderItemForeVO> orderItemForeVOS = orderItemService.findByUid(user.getId());

            return ResultVOUtil.success(orderItemForeVOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    @GetMapping("/forecheckLogin")
    public ResultVO forecheckLogin(){
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        if (authenticated) {
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(1, "用户未登陆");
        }
    }

    @GetMapping("/foreaddCart")
    public ResultVO foreaddCart(@RequestParam("pid") Integer pid, @RequestParam("num") Integer num){

        try {
            Product product = productService.findById(pid);
            if (product == null) {
                return ResultVOUtil.error(1, "没有这个商品");
            }
            if (product.getStock() < num) {
                return ResultVOUtil.error(1, "没有这么多商品库存");
            }
            String principal = (String) SecurityUtils.getSubject().getPrincipal();
            User user = userService.findByName(principal);

            Orderitem orderitem1 = orderItemService.findByUidAndPid(user.getId(), pid);
            if (orderitem1 != null) {
                orderItemService.updateNumByUidAndPid(user.getId(), pid, num);
                return ResultVOUtil.success();
            }

            Orderitem orderitem = new Orderitem();
            orderitem.setNumber(num);
            orderitem.setPid(product.getId());
            orderitem.setUid(user.getId());
            orderItemService.insertNoOid(orderitem);

            return ResultVOUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    @PostMapping("/forelogin")
    public ResultVO forelogin(@RequestBody UserVO user, HttpSession session){
        Subject subject = SecurityUtils.getSubject();
        if (user.getName().isEmpty() || user.getPassword().isEmpty()) {
            return ResultVOUtil.error(1, "账号密码不能为空");
        }
        CustomizedToken token = new CustomizedToken(user.getName(), user.getPassword(), LoginType.USER.getType());

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
            String loginType = LoginType.USER.getType();
            session.setAttribute("user", user);
            return ResultVOUtil.success(loginType);
        } else {
            token.clear();
            return ResultVOUtil.error(1,"登陆失败"); //"登陆失败";
        }
    }

}
