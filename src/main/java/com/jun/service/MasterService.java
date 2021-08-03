package com.jun.service;


import com.jun.bean.Master;
import com.jun.bean.MasterExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MasterService {

    long countByExample(MasterExample example);   // 查总条数 动态

    int deleteByExample(MasterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Master record);

    int insertSelective(Master record);

    List<Master> selectByExample(MasterExample example);  // 查询...

    Master selectByPrimaryKey(Integer id);
  
    int updateByExampleSelective(@Param("record") Master record, @Param("example") MasterExample example);

    int updateByExample(@Param("record") Master record, @Param("example") MasterExample example);

    int updateByPrimaryKeySelective(Master record); // 修改 按逐渐 id

    int updateByPrimaryKey(Master record);

}
