package com.jun.dao;

import com.jun.bean.Orders;
import com.jun.bean.OrdersExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersDAO {
 public  List<Orders> findAll( int pageSize, int pageCode);
 public int addOrders(Orders orders);
 public  int count();
 public List<Orders> selectByOrders(@Param("pageSize") int pageSize, @Param("pageCode") int pageCode);


 public  List<Orders> selectOrdersByIfs(@Param("status") String status, @Param("phone") String phone);

  int  UpdateOrders(@Param("id") int id,@Param("status") String status);

    List<Orders> selectByExample(OrdersExample example);


 List<Orders>  findAllOrders(@Param("mid") int mid,@Param("status") String status);

 int  UPAllOrders(@Param("id") int id,@Param("mid") int  mid);


 int  UPAllOrdersStrtus(@Param("id") int id,@Param("status") int  status);

 int insertSelective(Orders orders);

 int delectOrders(int  id);


 Orders selectfindAllOrdersBid(int  id);

 int updateByExampleSelective(@Param("record") Orders  record, @Param("example") OrdersExample  example);
}