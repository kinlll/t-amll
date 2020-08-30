package com.kinl.tmall.controller.admin;

import com.kinl.tmall.Utils.ImageUtil;
import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.EsVO;
import com.kinl.tmall.VO.ProductImageVO;
import com.kinl.tmall.VO.ProductVO;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.configuration.ElasticsearchConfiguration;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.*;
import com.kinl.tmall.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductimageService productimageService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private ElasticsearchConfiguration esConfig;

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/categories/{cid}/product")
    public ResultVO getProduct(@PathVariable("cid") Integer categoryId,
                               @RequestParam(name = "start", defaultValue = "0", required = false) Integer pageIndex,
                               @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize){
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);

        try {
            Category category = categoryService.findById(categoryId);
            if (category == null) {
                return ResultVOUtil.error(1,"分类不存在");
            }
            Page page = productService.pageQuery(map);
            return ResultVOUtil.success(page);
        } catch (AllException a){
            a.printStackTrace();;
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"加载产品异常");
        }
    }

    @RequiresPermissions(value = {"admin:select"})
    @PostMapping("/product")
    public ResultVO addProduct(@RequestBody ProductVO bean){
        try {
            Category category = categoryService.findById(bean.getCategory().getId());
            if (category == null) {
                return ResultVOUtil.error(1,"没有该分类");
            }
            bean.setCid(bean.getCategory().getId());
            productService.addVO(bean);

            // elasticsearch 添加文档
            RestHighLevelClient client = esConfig.getClient();
            EsVO esVO = new EsVO();
            esVO.setId(bean.getId());
            esVO.setName(bean.getName());
            esVO.setSubtitle(bean.getSubTitle());
            esConfig.add(esVO);

            return ResultVOUtil.success();
        } catch (AllException a){
            a.printStackTrace();;
            return ResultVOUtil.error(1, a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "添加产品异常");
        }
    }

    @RequiresPermissions(value = {"admin:select"})
    @PostMapping("/productImages")
    public ResultVO addProductImage(@RequestParam("pid") Integer productId, MultipartFile image, @RequestParam("type") String type, HttpServletRequest request){   //type=1：为产品单个图片   type=2：为产品详情图片
        try {
            Product product = productService.findById(productId);
            if (product == null) {
                return ResultVOUtil.error(1,"没有该产品");
            }
            Productimage productimage = new Productimage();
            productimage.setPid(product.getId());
            if (type != null){
                if ("single".equals(type)) {
                    productimage.setType("single");
                } else if ("detail".equals(type)) {
                    productimage.setType("detail");
                }else {
                    return ResultVOUtil.error(1,"没有该类型的图片");
                }
            } else {
                return ResultVOUtil.error(1,"图片类型不能为空");
            }
            Integer productImageId = productimageService.add(productimage);

            saveOrUpdateImageFile(productImageId, image, request);

            return ResultVOUtil.success();
        }catch (AllException a){
            a.printStackTrace();
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"添加图片失败");
        }
    }


    public void saveOrUpdateImageFile(Integer id, MultipartFile image, HttpServletRequest request) throws IOException, InterruptedException {
        //图片转存jpg格式       !!!!!!!!!!!
        File imageFile = new File(request.getServletContext().getRealPath("img/productImage"));
        File file = new File(imageFile, id + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/products/{productId}/productImages")
    public ResultVO getProductImage(@PathVariable Integer productId, @RequestParam("type") String type){
        try {
            ProductImageVO productImageVO = new ProductImageVO();
            Product product = productService.findById(productId);
            if (product == null) {
                return ResultVOUtil.error(1,"没有该产品");
            }
            if ("single".equals(type)) {
                ArrayList<Integer> simpleArray = productimageService.findByIdAndSimple(productId);
                productImageVO.setSimpleImage(simpleArray);
            }else if ("detail".equals(type)){
                ArrayList<Integer> detailsArray = productimageService.findByIdAndDetails(productId);
                productImageVO.setDetailsImage(detailsArray);
            }

            return ResultVOUtil.success(productImageVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"查询产品图片失败");
        }
    }

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/propertyvalue/{productId}")
    public ResultVO getPropertyValue(@PathVariable Integer productId){
        try {
            Product product = productService.findById(productId);
            if (product == null) {
                return ResultVOUtil.error(1,"没有该产品");
            }
            Map<Object, String> map= new HashMap<>();
            Category category = categoryService.findById(product.getCid());
            List<Property> properties = propertyService.findByCid(category.getId());
            for (Property property : properties) {
                Propertyvalue propertyvalue = propertyValueService.findByPtid(property.getId());
                if (propertyvalue == null) {
                    map.put(property,"");
                }else {
                    map.put(property,propertyvalue.getValue());
                }
            }
            return ResultVOUtil.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"查询属性失败");
        }
    }

    /**
     *   map参数  key=属性id， value=属性值
     */
    @RequiresPermissions(value = {"admin:update"})
    @PostMapping("/propertyvalue/{productId}")
    public ResultVO updatePropertyValue(@PathVariable Integer productId,@RequestBody Map<Integer, String> map){
        try {
            Product product = productService.findById(productId);
            if (product == null) return ResultVOUtil.error(1,"没有该产品");
            if (map == null || map.size()==0) {
                return ResultVOUtil.error(1,"请传入需要修改的属性值");
            } else {
               propertyValueService.updateValue(productId, map);
            }
            return ResultVOUtil.success();
        }catch (AllException a){
            a.printStackTrace();
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"更新属性值异常");
        }
    }

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/product/{productId}")
    public ResultVO getProduct(@PathVariable Integer productId){
        try {
            Product product = productService.findById(productId);
            if (product == null) {
                return ResultVOUtil.error(1,"没有该产品");
            }
            return ResultVOUtil.success(product);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"查询产品失败");
        }
    }

    @RequiresPermissions(value = {"admin:select"})
    @PutMapping("/product")
    public ResultVO put(@RequestBody Product bean){
        try {
            Product product = productService.findById(bean.getId());
            if (product == null) {
                return ResultVOUtil.error(1, "没有该产品");
            }
            productService.update(bean);
            return ResultVOUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }

    @RequiresPermissions(value = {"admin:update"})
    @PostMapping("/updateProduct/{productId}")
    public ResultVO updateProduct(@PathVariable Integer productId, @RequestBody Product requestProduct){
        try {
            productService.update(productId, requestProduct);
            return ResultVOUtil.success();
        }catch (AllException a){
            a.printStackTrace();
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"查询产品失败");
        }
    }

    @RequiresPermissions(value = {"admin:delete"})
    @DeleteMapping("/product/{id}")
    public ResultVO deleteProduct(@PathVariable("id") Integer productId){
        try {
            Product product = productService.findById(productId);
            if (product == null) {
                return ResultVOUtil.error(1,"没有该产品");
            }
            //删除产品属性值
            List<Propertyvalue> propertyvalueList = propertyValueService.findByPid(productId);
            for (Propertyvalue propertyvalue : propertyvalueList) {
                propertyValueService.deleteById(propertyvalue.getId());
            }
            productService.deleteById(productId);

            esConfig.delete(productId);

            return ResultVOUtil.success();
        } catch (AllException a){
            a.printStackTrace();
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"删除产品异常");
        }
    }


    @RequiresPermissions(value = {"admin:select"})
    @DeleteMapping("/productImages/{id}")
    public ResultVO productImages(@PathVariable Integer id, HttpServletRequest request){
        try {
            Productimage productimage = productimageService.findById(id);
            if (productimage == null) {
                return ResultVOUtil.error(1,"没有该图片");
            }
            productimageService.deleteById(id, request);

            return ResultVOUtil.success();
        } catch (AllException a){
            a.printStackTrace();
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"删除产品异常");
        }
    }
}
