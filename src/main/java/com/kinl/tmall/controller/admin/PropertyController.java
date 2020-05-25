package com.kinl.tmall.controller.admin;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Category;
import com.kinl.tmall.pojo.Property;
import com.kinl.tmall.pojo.Propertyvalue;
import com.kinl.tmall.service.CategoryService;
import com.kinl.tmall.service.PropertyService;
import com.kinl.tmall.service.PropertyValueService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class PropertyController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyValueService propertyValueService;

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/pageProperty/{categoryId}")
    public ResultVO pageProperty(@PathVariable Integer categoryId,
                                 @RequestParam(name = "pageIndex", defaultValue = "1", required = false) Integer pageIndex,
                                 @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize){
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        try {
            Category category = categoryService.findById(categoryId);
            if (category == null){
                return ResultVOUtil.error(1, "没有该分类");
            }
            Page page = propertyService.queryPage(map);
            return ResultVOUtil.success(page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "查询失败");
        }
    }

    @RequiresPermissions(value = {"admin:create"})
    @PostMapping("/property/{categoryId}")
    public ResultVO addProperty(@PathVariable Integer categoryId, Property property){
        try {
            if (property.getName() == null || "".equals(property.getName())){
                return ResultVOUtil.error(1,"属性名称不能为空");
            }
            Category category = categoryService.findById(categoryId);
            if (category == null){
                return ResultVOUtil.error(1, "没有该分类");
            }
            property.setCid(categoryId);
            propertyService.add(property);
        } catch (AllException a) {
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "添加失败");
        }
        return ResultVOUtil.success();
    }

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/getProperty")
    public ResultVO getProperty(Integer propertyId){
        try {
           /* Category category = categoryService.findById(categoryId);
            if (category == null){
                return ResultVOUtil.error(1, "没有该分类");
            }*/
            Property property = propertyService.findById(propertyId);
            if (property == null){
                return ResultVOUtil.error(1, "没有该属性");
            }
            return ResultVOUtil.success(property);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"查询属性失败");
        }
    }

    @RequiresPermissions(value = {"admin:create"})
    @PostMapping("/property")
    public ResultVO addProperty(Property property){
        try {
            propertyService.update(property);
            return ResultVOUtil.success();
        } catch (AllException a){
            a.printStackTrace();
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"更新失败");
        }
    }

    @RequiresPermissions(value = {"admin:delete"})
    @DeleteMapping("/property/{propertyId}")
    public ResultVO deleteProperty(@PathVariable Integer propertyId){
        try {
            Property property = propertyService.findById(propertyId);
            if (property == null) return ResultVOUtil.error(1,"属性不存在");
            Propertyvalue propertyvalue = propertyValueService.findByPtid(propertyId);
            if (propertyvalue != null) return ResultVOUtil.error(1,"请先删除属性的属性值");
            propertyService.deleteById(propertyId);
            return ResultVOUtil.success();
        } catch (AllException a){
            a.printStackTrace();
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"删除属性失败");
        }
    }

}
