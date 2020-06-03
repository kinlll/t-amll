package com.kinl.tmall.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.kinl.tmall.VO.CategoryVO;
import com.kinl.tmall.VO.ProductRedisVO;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.service.CategoryService;
import com.kinl.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/web")
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/forehome")
    public ResultVO index(){
        ResultVO resultVO = new ResultVO();
        try {
            List<CategoryVO> categoryVOS = categoryService.finAllVO();
            for (CategoryVO categoryVO : categoryVOS) {
                List<Product> products = productService.findByCidFive(categoryVO.getId());
                categoryVO.setProducts(products);

                //从redis加载home页自定义标签
                String s = stringRedisTemplate.opsForValue().get(categoryVO.getName());

                List<ProductRedisVO> productRedisVOS = new ArrayList<>();
                HashMap hashMap = JSONObject.parseObject(s, HashMap.class);

                Iterator iterator = hashMap.keySet().iterator();
                while (iterator.hasNext()) {
                    ProductRedisVO productRedisVO = new ProductRedisVO();
                    Integer key = (Integer) iterator.next();
                    productRedisVO.setId(key);
                    productRedisVO.setName((String) hashMap.get(key));
                    productRedisVOS.add(productRedisVO);
                }
                categoryVO.setData(productRedisVOS);

            }
            resultVO.setData(categoryVOS);
            resultVO.resultVO(0,"成功");
        } catch (Exception e) {
            resultVO.resultVO(1,"失败");
            e.printStackTrace();
        }
        return resultVO;
    }



}
