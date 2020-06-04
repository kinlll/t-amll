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
                List<Product> products = productService.findbyCid(categoryVO.getId());
                categoryVO.setProducts(products);
                //List<List<Product>> listList = groupList(products);

                /**
                 * 拆分List集合成多个
                 */
                List<List<Product>> lists = new ArrayList<>();
                int size = products.size();
                //截取的子集合长度
                int toIndex = 7;
                for (int i = 0; i < lists.size(); i+=7){
                    if (i + 7 > size){
                        toIndex = size - i;
                    }
                    List<Product> products1 = products.subList(i, i + toIndex);
                    lists.add(products1);
                    categoryVO.setProducts(products1);
                }



                categoryVO.setData(lists);

            }
            resultVO.setData(categoryVOS);
            resultVO.resultVO(0,"成功");
        } catch (Exception e) {
            resultVO.resultVO(1,"失败");
            e.printStackTrace();
        }
        return resultVO;
    }


    //拆分List集合成多个
    public static List<List<Product>> groupList(List<Product> products){
        List<List<Product>> lists = new ArrayList<>();
        int size = products.size();
        //截取的子集合长度
        int toIndex = 7;
        for (int i = 0; i < lists.size(); i+=7){
            if (i + 7 > size){
                toIndex = size - i;
            }
            List<Product> products1 = products.subList(i, i + toIndex);
            lists.add(products1);
        }

        return lists;
    }



}
