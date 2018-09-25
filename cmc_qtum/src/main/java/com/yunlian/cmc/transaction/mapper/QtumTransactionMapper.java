package com.yunlian.cmc.transaction.mapper;

import java.util.Map;

import com.yunlian.cmc.transaction.model.po.QtumTransaction;

public interface QtumTransactionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QtumTransaction record);

    int insertSelective(QtumTransaction record);

    QtumTransaction selectByPrimaryKey(Integer id);
    
    QtumTransaction selectBySelective(Map<String, Object> param);

    int updateByPrimaryKeySelective(QtumTransaction record);

    int updateByPrimaryKey(QtumTransaction record);
}