package com.jun.service;


import com.jun.bean.Coupon;
import com.jun.bean.CouponExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponService {
   
    long countByExample(CouponExample example);

    int deleteByExample(CouponExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Coupon record);

    int insertSelective(Coupon record);

    List<Coupon> selectByExample(CouponExample example);

    Coupon selectByPrimaryKey(Integer id);
  
    int updateByExampleSelective(@Param("record") Coupon record, @Param("example") CouponExample example);

    int updateByExample(@Param("record") Coupon record, @Param("example") CouponExample example);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);
    public List<Coupon> selectByCouponDao(@Param("pageSize") int pageSize, @Param("pageCode") int pageCode, @Param("name") String name);
    public  int count(@Param("name") String name);

    //优惠劵的添加
    public  int addCoupon(Coupon coupon);
    //优惠劵的删除
    public  int deleteById(Integer id);
    //修改
    public int updateCoupon(Coupon coupon);
}
