package com.kinl.tmall.controller.web;

import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.*;
import com.kinl.tmall.comparator.*;
import com.kinl.tmall.configuration.CustomizedToken;
import com.kinl.tmall.enums.LoginType;
import com.kinl.tmall.enums.OrderStatusEnum;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.pojo.*;
import com.kinl.tmall.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.session.HttpServletSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    private OrderService orderService;

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

    //删除购物车商品
    @GetMapping("/foredeleteOrderItem")
    public ResultVO foredeleteOrderItem(@RequestParam("oiid") Integer oiid){
        try {
            Orderitem orderitem = orderItemService.findById(oiid);
            if (orderitem == null) {
                return ResultVOUtil.error(1,"购物车没有改订单项");
            }
            if (orderitem.getOid() != null) {
                return ResultVOUtil.error(1, "购物车内不可删除已购买订单");
            }
            orderItemService.deleteById(oiid);
            return ResultVOUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //获取产品
    @GetMapping("/foreproduct/{pid}")
    public ResultVO foreproduct(@PathVariable("pid") Integer pid){
        try {
            ForeProductVO foreProductVO = new ForeProductVO();
            Product product = productService.findById(pid);
            if (product == null)
                return ResultVOUtil.error(1, "找不到该产品");

            //属性
            List<PropertyValueVO> propertyValueVOS = propertyValueService.findVOByPid(product.getId());

            //评论
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

    //购买单个商品
    @GetMapping("/forebuyone")
    public ResultVO forebuyone(@RequestParam("pid") Integer pid, @RequestParam("num") Integer num){
        try {
            Product product = productService.findById(pid);
            if (product == null) {
                return ResultVOUtil.error(1, "没有该产品");
            }
            if (product.getStock() < num) {
                return ResultVOUtil.error(1, "没有这么多库存");
            }
            String username = (String) SecurityUtils.getSubject().getPrincipal();
            User user = userService.findByName(username);
            orderItemCreat(pid, num);
            Orderitem orderitem = orderItemService.findByUidAndPid(user.getId(), pid);
            return ResultVOUtil.success(orderitem.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //加载确认收货页面
    @GetMapping("/foreconfirmPay")
    public ResultVO foreconfirmPay(@RequestParam("oid") Integer oid){
        try {
            Order order = orderService.findById(oid);
            if (order == null) {
                return ResultVOUtil.error(1, "没有该订单");
            }
            ForeOrderVO foreOrderVO = orderService.findVOById(oid);
            return ResultVOUtil.success(foreOrderVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //确认收货
    @GetMapping("/foreorderConfirmed")
    public ResultVO foreorderConfirmed(@RequestParam("oid") Integer oid){
        try {
            Order order = orderService.findById(oid);
            if (order == null) {
                return ResultVOUtil.error(1, "没有该订单");
            }
            order.setStatus(OrderStatusEnum.WAITREVIEW.getStatus());
            order.setConfirmdate(new Date());
            orderService.update(order);
            return ResultVOUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //获取购物车
    @GetMapping("/forecart")
    public ResultVO forecart(){
        try {
            String username = (String) SecurityUtils.getSubject().getPrincipal();
            User user = userService.findByName(username);
            List<OrderItemForeVO> orderItemForeVOS = orderItemService.findByUidAndNoOid(user.getId());

            return ResultVOUtil.success(orderItemForeVOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //检查是否登录
    @GetMapping("/forecheckLogin")
    public ResultVO forecheckLogin(){
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        if (authenticated) {
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(1, "用户未登陆");
        }
    }

    //生成订单项
    public void orderItemCreat(Integer pid, Integer num){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.findByName(principal);

        Orderitem orderitem1 = orderItemService.findByUidAndPid(user.getId(), pid);
        if (orderitem1 != null) {
            orderItemService.updateNumByUidAndPid(user.getId(), pid, num);
            return;
        }

        Orderitem orderitem = new Orderitem();
        orderitem.setNumber(num);
        orderitem.setPid(pid);
        orderitem.setUid(user.getId());
        orderItemService.insertNoOid(orderitem);
    }

    //添加到购物车
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
            //生成订单项
            orderItemCreat(pid, num);

            return ResultVOUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //改变购物车商品数量
    @GetMapping("/forechangeOrderItem")
    public ResultVO forechangeOrderItem(@RequestParam("pid") Integer pid, @RequestParam("num") Integer num){
        try {
            Product product = productService.findById(pid);
            if (product == null) {
                return ResultVOUtil.error(1, "没有该产品");
            }
            if (product.getStock() < num) {
                return ResultVOUtil.error(1, "没有这么多库存");
            }
            String principal = (String) SecurityUtils.getSubject().getPrincipal();
            User user = userService.findByName(principal);
            orderItemService.updateNumByUidAndPidInsert(user.getId(), pid, num);
            return ResultVOUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }

    }

    //生成订单页面
    @GetMapping("/forebuy")
    public ResultVO forebuy(@RequestParam("oiid") int[] oiid, HttpSession session){
        try {
            List<OrderItemForeVO> orderItemForeVOS = orderItemService.findByIds(oiid);
            float total = orderItemService.getTotal(orderItemForeVOS);

            ForeBuyVO<OrderItemForeVO> foreBuyVO = new ForeBuyVO<>();
            foreBuyVO.setOrderItems(orderItemForeVOS);
            foreBuyVO.setTotal(total);

            session.setAttribute("ois", orderItemForeVOS);

            return ResultVOUtil.success(foreBuyVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }

    }

    //提交订单
    @PostMapping("/forecreateOrder")
    public ResultVO forecreateOrder(@RequestBody ForeOrderVO order, HttpSession session){
        try {
            String principal = (String) SecurityUtils.getSubject().getPrincipal();
            User user = userService.findByName(principal);
            if (user == null) {
                return ResultVOUtil.error(1, "请先登录");
            }
            List<OrderItemForeVO> orderItemForeVOS = orderService.createOrder(order, user, session);
            float total = orderItemService.getTotal(orderItemForeVOS);

            ForeBuyVO<OrderItemForeVO> foreBuyVO = new ForeBuyVO<>();
            foreBuyVO.setTotal(total);
            foreBuyVO.setOrderItems(orderItemForeVOS);
            foreBuyVO.setOid(orderItemForeVOS.get(0).getOrder().getId());

            return ResultVOUtil.success(foreBuyVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "创建订单失败");
        }

    }

    //支付
    @GetMapping("/forepayed")
    public ResultVO forepayed(@RequestParam("oid") Integer oid){
        try {
            Order order = orderService.findById(oid);
            if (order == null) {
                return ResultVOUtil.error(1, "没有该订单");
            }

            //模拟用户支付页面，应付款应该根据订单从数据库查询，而不是使用前端传过来的
            List<OrderItemForeVO> orderItemForeVOS = orderItemService.findVOByOid(oid);
            float total1 = orderItemService.getTotal(orderItemForeVOS);

            orderService.waitDelivery(oid);

            Order order1 = orderService.findById(oid);
            return ResultVOUtil.success(order1);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //全部订单页面
    @GetMapping("/forebought")
    public ResultVO forebought(){
        try {
            String principal = (String) SecurityUtils.getSubject().getPrincipal();
            User user = userService.findByName(principal);
            if (user == null) {
                return ResultVOUtil.error(1, "请先登录");
            }
            List<ForeOrderVO> foreOrderVOS = orderService.findByUid(user.getId());
            return ResultVOUtil.success(foreOrderVOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //获取评论页面
    @GetMapping("/forereview")
    public ResultVO forereview(@RequestParam("oid") Integer oid){
        try {
            Order order = orderService.findById(oid);
            if (order == null) {
                return ResultVOUtil.error(1, "找不到订单");
            }
            ForeReviewVO foreReviewVO = new ForeReviewVO();
            Orderitem orderitem = orderItemService.findFirstByOid(oid);
            Product product = productService.findById(orderitem.getPid());
            List<ReviewVO> reviewVOS = reviewService.findByPid(product.getId());

            foreReviewVO.setOrder(order);
            foreReviewVO.setProduct(product);
            foreReviewVO.setReviews(reviewVOS);

            return ResultVOUtil.success(foreReviewVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //评论
    @PostMapping("/foredoreview")
    public ResultVO foredoreview(@RequestParam("oid") Integer oid, @RequestParam("pid") Integer pid, @RequestParam("content") String content){
        try {
            Order order = orderService.findById(oid);
            Product product = productService.findById(pid);
            if (order == null || product == null) {
                return ResultVOUtil.error(1,"没有改订单或该商品");
            }
            if (content.trim() == null) {
                return ResultVOUtil.error(1, "评论内容不能为空");
            }
            String principal = (String) SecurityUtils.getSubject().getPrincipal();
            User user = userService.findByName(principal);

            Review review = new Review();
            review.setContent(content);
            review.setCreatedate(new Date());
            review.setPid(pid);
            review.setUid(user.getId());

            reviewService.create(review);
            order.setStatus(OrderStatusEnum.COMPLETE.getStatus());
            orderService.update(order);

            //获取商品内容和评论
            ForeReviewVO foreReviewVO = new ForeReviewVO();
            Orderitem orderitem = orderItemService.findFirstByOid(oid);
            List<ReviewVO> reviewVOS = reviewService.findByPid(product.getId());
            foreReviewVO.setOrder(order);
            foreReviewVO.setProduct(product);
            foreReviewVO.setReviews(reviewVOS);

            return ResultVOUtil.success(foreReviewVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //注册
    @PostMapping("/foreregister")
    public ResultVO foreregister(@RequestBody User user){
        try {
            if ("".equals(user.getName())  || "".equals(user.getPassword())) {
                return ResultVOUtil.error(1, "账号密码不能为空");
            }
            user.setSalt(user.getName());
            ByteSource salt = ByteSource.Util.bytes(user.getName());
            Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt);
            String s = md5Hash.toHex();
            user.setPassword(s);
            userService.insert(user);

            return ResultVOUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    //小登录，只有前台用户登录模式
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
