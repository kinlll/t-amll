package com.kinl.tmall.controller.admin;

import com.kinl.tmall.Utils.ImageUtil;
import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Category;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.pojo.Property;
import com.kinl.tmall.service.CategoryService;
import com.kinl.tmall.service.ProductService;
import com.kinl.tmall.service.PropertyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PropertyService propertyService;

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/pageCategory")
    public ResultVO pageCategory(@RequestParam(name = "pageSize",defaultValue = "5",required = false) Integer pageSize,
                                 @RequestParam(name = "start",defaultValue = "0",required = false) Integer start){
        Map<String,Object> map = new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("start",start);
        try {
            Page page = categoryService.queryPage(map);
            return   ResultVOUtil.success(page);
        } catch (Exception e) {
            e.printStackTrace();
            return    ResultVOUtil.error(1,"查询失败");
        }
    }

    @RequiresPermissions(value = {"admin:create"})
    @Transactional
    @PostMapping("/addCategory")
    public ResultVO addCategory(String categoryName, MultipartFile image, HttpServletRequest request){
        if ("".equals(categoryName) || categoryName == null){
            throw new AllException(ResultEnum.CATEGORYE_EMPTY);
        }
        try {
            Integer id = categoryService.addCategory(categoryName);

            saveOrUpdateImageFile(id, image, request);

            return ResultVOUtil.success();
        }catch (AllException a){
            a.printStackTrace();
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"添加失败");
        }
    }

    public void saveOrUpdateImageFile(Integer id, MultipartFile image, HttpServletRequest request) throws IOException, InterruptedException {
        //图片转存jpg格式       !!!!!!!!!!!
        File imageFile = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFile, id + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    @RequiresPermissions(value = {"admin:delete"})
    @DeleteMapping("/category/{id}")
    public ResultVO deleteCategory(@PathVariable("id") Integer id, HttpServletRequest request){
        try {
            Category category = categoryService.findById(id);
            if (category == null) return ResultVOUtil.error(1,"不存在该分类");

            //判断分类从表对应属性、产品是否删除
            List<Product> productList = productService.findByCidFive(id);
            if (productList.size()>0) return ResultVOUtil.error(1,"请先删除分类下产品");
            List<Property> propertyList = propertyService.findByCid(id);
            if (propertyList.size()>0) return ResultVOUtil.error(1,"请先删除分类下属性");

            Integer result = categoryService.deleteById(id);
            if (result == 0){
                return ResultVOUtil.error(1,"删除分类失败");
            }
            File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
            File file = new File(imageFolder, id + ".jpg");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"删除分类图片失败");
    }
        return ResultVOUtil.success();
    }

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/category/{id}")
    public ResultVO getCategory(@PathVariable("id") Integer id){
        try {
            Category category = categoryService.findById(id);
            return ResultVOUtil.success(category);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"找不到分类");
        }
    }

    @RequiresPermissions(value = {"admin:update"})
    @PutMapping("/category/{id}")
    public ResultVO putCategory(@PathVariable("id") Integer id, String name, MultipartFile image, HttpServletRequest request){
        try {
            Category category = categoryService.findById(id);
            if (category == null) {
                return ResultVOUtil.error(1,"没有该分类");
            }
            category.setName(name);
            Integer update = categoryService.update(category);
            if (update == 0) {
                return ResultVOUtil.error(1,"更新失败");
            }
            if (image != null) {
                saveOrUpdateImageFile(id, image, request);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new AllException(1,"更新图片失败");
        }
        return ResultVOUtil.success();
    }

}
