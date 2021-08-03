package com.jun.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jun.bean.Master;
import com.jun.bean.MasterExample;
import com.jun.service.MasterService;
import com.jun.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/master")
public class MasterController {
    @Autowired(required = false)
    private MasterService masterService;
    @Autowired
    private OrdersService ordersService;
    // 登录页  sql:  select * from master where  account = ? and password = ?
    // 单挑的 动态的查询.   已经写好了. 怎么使用呢 ?
    @RequestMapping("/login")   //  /api/master/login
    public Map login(Master master, HttpSession session) {
        Map map = new HashMap();
        MasterExample example = new MasterExample();
        MasterExample.Criteria criteria = example.createCriteria();
                    criteria.andAccountEqualTo(master.getAccount());
                    criteria.andPasswordEqualTo(master.getPassword());
        List<Master> masters = masterService.selectByExample(example);
        System.out.println("masters = " + masters);
        if (masters.size()==1) {
            Master master1 = masters.get(0);
            session.setAttribute("master1",master1);
            map.put("code",0);
            map.put("msg","登录成共");
            System.out.println("master1 = " + master1);
            return map;
        }else{
            map.put("code",400);
            map.put("msg","登录失败");
            System.out.println("masters = " + masters);
            return map;
        }
    }
//
//    // 查-- 查询+自身对象的查询 + 分页
//// 分页查询
//    @RequestMapping("/selectAllByPage")
//    public Map selectAllByPage(Master master, @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
//                               @RequestParam(value = "limit", required = true) Integer pageSize) {
//        // 调用service 层   , 适用于 单表!!!
//        PageHelper.startPage(page, pageSize);
//        MasterExample example = new MasterExample();
//        MasterExample.Criteria criteria = example.createCriteria();
//
////    if (null!=master.getYYYYYYYY()&&!"".equals(master.getYYYYYYY())){
////         criteria.andPhoneEqualTo(master.getPhone());   // 1
////    }
//
//        List<Master> mastersList = masterService.selectByExample(example);
//        PageInfo pageInfo = new PageInfo(mastersList);
//        long total = pageInfo.getTotal();
//        Map responseMap = new HashMap();
//        responseMap.put("code", 0);
//        responseMap.put("msg", "查询成功");
//        responseMap.put("pageInfo", pageInfo);
//        responseMap.put("count", total);
//        return responseMap;
//    }
// 查-- 查询+自身对象的查询 + 分页
// 分页查询
@RequestMapping("/selectCustomer")  // //api/master/selectCustomer
@ResponseBody
public Map selectAllByPage(Master   master, @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
                           @RequestParam(value = "limit", required = true) Integer pageSize) {
    // 调用service 层   , 适用于 单表!!!
    PageHelper.startPage(page, pageSize); // 开启 第几页,  每页 条数
    MasterExample  example = new MasterExample ();
    MasterExample .Criteria criteria = example.createCriteria();
    if (null!=master.getPhone()&&!"".equals(master.getPhone())){
        criteria.andPhoneEqualTo(master.getPhone());   // 1
    }
    if (null!=master.getName()&&!"".equals(master.getName())){
        criteria.andNameEqualTo(master.getName());   // 2
    }
    List<Master> customerList = masterService.selectByExample(example);
    System.out.println("customerList = " + customerList);
    PageInfo pageInfo = new PageInfo(customerList);  // 是 把查出来的 所有数据 进行 拦截.
    long total = pageInfo.getTotal();  // 总条数
    Map responseMap = new HashMap();
    responseMap.put("code", 0);
    responseMap.put("msg", "查询成功");
    responseMap.put("pageInfo", pageInfo);
    responseMap.put("count", total);
    return responseMap;
}
    // 删
    // 删除订单  根据 主键 id 删除
    @RequestMapping("/deleteById")     // /api/master/deleteById
    public Map deleteById(@RequestParam(value = "id") Integer id) {
        Map responseMap = new HashMap();
        int i = masterService.deleteByPrimaryKey(id);
        if (i > 0) {
            responseMap.put("code", 200);
            responseMap.put("msg", "删除成功");
            return responseMap;
        } else {
            responseMap.put("code", 400);
            responseMap.put("msg", "删除失败");
            return responseMap;
        }
    }
    //下拉
    @RequestMapping("/selectById")     // /api/master/selectById
    @ResponseBody
    public Map selectById(@RequestParam(value = "id") Integer id,HttpSession session) {
        Map map=new HashMap();
        System.out.println("id = " + id);
        System.out.println(" = ================================================================");
        session.setAttribute("id",id);
//        List<Master> customerList = masterService.selectByExample(null);
//        System.out.println("customerList = " + customerList);
//        session.setAttribute("customerList",customerList);
//        map.put("customerList",customerList);
        map.put("code",0);
        map.put("msg","");
        map.put("data","哈哈哈");
       return  map;
    }

    @RequestMapping("selectMaster")    //         /api/master/selectMaster
    @ResponseBody
    public Map selectMaster(HttpSession session){
        Map map=new HashMap();
        List<Master> customerList = masterService.selectByExample(null);
        System.out.println("customerList = " + customerList);
        session.setAttribute("customerList",customerList);
        map.put("customerList",customerList);
        map.put("msg","找到了");
        return map;
    }
    @RequestMapping("uptMaster")    //         /api/master/uptMaster
    @ResponseBody
    public Map uptMaster(HttpSession session, @RequestParam(value = "mid") Integer mid) {
        int id = (int) session.getAttribute("id");
        System.out.println("id = " + id);
        System.out.println("mid = " + mid);
        Map map = new HashMap();
        int i = ordersService.UPAllOrders(id, mid);
        if (i > 0) {
            map.put("code", 0);
            map.put("data", "");
            map.put("msg", "派单成功");
            return map;
        } else {
            map.put("code", 500);
            map.put("data", "");
            map.put("msg", "派单失败");
            return map;
        }
    }

    @RequestMapping("uptOrders")    //         /api/master/uptOrders
    @ResponseBody
    public Map uptOrders(HttpSession session, @RequestParam(value = "status") int status) {
        int id = (int) session.getAttribute("id");
        System.out.println("id = " + id);
        System.out.println("status = " + status);
        Map map = new HashMap();
        int i = ordersService.UPAllOrdersStrtus(id,status);
        if (i > 0) {
            map.put("code", 0);
            map.put("data", "");
            map.put("msg", "派单成功");
            return map;
        } else {
            map.put("code", 500);
            map.put("data", "");
            map.put("msg", "派单失败");
            return map;
        }
    }

    @RequestMapping("/inMaster")       //         /api/master/inMaster
    @ResponseBody
    public Map inMaster (Master master){
        Map map = new HashMap();
        int i = masterService.insertSelective(master);
        if (i>0){
            map.put("code",0);
            map.put("msg","新增成功");
            map.put("data","");
            return map;
        }else {
            map.put("code",500);
            map.put("msg","新增失败");
            map.put("data","");
            return map;
        }
    }
    @RequestMapping("uptMasterBid")    //         /api/master/uptMasterBid
    @ResponseBody
    public Map uptMasterBid(Master master,HttpSession session) {
        int id = (int)session.getAttribute("id");
        Map map = new HashMap();
        MasterExample example= new MasterExample();
        MasterExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
//        int i = masterService.updateByPrimaryKeySelective(master);
        int i = masterService.updateByExampleSelective(master, example);
        if (i > 0) {
            map.put("code", 0);
            map.put("data", "");
            map.put("msg", "修改成功");
            return map;
        } else {
            map.put("code", 500);
            map.put("data", "");
            map.put("msg", "修改失败");
            return map;
        }
    }
    //Master  id   查
    @RequestMapping("/selectOrederById")     // /api/master/selectOrederById
    @ResponseBody
    public Map selectOrederById(HttpSession session) {
        int id = (int)session.getAttribute("id");
        Map map=new HashMap();
        System.out.println("id = " + id);
        System.out.println(" = ================================================================");
        session.setAttribute("id",id);
        Master master = masterService.selectByPrimaryKey(id);
        System.out.println("master = " + master.toString());
        session.setAttribute("master",master);
        map.put("code",0);
        map.put("msg","");
        map.put("id",master.getId());
        map.put("name",master.getName());
        map.put("sex",master.getSex());
        map.put("age",master.getAge());
        map.put("phone",master.getPhone());
        map.put("account",master.getAccount());
        map.put("password",master.getPassword());
        map.put("data","哈哈哈");
        return  map;
    }

}
