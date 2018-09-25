package com.yunlian.cmc.dna.qtumutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.service.account.AccountService;
import com.yunlian.cmc.dna.service.contract.QtumContract;
import com.yunlian.cmc.dna.service.unspent.UnspentService;

/**
 * @author mc
 * 
 * ***********  ZAT   *********
 * FUNCTIONHASHES
 * {
	"dd62ed3e": "allowance(address,address)",
	"095ea7b3": "approve(address,uint256)",
	"70a08231": "balanceOf(address)",
	"313ce567": "decimals()",
	"06fdde03": "name()",
	"95d89b41": "symbol()",
	"18160ddd": "totalSupply()",
	"a9059cbb": "transfer(address,uint256)",
	"23b872dd": "transferFrom(address,address,uint256)"
	}
 *
 */
@Component
public class ZatRpcUtil {

	@Autowired
	private QtumContract qtumContract;
	
	@Autowired 
	private UnspentService unspentService;

	
	/**
	 * 查询代币zat可用余额
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public String balanceOf(String address) throws Exception{
		return qtumContract.balanceOfZat(address);
	}
	
	/**
	 * 
	 * 发送zat到指定地址
	 * gaslimit:250000
	 * gasprice:0.0000004
	 * 
	 * @param toAdress
	 * @param amount
	 * @param senderAdress
	 * @return
	 * @throws Exception
	 */
	public String sendToAddress(String toAdress, String amount, String senderAdress) throws Exception {
		//锁定其他账户未用输出
		boolean lockOtherAcountUnpent = unspentService.lockOtherAcountUnpent(senderAdress);
		if (!lockOtherAcountUnpent) {
			throw new DnaDebugException("锁定其他账户异常");
		}
		//发送交易
		String sendZatToAddress = qtumContract.sendZatToAddress(toAdress,amount, senderAdress);
		return sendZatToAddress;
	}
	
	/**
	 * 发送zat到指定地址
	 * gasprice:0.0000004
	 * @param toAdress
	 * @param amount
	 * @param gasLimit
	 * @param senderAdress
	 * @return
	 * @throws Exception
	 */
	public String sendToAddress(String toAdress, String amount, Integer gasLimit, String senderAdress) throws Exception {
		//锁定其他账户未用输出
		boolean lockOtherAcountUnpent = unspentService.lockOtherAcountUnpent(senderAdress);
		if (!lockOtherAcountUnpent) {
			throw new DnaDebugException("锁定其他账户异常");
		}
		//发送交易
		String sendZatToAddress = qtumContract.sendZatToAddress(toAdress,amount,gasLimit, senderAdress);
		return sendZatToAddress;
	}
	
	
	

	
}
