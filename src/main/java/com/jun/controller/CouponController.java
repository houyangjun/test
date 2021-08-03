package com.jun.controller;

import com.jun.bean.Coupon;
import com.jun.service.CouponService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
@Autowired(required = false)
private CouponService couponService;
    @RequestMapping("/selectcoupon")  // /      /api/coupon/selectcoupon
    @ResponseBody
    public Map findCoupon(HttpServletRequest request, String name, Coupon coupon) {
        Map map = new HashedMap();
        int count = couponService.count(name);
        String limit = request.getParameter("limit");
        String page = request.getParameter("page");
        List<Coupon> coupons = couponService.selectByCouponDao(Integer.parseInt(limit), Integer.parseInt(page), name);
//        System.out.println("clist = " + clist);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", coupons);
        return map;
    }

    @RequestMapping("/addConpon") //       /api/coupon/addConpon
    @ResponseBody
    public  Map addConpon(Coupon coupon,String name,String begin,String end,String discount,String remark){
        Map map = new HashedMap();
        coupon.setName(name);
        coupon.setBegin(begin);
        coupon.setEnd(end);
        coupon.setDiscount(discount);
        coupon.setRemark(remark);
        System.out.println("coupon = " + coupon);
//        int i = couponService.addCoupon(coupon);
        int i = couponService.insertSelective(coupon);

        if (i>0){
            map.put("code",200);
            map.put("msg","添加成功");
        }else {
            map.put("code",500);
            map.put("msg","添加失败");
        }
        return map;
    }
    @RequestMapping("/deleteById")  //            /api/coupon/deleteById
    @ResponseBody
    public  Map deleteById(HttpServletRequest request){
        System.out.println("11111111111111");
        String id = request.getParameter("id");
        Map map = new HashedMap();
        int i = couponService.deleteById(Integer.parseInt(id));
        System.out.println("i = " + i);
        if (i>0){
            map.put("code",200);
            map.put("msg","删除成功");
        }else{
            map.put("code",500);
            map.put("msg","删除失败");
        }
        return  map;
    }

    @RequestMapping("/updateCoupon")//               /api/coupon/updateCoupon
    @ResponseBody
    public  Map updateCoupon(Coupon coupon){
        System.out.println("22222222222222222222255555555555555555");
        Map map = new HashedMap();
        int i = couponService.updateCoupon(coupon);
        System.out.println("i = " + i);
        if (i>0){
            map.put("code",200);
            map.put("msg","编辑成功");
        }else{
            map.put("code",500);
            map.put("msg","编辑失败");
        }
        return  map;
    }


    }
