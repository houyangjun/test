package com.jun.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jun.bean.Master;
import com.jun.bean.Orders;
import com.jun.bean.OrdersExample;
import com.jun.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired(required = false)
    private OrdersService ordersService;
    @RequestMapping("/getMoney")
    public Map getMoney(Double lng, Double lat) {
            // 假定一个 维修中心的经度和维度
            Double shopLng = 113.838124;//店面位置
            Double shopLat = 34.729147;//店面位置
            // 不除100 是 米数 . 除以100是 人民币钱数.
            double rmb = MapUtils.algorithm(lng, lat, shopLng, shopLat) / 100;
            Map map = new HashMap();
            // 根据 lng  lat 计算  2点之间的距离. 这个是个 公式!!  百度的...
            // 算出 距离后,  比如 4000米 ,  收费40 .   结果除以100 费用.
            map.put("code", 0);
            map.put("msg", "计算成功");
            map.put("data", rmb);
    return map;
}
@RequestMapping("/addOrders")  // /orders/addOrders
@ResponseBody
public Map addOrders(Orders orders, HttpSession session){
    System.out.println(" = ");
        Map map=new HashMap();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        orders.setCreatetime(sdf.format(new Date()));
        orders.setStatus("0");
    orders.setPhone((String)(session.getAttribute("phone")));
    System.out.println("orders = " + orders.toString());
    int i= ordersService.addOrders(orders);
    if (i>0){
        map.put("code",0);
        map.put("msg","亲，下单成功");

    }else {
        map.put("code",500);
        map.put("msg","亲，下单失败");
    }
    return map;
}
//@RequestMapping("/ordersselect")   // /orders/ordersselect
//@ResponseBody
//    public  Map ordersselect(HttpServletRequest request){
//   int pageSize=2;
//   int pageCode=0;
//   int totalCode=0;
//   int totalCount=0;
//     totalCount = ordersService.count();
//    if (totalCount%pageSize==0){
//        totalCode=totalCount/pageSize;
//    }else {
//        totalCode=totalCount/pageSize+1;
//    }
//    if (request.getParameter("pageCode")==null||request.getParameter("pageCode")==""){
//        pageCode=1;
//    }else {
//        if (Integer.parseInt(request.getParameter("pageCode"))>totalCode){
//            pageCode=totalCode;
//        }else {
//            pageCode= Integer.parseInt(request.getParameter("pageCode"));
//        }
//    }
//    request.setAttribute("totalCode",totalCode);
//    request.setAttribute("pageCode",pageCode);
//
//    Map map=new HashMap();
//    List<Orders> all = ordersService.findAll(pageSize, (pageCode - 1) * pageSize);
//    map.put("code",0);
//    map.put("msg","");
//    map.put("count",totalCount);
//    map.put("data",all);
//    System.out.println("all = " + all.toString());
//return map;
//}
@RequestMapping("/UpdateOrders")       // /orders/UpdateOrders
    @ResponseBody
    public  Map UpdateOrders(String id){
        Map map=new HashMap();
        System.out.println("-----------");
        int i = ordersService.UpdateOrders(Integer.parseInt(id),"-1");
        System.out.println("id = " + id);
        if (i>0){
            map.put("code",0);
            map.put("msg","成功");
        }else {
            map.put("code",400);
            map.put("msg","失败");
        }
        return map;
}
    @RequestMapping("/UpdateOrderspai")       // /orders/UpdateOrders
    @ResponseBody
    public  Map UpdateOrderspai(String id){
        Map map=new HashMap();
        System.out.println("-----------");
        int i = ordersService.UpdateOrders(Integer.parseInt(id),"1");
        System.out.println("id = " + id);
        if (i>0){
            map.put("code",0);
            map.put("msg","成功");
        }else {
            map.put("code",400);
            map.put("msg","失败");
        }
        return map;
    }
    @RequestMapping("/selectOrdersByIfs")       // /orders/selectOrdersByIfs
    @ResponseBody
    public Map selectOrdersByIfs(HttpServletRequest request,String status,String phone){
        Map map=new HashMap();
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        int page1 = Integer.parseInt(page);
        int limit1 = Integer.parseInt(limit);
        System.out.println("limit1 = " + limit1);
        System.out.println("page1 = " + page1);
        int count = ordersService.count();
        System.out.println("count = " + count);
        List<Orders> orders = ordersService.selectOrdersByIfs(status, phone);
        if (orders!=null){
//            List<Orders> all = ordersService.findAll(limit1, (page1 - 1) * limit1);
//            System.out.println("all = " + all);
            map.put("code",0);
            map.put("msg","成功");
        }else {
            map.put("code",400);
            map.put("msg","没有找到");
        }
        return map;
    }
    @RequestMapping("/selectOrdersByIfsO")       // /orders/selectOrdersByIfsO
    @ResponseBody
    public Map selectOrdersByIfsO(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map map=new HashMap();
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        int count = ordersService.count();
        List<Orders> all = ordersService.selectByOrders(Integer.parseInt(limit),Integer.parseInt(page));
        System.out.println("all = " + all);
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data",all);
        return map;
    }


    // 查-- 查询+自身对象的查询 + 分页
// 分页查询
    @RequestMapping("/selectAllByPage")  // /orders/selectAllByPage
    public Map selectAllByPage(Orders orders, @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
                               @RequestParam(value = "limit", required = true) Integer pageSize) {
        // 调用service 层   , 适用于 单表!!!
        PageHelper.startPage(page, pageSize); // 开启 第几页,  每页 条数
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        if (null!=orders.getPhone()&&!"".equals(orders.getPhone())){
            criteria.andPhoneEqualTo(orders.getPhone());   // 1
        }
        if (null!=orders.getCost()&&!"".equals(orders.getCost())){
            criteria.andCostEqualTo(orders.getCost());   // 2
        }
        if (null!=orders.getContents()&&!"".equals(orders.getContents())){
            criteria.andContentsEqualTo(orders.getContents());   // 3
        }
        if (null!=orders.getStatus()&&!"".equals(orders.getStatus())){
            criteria.andStatusEqualTo(orders.getStatus());   // 4
        }

        List<Orders> orderssList = ordersService.selectByExample(example);
        System.out.println("orderssList = " + orderssList);
        PageInfo pageInfo = new PageInfo(orderssList);  // 是 把查出来的 所有数据 进行 拦截.
        long total = pageInfo.getTotal();  // 总条数
        Map responseMap = new HashMap();
        responseMap.put("code", 0);
        responseMap.put("msg", "查询成功");
        responseMap.put("pageInfo", pageInfo);
        responseMap.put("count", total);
        return responseMap;
    }

    // 获取订单
    @RequestMapping("/myOrder")  // // /orders/myOrder
    public Map myOrder (HttpSession session){
        Map map = new HashMap();
        // 1. 从session 中拿去 mid 的值 ?   // session.setAttribute("master1",master1);
        Master master1 = (Master) session.getAttribute("master1");
        Integer mid = master1.getId();
        System.out.println("mid = " + mid);
        // 2. 根据 mid 查询orders 这个表(类)
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andMidEqualTo(mid);
        List<Orders> orders = ordersService.selectByExample(example);
        map.put("code",0);
        map.put("msg","查询成功");
        map.put("data",orders);
        System.out.println("orders = " + orders);
        return map;
    }

    @RequestMapping("/findAllOrders")  // // /orders/findAllOrders
    public Map findAllOrders (HttpSession session){
        Map map = new HashMap();
        Master master1 = (Master)session.getAttribute("master1");
        Integer id = master1.getId();
        List<Orders> allOrders = ordersService.findAllOrders(id,"0");
        map.put("code",0);
        map.put("msg","查询成功");
        map.put("data",allOrders);
        return map;
    }
    @RequestMapping("/inOrders")       // /orders/inOrders
    @ResponseBody
    public Map inOrders (Orders orders){
        Map map = new HashMap();
        int i = ordersService.insertSelective(orders);
        if (i>0){
            map.put("code",0);
            map.put("msg","新增成功");
            map.put("data","");
            return map;
        }else {
            map.put("code",0);
            map.put("msg","新增失败");
            map.put("data","");
            return map;
        }
    }
    @RequestMapping("uptOrders")    // /orders/uptOrders
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
            map.put("msg", "修改成功");
            return map;
        } else {
            map.put("code", 500);
            map.put("data", "");
            map.put("msg", "修改失败");
            return map;
        }
    }
    // 删
// 删除订单  根据 主键 id 删除
    @RequestMapping("/deleteById")      // /orders/deleteById
    public Map deleteById(@RequestParam(value = "id") Integer id) {
        Map responseMap = new HashMap();
        int i = ordersService.delectOrders(id);
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

    //orders  id   查
    @RequestMapping("/selectOrederById")     //       /orders/selectOrederById
    @ResponseBody
    public Map selectOrederById(HttpSession session) {
        int id = (int)session.getAttribute("id");
        Map map=new HashMap();
        System.out.println("id = " + id);
        System.out.println(" = ================================================================");
        Orders orders = ordersService.selectfindAllOrdersBid(id);
        System.out.println("orders = " + orders.toString());
        System.out.println(" = ================================================================");
        map.put("code",0);
        map.put("msg","");
        map.put("id",orders.getId());
        map.put("phone",orders.getPhone());
        map.put("mid",orders.getMid());
        map.put("lat",orders.getLat());
        map.put("lng",orders.getLng());
        map.put("createtime",orders.getCreatetime());
        map.put("contents",orders.getContents());
        map.put("status",orders.getStatus());
        map.put("address",orders.getAddress());
        map.put("cost",orders.getCost());
        map.put("data","哈哈哈");
        return  map;
    }
    @RequestMapping("uptMasterBid")    //          /orders/uptMasterBid
    @ResponseBody
    public Map uptMasterBid(Orders orders,HttpSession session) {
        int id = (int)session.getAttribute("id");
        Map map = new HashMap();
        OrdersExample  example1= new OrdersExample ();
        OrdersExample .Criteria criteria = example1.createCriteria();
        criteria.andIdEqualTo(id);
//        int i = masterService.updateByPrimaryKeySelective(master);
        int i = ordersService.updateByExampleSelective(orders, example1);
        System.out.println("i = " + i);
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
}
