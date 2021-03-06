package com.kinl.tmall.dao;

import com.kinl.tmall.VO.OrderItemForeVO;
import com.kinl.tmall.pojo.Orderitem;
import com.kinl.tmall.pojo.OrderitemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderitemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    int countByExample(OrderitemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    int deleteByExample(OrderitemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    int insert(Orderitem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    int insertSelective(Orderitem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    List<Orderitem> selectByExample(OrderitemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    Orderitem selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Orderitem record, @Param("example") OrderitemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Orderitem record, @Param("example") OrderitemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Orderitem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orderitem
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Orderitem record);

    List<Orderitem> findByOid(Integer oid);

    Integer insertNoOid(@Param("orderitem") Orderitem orderitem);

    Orderitem findByUidAndPid(@Param("uid") Integer uid,@Param("pid") Integer pid);

    Integer updateNumByUidAndPid(@Param("uid") Integer uid,@Param("pid") Integer pid, @Param("num")Integer num);

    Integer updateNumByUidAndPidInsert(@Param("uid") Integer uid,@Param("pid") Integer pid, @Param("num")Integer num);

    Integer updateById(@Param("orderItemForeVO") OrderItemForeVO orderItemForeVO);

    Orderitem findFirstByOid(Integer oid);
}