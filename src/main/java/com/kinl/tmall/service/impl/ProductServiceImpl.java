package com.kinl.tmall.service.impl;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.ProductVO;
import com.kinl.tmall.dao.ProductMapper;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Category;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.pojo.Productimage;
import com.kinl.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductimageService productimageService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderItemService orderItemService;

    @Override
    public List<Product> findAll() {
        return productMapper.findAll();
    }

    @Override
    public List<Product> findByCidFive(Integer cid) {
        return productMapper.findByCidFive(cid);
    }

    @Override
    public Page pageQuery(Map<String, Object> map) {
        try {
            Page page = new Page((Integer) map.get("pageSize"),(Integer) map.get("pageIndex"));
            Integer start = page.getStart();
            map.put("start", start);
            List<Product> products = productMapper.pageQuery(map);
            for (Product product : products) {
                Productimage firstImageByPid = productimageService.findFirstImageByPid(product.getId());
                product.setFirstProductImage(firstImageByPid);
            }
            Integer count = productMapper.queryCount(map);
            page.setDatas(products);
            page.setRecord(count);
            page.setTotalPageCount(count);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AllException(ResultEnum.PRODUCT_PAGE_ERROR);
        }
    }

    @Override
    public Integer add(Product product) {
            product.setCreatedate(new Date());
            int insert = productMapper.insert(product);
            if (insert == 0) {
                throw new AllException(ResultEnum.ADD_PRODUCT_ERROR);
            }
            return insert;
    }

    @Override
    public Product findById(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new AllException(ResultEnum.PRODUCT_NOEXIST);
        }
        //获取图片
        getProductImage(product);
        //获取评论总数和销量 (这里暂时逻辑为加入购物车就增加销量)
        getReviewAndSaleCount(product);
        Category category = categoryService.findById(product.getCid());
        product.setCategory(category);

        return product;
    }

    @Override
    public void update(Integer productId, Product requestProduct) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            throw new AllException(ResultEnum.PRODUCT_NOEXIST);
        }
        requestProduct.setId(productId);
        Integer update = productMapper.update(requestProduct);
        if (update == 0) {
            throw new AllException(ResultEnum.PRODUCT_UPDATE_ERROR);
        }
    }

    @Override
    public Integer deleteById(Integer id) {
        int i = productMapper.deleteByPrimaryKey(id);
        if (i == 0) {
            throw new AllException(ResultEnum.PRODUCT_EXIST_PROPERTYVALUE);
        }
        return i;
    }

    @Override
    public Float findPromotepriceById(Integer pid) {
        Float promoteprice = productMapper.findPromotepriceById(pid);
        return promoteprice;
    }

    @Override
    public String findNameById(Integer id) {
        return productMapper.findNameById(id);
    }


    //加载产品所有图片，包括首图，单个图片，详情图片
    private void getProductImage(Product product){
        Productimage firstImage = productimageService.findFirstImageByPid(product.getId());
        List<Productimage> simpleByPid = productimageService.findAllSimpleByPid(product.getId());
        List<Productimage> detailsByPid = productimageService.findAllDetailsByPid(product.getId());

        product.setFirstProductImage(firstImage);
        product.setProductSingleImages(simpleByPid);
        product.setProductDetailImages(detailsByPid);
    }

    //获取产品的评论总数和销量(这里暂时逻辑为加入购物车就增加销量)
    private void getReviewAndSaleCount(Product product){
        Integer reviewCount = reviewService.findCountBypid(product.getId());
        Integer saleCount = orderItemService.countByPid(product.getId());
        product.setReviewCount(reviewCount);
        product.setSaleCount(saleCount);
    }

    @Override
    public List<Product> findbyCid(Integer cid) {
        List<Product> products = null;
        try {
            products = productMapper.findbyCid(cid);
            for (Product product : products) {
               getProductImage(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AllException(ResultEnum.FIND_PRODUCT_ERROR);
        }
        return products;
    }

    @Override
    public Integer addVO(ProductVO product) {
        //todo  jvm时间比当前时间早8小时
        product.setCreatedate(new Date());
        return productMapper.insertVO(product);
    }

    @Override
    public Integer updateVO(ProductVO product) {
        Integer integer = productMapper.updateVO(product);
        if (integer == 0) {
            throw new AllException(ResultEnum.PRODUCT_UPDATE_ERROR);
        }
        return integer;
    }

    @Override
    public Integer update(Product product) {
        return productMapper.updateP(product);
    }

}