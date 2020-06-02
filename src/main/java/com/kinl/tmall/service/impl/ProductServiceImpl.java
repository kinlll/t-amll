package com.kinl.tmall.service.impl;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.dao.ProductMapper;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

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
            Integer startIndex = page.getStartIndex();
            map.put("startIndex", startIndex);
            List<Product> products = productMapper.pageQuery(map);
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
        return productMapper.selectByPrimaryKey(id);
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

        return productMapper.findbyCid(cid);
    }

}