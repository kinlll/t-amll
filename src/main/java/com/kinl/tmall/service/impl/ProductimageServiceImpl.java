package com.kinl.tmall.service.impl;

import com.kinl.tmall.dao.ProductimageMapper;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Productimage;
import com.kinl.tmall.service.ProductimageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class ProductimageServiceImpl implements ProductimageService {

    @Autowired
    private ProductimageMapper productimageMapper;

    @Override
    public Integer add(Productimage productimage) {
        int insert = productimageMapper.insert(productimage);
        if (insert == 0){
            throw new AllException(ResultEnum.ADD_PRODUCTIMAGE_ERROR);
        }
        return productimage.getId();
    }

    @Override
    public ArrayList<String> findByIdAndSimple(Integer id) {
        ArrayList<Integer> simple = productimageMapper.findByIdAndSimple(id);
        ArrayList<String> simpleArray = new ArrayList<>();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String realPath = request.getServletContext().getRealPath("img/productImage");
        for (Integer integer : simple) {
            String s = integer.toString();
            String filePath = realPath + "\\" + s;
            simpleArray.add(filePath);
        }
        return simpleArray;
    }

    @Override
    public ArrayList<String> findByIdAndDetails(Integer id) {
        ArrayList<Integer> details = productimageMapper.findByIdAndDetails(id);
        ArrayList<String> detailsArray = new ArrayList<>();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String realPath = request.getServletContext().getRealPath("img/productImage");
        for (Integer integer : details) {
            String s = integer.toString();
            String filePath = realPath + "\\" + s;
            detailsArray.add(filePath);
        }
        return detailsArray;
    }

    @Override
    public String findFirstImage(Integer pid) {
        Integer imageId = productimageMapper.findFirstImage(pid);
        if (imageId == null){
            return null;
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String realPath = request.getServletContext().getRealPath("img/productImage");
        return realPath + "\\" + imageId;
    }


}
