package com.jun.controller;

import com.jun.bean.Customerservice;
import com.jun.bean.CustomerserviceExample;
import com.jun.service.CustomerserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/login")
public class CustomerserviceController {
    @Autowired
    private CustomerserviceService customerserviceService;
    @RequestMapping("/loginon")
    public String loginon(Customerservice customerservice, HttpSession session){
        CustomerserviceExample cu=new CustomerserviceExample();
        CustomerserviceExample.Criteria criteria = cu.createCriteria();
        criteria.andAccountEqualTo(customerservice.getAccount());
        criteria.andPasswordEqualTo(customerservice.getPassword());
        List<Customerservice> customerservices = customerserviceService.selectByExample(cu);
        if (customerservices.size()==1){
            return "pc/index1";
        }else {
            return "manager";
        }
    }
}
