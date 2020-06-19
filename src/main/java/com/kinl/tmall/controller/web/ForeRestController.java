package com.kinl.tmall.controller.web;

import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.CategoryVO;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.comparator.*;
import com.kinl.tmall.pojo.Category;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.service.CategoryService;
import com.kinl.tmall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/web")
public class ForeRestController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

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
}
