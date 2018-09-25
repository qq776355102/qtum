package com.yunlian.cmc.exceptionhandler.output.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunlian.cmc.exceptionhandler.output.mapper.ExceptionrecMapper;
import com.yunlian.cmc.exceptionhandler.output.model.po.Exceptionrec;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ExceptionrecService {

	@Autowired
	private ExceptionrecMapper mapper;
	
	@Async
	public void insertSelective(Exceptionrec exceptionrec) {
		mapper.insertSelective(exceptionrec);
	}
}
