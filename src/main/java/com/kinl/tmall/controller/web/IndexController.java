package com.kinl.tmall.controller.web;

import com.kinl.tmall.VO.CategoryVO;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.service.CategoryService;
import com.kinl.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/web")
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Cacheable(value = "product")
    @GetMapping("/forehome")
    public ResultVO<List<CategoryVO>> index(){
        ResultVO<List<CategoryVO>> resultVO = new ResultVO<>();
        try {
            List<CategoryVO> categoryVOS = categoryService.finAllVO();
            for (CategoryVO categoryVO : categoryVOS) {
                List<Product> products = productService.findbyCid(categoryVO.getId());
                categoryVO.setProducts(products);

                List<List<Product>> listList = groupList(products);

                categoryVO.setProductsByRow(listList);

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
        for (int i = 0; i <products.size(); i+=7){
            if (i + 7 > size){
                toIndex = size - i;
            }

            //使用subList，在redis读取时出现了序列化问题，需要用一个new ArrayLust封装一下 !!!!!!
//            List<Product> products1 = products.subList(i, i + toIndex);
            ArrayList<Product> products1 = new ArrayList<>(products.subList(i, i + toIndex));
            lists.add(products1);
        }

        return lists;
    }



}
