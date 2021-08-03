package com.jun.dao;


import com.jun.bean.Master;
import com.jun.bean.MasterExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MasterDAO {
    long countByExample(MasterExample example);

    int deleteByExample(MasterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Master record);

    int insertSelective(Master record);

    List<Master> selectByExample(MasterExample example);

    Master selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Master record, @Param("example") MasterExample example);

    int updateByExample(@Param("record") Master record, @Param("example") MasterExample example);

    int updateByPrimaryKeySelective(Master record);

    int updateByPrimaryKey(Master record);

    int UPAllOMaster(Master record);

}