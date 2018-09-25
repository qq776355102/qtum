package com.yunlian.cmc.exceptionhandler.output.mapper;

import com.yunlian.cmc.exceptionhandler.output.model.po.Exceptionrec;

public interface ExceptionrecMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Exceptionrec record);

    int insertSelective(Exceptionrec record);

    Exceptionrec selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Exceptionrec record);

    int updateByPrimaryKey(Exceptionrec record);
}