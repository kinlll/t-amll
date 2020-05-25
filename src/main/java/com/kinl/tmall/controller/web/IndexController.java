package com.kinl.tmall.controller.web;

import com.kinl.tmall.VO.CategoryVO;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.service.CategoryService;
import com.kinl.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/web")
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/index")
    public ResultVO index(){
        ResultVO resultVO = new ResultVO();
        try {
            List<CategoryVO> categoryVOS = categoryService.finAllVO();
            for (CategoryVO categoryVO : categoryVOS) {
                List<Product> products = productService.findByCidFive(categoryVO.getId());
                categoryVO.setProducts(products);
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
