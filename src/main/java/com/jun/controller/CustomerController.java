package com.jun.controller;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jun.alyunsms.SMSUtils;
import com.jun.alyunsms.ValidateCodeUtils;
import com.jun.bean.Customer;
import com.jun.bean.CustomerExample;
import com.jun.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/Main")
    @ResponseBody
    public Map main(String phone, HttpSession session){
        System.out.println("phone = " + phone);
        Map map=null;
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            SMSUtils.sendShortMessage(phone,code.toString());
//            Jedis jedis = JedisUtil.conn("172.0.0.1", 6379);
            jedisPool.getResource().setex(phone,3600,code.toString());// 村到redis 里去了...
//            jedis.setex(phone,600000,code.toString());
//            JedisUtil.putKey(phone,code.toString());
            map=new HashMap();
            map.put("phone",code.toString());
            map.put("code",0);
            map.put("msg","验证码发送成功");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return map;
    }
    @RequestMapping("/login1")
    @ResponseBody
    public String login(String phone, String code, HttpSession session , Customer customer){
//        JedisUtil.conn("127.0.0.1", 6379);
//        String s = JedisUtil.gettKey(phone);
        String s = jedisPool.getResource().get(phone); // redis 中的验证码
        if (s.equals(code)){
            System.out.println("输入正确");
                new Thread(){
                @Override
                public void run() {
                    //数据库查询这个号码    找到了是我的会员直接登录
                    System.out.println("查");
                    System.out.println("phone = " + phone);
                    List<Customer> customers = customerService.selectByExample(null);
                    System.out.println("查2");
                    System.out.println("customers = " + customers);
//                    if (byphone==null){
//                        System.out.println("没查到添加");
//                      int  i=  customerService.add(phone);
//                        System.out.println("i = " + i);
//                    }
                }
            }.start();
        }else {
            System.out.println("输入错误");
            return "error";
        }
        session.setAttribute("phone",phone);
        //登录成功    继续访问后面的资源
        return "/res/html/phone/index1.html";
    }
    // 查-- 查询+自身对象的查询 + 分页
// 分页查询
    @RequestMapping("/selectCustomer")  // /login/selectCustomer
    @ResponseBody
    public Map selectAllByPage(Customer  customer, @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
                               @RequestParam(value = "limit", required = true) Integer pageSize) {
        // 调用service 层   , 适用于 单表!!!
        PageHelper.startPage(page, pageSize); // 开启 第几页,  每页 条数
        CustomerExample  example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        if (null!=customer.getPhone()&&!"".equals(customer.getPhone())){
            criteria.andPhoneEqualTo(customer.getPhone());   // 1
        }
        if (null!=customer.getName()&&!"".equals(customer.getName())){
            criteria.andNameEqualTo(customer.getName());   // 2
        }
//        if (null!=orders.getContents()&&!"".equals(orders.getContents())){
//            criteria.andContentsEqualTo(orders.getContents());   // 3
//        }
//        if (null!=orders.getStatus()&&!"".equals(orders.getStatus())){
//            criteria.andStatusEqualTo(orders.getStatus());   // 4
//        }
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        customer.setCreatetime(sdf.format(new Date()));
        List<Customer> customerList = customerService.selectByExample(example);
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


    // 修改订单
    @RequestMapping("/update")     // /login/update
    public Map update(@RequestBody Customer  customer) {
        Map map = new HashMap();
        int i = customerService.updateByPrimaryKeySelective( customer);
        if (i > 0) {
            map.put("code", 200);
            map.put("msg", "修改成功");
            return map;
        } else {
            map.put("code", 400);
            map.put("msg", "修改失败,检查网络再来一次");
            return map;
        }
    }

    // 删
// 删除订单  根据 主键 id 删除
    @RequestMapping("/deleteById")     // /login/deleteById
    public Map deleteById(@RequestParam(value = "id") Integer id) {
        Map responseMap = new HashMap();
        int i = customerService.deleteByPrimaryKey(id);
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
    @RequestMapping("/inMaster")       //          /login/inMaster
    @ResponseBody
    public Map inMaster (Customer  customer){
        Map map = new HashMap();
        int i = customerService.insertSelective(customer);
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
    // 修
// 修改订单  根据 主键 id 修改
    @RequestMapping("/upCustomerById")     //             /login/upCustomerById
    @ResponseBody
    public Map upCustomerById(HttpSession session, Customer customer) {
        int id = (int)session.getAttribute("id");
        System.out.println("id = " + id);
        Map map = new HashMap();
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        int i = customerService.updateByExampleSelective(customer,example);
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
    @RequestMapping("/selectCustomerById")     //    /login/selectCustomerById
    @ResponseBody
    public Map selectCustomerById(HttpSession session) {
        int id = (int)session.getAttribute("id");
        Map map=new HashMap();
        System.out.println("id = " + id);
        System.out.println(" = ================================================================");
        session.setAttribute("id",id);
        Customer master = customerService.selectByPrimaryKey(id);
        System.out.println("master = " + master.toString());
        session.setAttribute("master",master);
        map.put("code",0);
        map.put("msg","");
        map.put("id",master.getId());
        map.put("name",master.getName());
        map.put("sex",master.getSex());
        map.put("createtime",master.getCreatetime());
        map.put("phone",master.getPhone());
        map.put("idcard",master.getIdcard());
        map.put("car",master.getCar());
        map.put("data","哈哈哈");
        return  map;
    }
}
