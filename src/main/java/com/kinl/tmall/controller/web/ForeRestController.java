package com.kinl.tmall.controller.web;

import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.CategoryVO;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.pojo.Category;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.service.CategoryService;
import com.kinl.tmall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ForeRestController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/forecategory/{cid}")
    public ResultVO forecategory(@PathVariable("cid") Integer cid, String sort){
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

                    break;
                case "review" :

                    break;
                case "date" :

                    break;
                case "saleCount" :

                    break;
                case "price" :

                    break;
            }
        }
    }
}
