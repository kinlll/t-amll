package com.kinl.tmall.service.impl;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.VO.CategoryVO;
import com.kinl.tmall.dao.CategoryMapper;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Category;
import com.kinl.tmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }

    @Override
    public List<CategoryVO> finAllVO() {
        return categoryMapper.finAllVO();
    }

    @Override
    public Page queryPage(Map<String,Object> map) {
        Page page = new Page((Integer) map.get("pageSize"), (Integer)map.get("pageIndex"));

        Integer start = page.getStart();
        map.put("start", start);

        List<Category> categories = categoryMapper.queryList(map);
        Integer count = categoryMapper.queryCount(map);
        page.setDatas(categories);
        page.setRecord(count);
        page.setTotalPageCount(count);

        return page;
    }

    @Override
    public Integer addCategory(String categoryName) {
        List<Category> categories = categoryMapper.findByName(categoryName);
        if (categories.size() > 0){
            throw new AllException(ResultEnum.CATEGORYEXIST);
        } else {
            Category category = new Category();
            category.setName(categoryName);
            if (categoryMapper.insert(category) == 0){
                throw new AllException(ResultEnum.INSERT_ERROR);
            }
            return category.getId();
        }

    }

    @Override
    public Integer deleteById(Integer id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category == null){
            throw new AllException(ResultEnum.CATEGORYNOEXIST);
        }
        int i = categoryMapper.deleteByPrimaryKey(id);
        return i;
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer update(Category category) {
        return categoryMapper.updateByPrimaryKey(category);
    }
}
