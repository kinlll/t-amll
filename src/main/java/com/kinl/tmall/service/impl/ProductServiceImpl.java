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
import com.kinl.tmall.service.CategoryService;
import com.kinl.tmall.service.ProductService;
import com.kinl.tmall.service.ProductimageService;
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

    @Override
    public List<Product> findbyCid(Integer cid) {
        List<Product> products = null;
        try {
            products = productMapper.findbyCid(cid);
            for (Product product : products) {
                Productimage firstImage = productimageService.findFirstImageByPid(product.getId());
                List<Productimage> simpleByPid = productimageService.findAllSimpleByPid(product.getId());
                List<Productimage> detailsByPid = productimageService.findAllDetailsByPid(product.getId());

                product.setFirstProductImage(firstImage);
                product.setProductSingleImages(simpleByPid);
                product.setProductDetailImages(detailsByPid);

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

}