package com.yunlian.cmc.dna.service.block.proxy;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.service.block.BlockService;
import com.yunlian.cmc.dna.timer.QtumTransactionHolder;

import net.sf.json.JSONObject;

@Component
public class BlockProxyService {

	@Autowired
	private BlockService blockService;

	private final ExecutorService executor = Executors.newFixedThreadPool(5);

	//同步链上块高
	@Async
	public void contactConn() throws DnaDebugException, InterruptedException {
		// 链上最新块高
		BigInteger lastBlockCount = this.getLastBlockCountOfAsync();
		
		QtumTransactionHolder.setLAST_BLOCK(lastBlockCount);
		
	
		System.out.println("获取最新块高的线程名:********"+Thread.currentThread().getName()+"**********");
		System.out.println("链上最新块高:********"+lastBlockCount+"**********");
		
	}
	


	/**
	 * 获得最新块高
	 * 
	 * @return
	 */
	public BigInteger getLastBlockCountOfAsync() {
		
		Callable<String> callable = new Callable<String>() {

			@Override
			public String call() throws Exception {
				String lastBlockCount = blockService.getLastBlockCount();
				return lastBlockCount;
			}
		};
		Future<String> submit = executor.submit(callable);
		String rString = null;
		try {
			rString = submit.get(20, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			//同步链上块高异常
			// TODO Auto-generated catch block
		}
		BigInteger bigInteger = new BigInteger(JSONObject.fromObject(rString).getString("result"));
		return bigInteger;
	}
	
	
	

}
