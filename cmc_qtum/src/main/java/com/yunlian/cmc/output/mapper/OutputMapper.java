package com.yunlian.cmc.output.mapper;

import java.util.List;
import java.util.Map;

import com.yunlian.cmc.output.model.po.Output;

public interface OutputMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Output record);
    
    void insertBatch(List<Output> outputs);

    int insertSelective(Output record);

    Output selectByPrimaryKey(Integer id);
    
    List<Output> selectBySelective(Map<String, Object> param);

    int updateByPrimaryKeySelective(Output record);

    int updateByPrimaryKey(Output record);
}