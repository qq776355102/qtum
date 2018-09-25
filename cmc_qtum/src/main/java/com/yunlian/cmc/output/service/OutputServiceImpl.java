package com.yunlian.cmc.output.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.yunlian.cmc.output.mapper.OutputMapper;
import com.yunlian.cmc.output.model.po.Output;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OutputServiceImpl {

	@Autowired
	private OutputMapper mapper;
	
	/**
	 * 插入未花费的交易输出
	 * @param output
	 */
	@Async
	public void insertOutputOfUnspent(List<Output> outputs) {
		for (Output output : outputs) {
			Map<String, Object> param = new HashMap<>();
			param.put("txid", output.getTxid());
			param.put("vout", output.getVout());
			List<Output> list = mapper.selectBySelective(param);
			if (list != null && list.size() > 0) 
				continue;
			mapper.insertSelective(output);
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })  
	public void test() {  
	   try {  
//	      doDbStuff1();  
//	      doDbStuff2();  
	   } catch (Exception e) {  
	        e.printStackTrace();     
	        TransactionAspectSupport.currentTransactionStatus()
	        .setRollbackOnly();
	    //就是这一句了，加上之后，如果doDbStuff2()抛了异常,
	    //doDbStuff1()是会回滚的  
	   }  
	}
	
}
