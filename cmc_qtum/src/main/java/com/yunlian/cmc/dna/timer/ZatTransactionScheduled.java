package com.yunlian.cmc.dna.timer;

import java.math.BigInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.yunlian.cmc.dna.service.block.proxy.BlockProxyService;
import com.yunlian.cmc.dna.service.transaction.QtumTransactionService;

@Controller
public class ZatTransactionScheduled {

	private static final Logger logger = LoggerFactory.getLogger(ZatTransactionScheduled.class);

	@Autowired
	private BlockProxyService blockProxyService;

	@Autowired
	private QtumTransactionService qtumTransactionService;

	// 每15秒固定速率获取最新块高,并延迟一分钟执行秒执行  fixedRate = 15000, initialDelay = 180000
//	@Scheduled(fixedRate = 15000, initialDelay = 60000)
	public void ContactQtumBlockScheduled() throws InterruptedException {

		try {
			blockProxyService.contactConn();
		} catch (Exception e) {
			e.printStackTrace();
			// 异常处理
			logger.debug("定时任务异常了");
		}

	}

	// 系统同步块的速度 fixedDelay = 30000, initialDelay = 180000
//	@Scheduled(fixedDelay = 15000, initialDelay = 60000)
	public void sycnBlockRate() throws InterruptedException {
		// 以每小于等于5个块的速度同步
		int i = 5;
		BigInteger subtract = QtumTransactionHolder.getLAST_BLOCK().subtract(QtumTransactionHolder.getFINISHED_BLOCK());
		BigInteger finishedBlock = QtumTransactionHolder.getFINISHED_BLOCK();
		if (subtract.compareTo(new BigInteger(String.valueOf(i))) > 0) {
			do {
				QtumTransactionHolder.put(finishedBlock.toString());
				finishedBlock = finishedBlock.add(BigInteger.ONE);
				i--;
			} while (i > 0);
		} else {
			BigInteger subtract2 = null;
			do {
				QtumTransactionHolder.put(finishedBlock.add(BigInteger.ONE).toString());
				finishedBlock = finishedBlock.add(BigInteger.ONE);
				subtract2 = QtumTransactionHolder.getLAST_BLOCK().subtract(finishedBlock);
			} while (subtract2.compareTo(BigInteger.ONE) >= 0);
		}
	}

//	@PostConstruct
	public void transactionSycnIniti() throws InterruptedException {
		QtumTransactionHolder.put(QtumTransactionHolder.getINIT_BLOCK().toString());
		qtumTransactionService.syncQtumBlock();
	}

}
