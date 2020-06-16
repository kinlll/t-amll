package com.kinl.tmall.dao;

import com.kinl.tmall.VO.PropertyVO;
import com.kinl.tmall.pojo.Property;
import com.kinl.tmall.pojo.PropertyExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface PropertyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    int countByExample(PropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    int deleteByExample(PropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    int insert(Property record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    int insertSelective(Property record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    List<Property> selectByExample(PropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    Property selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Property record, @Param("example") PropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Property record, @Param("example") PropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Property record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Property record);

    List<Property> queryPage(@Param("map") Map<String, Object> map);

    Integer queryCount(@Param("map") Map<String, Object> map);

    Property findByName(String name);

    Integer updateName(@Param("property") Property property);

    List<Property> findByCid(Integer cid);

    int insertVO(@Param("propertyVO") PropertyVO propertyVO);

    Property findByCidAndName(@Param("cid") Integer cid,@Param("name") String name);
}