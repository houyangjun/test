package com.jun.service;

import com.jun.bean.Orders;
import com.jun.bean.OrdersExample;
import com.jun.dao.OrdersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("OrdersService")
public class OrdersServiceImpl implements OrdersService {
    @Autowired(required=false)
    private OrdersDAO ordersDAO;

    @Override
    public List<Orders> findAll(int pageSize, int pageCode) {
        return ordersDAO.findAll(pageSize,pageCode);
    }

    @Override
    public int addOrders(Orders orders) {
        return ordersDAO.addOrders(orders);
    }

    @Override
    public int count() {
        return ordersDAO.count();
    }

    @Override
    public List<Orders> selectByOrders(int pageSize, int pageCode) {
        return ordersDAO.selectByOrders(pageSize,pageCode);
    }

    @Override
    public int UpdateOrders(int id, String status) {
        return ordersDAO.UpdateOrders(id,status);
    }

    @Override
    public List<Orders> selectOrdersByIfs(String status, String phone) {
        return ordersDAO.selectOrdersByIfs(status,phone);
    }

    @Override
    public List<Orders> selectByExample(OrdersExample example) {
        return ordersDAO.selectByExample(example);
    }

    @Override
    public List<Orders> findAllOrders(int mid, String status) {
        return ordersDAO.findAllOrders(mid,status);
    }

    @Override
    public int UPAllOrders(int id, int mid) {
        return ordersDAO.UPAllOrders(id,mid);
    }

    @Override
    public int UPAllOrdersStrtus(int id, int status) {
        return ordersDAO.UPAllOrdersStrtus(id,status);
    }

    @Override
    public int insertSelective(Orders orders) {
        return ordersDAO.insertSelective(orders);
    }

    @Override
    public int delectOrders(int id) {
        return ordersDAO.delectOrders(id);
    }

    @Override
    public Orders selectfindAllOrdersBid(int id) {
        return ordersDAO.selectfindAllOrdersBid(id);
    }

    @Override
    public int updateByExampleSelective(Orders record, OrdersExample example) {
        return ordersDAO.updateByExampleSelective(record,example);
    }


}
