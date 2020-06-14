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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    public ArrayList<Integer> findByIdAndSimple(Integer id) {
        ArrayList<Integer> simple = productimageMapper.findByIdAndSimple(id);

        //前端设置图片地址，这里只返回图片id
        /*<String> simpleArray = new ArrayList<>();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String realPath = request.getServletContext().getRealPath("img/productImage");
        for (Integer integer : simple) {
            String s = integer.toString();
            String filePath = realPath + "\\" + s;
            simpleArray.add(filePath);
        }*/
        return simple;
    }

    @Override
    public ArrayList<Integer> findByIdAndDetails(Integer id) {
        ArrayList<Integer> details = productimageMapper.findByIdAndDetails(id);

        //前端设置图片地址，这里只返回图片id
        /*ArrayList<String> detailsArray = new ArrayList<>();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String realPath = request.getServletContext().getRealPath("img/productImage");
        for (Integer integer : details) {
            String s = integer.toString();
            String filePath = realPath + "\\" + s;
            detailsArray.add(filePath);
        }*/
        return details;
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

    @Override
    public Productimage findFirstImageByPid(Integer pid) {
        return productimageMapper.findFirstImageByPid(pid);
    }

    @Override
    public List<Productimage> findAllSimpleByPid(Integer pid) {
        return productimageMapper.findAllSimpleByPid(pid);
    }

    @Override
    public List<Productimage> findAllDetailsByPid(Integer pid) {
        return productimageMapper.findAllDetailsByPid(pid);
    }

    @Override
    public Productimage findById(Integer id) {
        return productimageMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteById(Integer id, HttpServletRequest request) {
        int i = productimageMapper.deleteByPrimaryKey(id);
        if (i == 0) {
            throw new AllException(ResultEnum.DELETE_PRODUCT_IMAGE_ERROR);
        }
        File imageFile = new File(request.getServletContext().getRealPath("img/productImage"));
        File file = new File(imageFile, id + ".jpg");
        boolean delete = file.delete();
        if (!delete) {
            throw new AllException(ResultEnum.DELETE_PRODUCT_IMAGE_ERROR);
        }

    }


}
