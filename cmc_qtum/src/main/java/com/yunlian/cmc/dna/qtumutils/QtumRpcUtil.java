package com.yunlian.cmc.dna.qtumutils;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.service.account.AccountService;
import com.yunlian.cmc.dna.service.sendUnspent.SendUnspentService;
import com.yunlian.cmc.dna.service.transaction.QtumTransactionService;
import com.yunlian.cmc.dna.service.unspent.UnspentService;
import com.yunlian.cmc.dna.service.wallet.QtumWalletService;

public class QtumRpcUtil {

	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UnspentService unspentService;
	
	@Autowired
	private QtumTransactionService transactionService;
	
	@Autowired
	private SendUnspentService sendUnspentService;
	
	@Autowired
	private QtumWalletService qtumWalletService;
	
	/**
	 * 根据账户获取地址列表
	 * @param account
	 * @return
	 */
	public String getaddressesbyaccount(String account) {
		return  accountService.getaddressesbyaccount(account);
	}
	
	/**
	 * 根据账户获取一个新的地址
	 * @param account
	 * @return
	 * @throws DnaDebugException
	 */
	public String getnewaddress(String account) throws DnaDebugException {
		return accountService.getnewaddress(account);
	}
	
	
	/**
	 * 返回关于给定的qtum地址的信息。
	 * @param address
	 * @return
	 */
	public String validateaddress(String address) {
		return accountService.validateaddress(address);
	}
	
	

	/**
	 * qtum发送交易 
	 *   1对1发送交易
	 *   旷工费从发送者地址扣除
	 * @param fromaddress 发送地址
	 * @param toaddress 接收地址
	 * @param amount 发送数量
	 * @return
	 * @throws Exception
	 */
	public String sendFromAddress(String fromaddress,String toaddress,BigDecimal amount) throws Exception {
		return sendUnspentService.sendFromAddress(fromaddress, toaddress, amount);
	}
	
	
	/**
	 * qtum发送交易
	 *   1对多
	 *   并指定扣除旷工费地址
	 * 
	 * @param fromaddress
	 * @param toaddressAndAmountJson
	 * @param subtractfeefrom
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public String sendFromAddress(String fromaddress, String toaddressAndAmountJson, String subtractfeefrom,
			String comment) throws Exception {
		return sendUnspentService.sendFromAddress(fromaddress, toaddressAndAmountJson, subtractfeefrom,comment);
	}
	
	/**
	 * 导出私钥
	 * @param address
	 * @return
	 */
	public String dumpprivkey(String address) {
		return  accountService.dumpprivkey(address);
	}
	
	
	/**
	 * 返回指定地址可用余额
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public String getTotalAvailableBalance(String address) throws Exception {
		return unspentService.getTotalAvailableBalance(address);
	}
	
	/**
	 * 获取交易记录
	 * @param address
	 * @return
	 * @throws DnaDebugException
	 */
	public String listTransactionsContainFromaddressByAddress(String address) throws DnaDebugException{
		return transactionService.listTransactionsContainFromaddressByAddress(address);
	}
	
	
	/**
	 * @return 获取钱包中的所有账户
	 */
	public String listAccounts(){
		return accountService.listAccounts();
	}
	
	/**
	 * @return 获取帮助
	 */
	public String help(String command){
		return qtumWalletService.help(command);
	}
	
	/**
	 * @param address 地址
	 * @return 账户信息
	 */
	public String getAccountByaddress(String address){
		return	accountService.getaccount(address);
	}
	
	/**
	 * 已过时。 设置与给定地址关联的帐户。
	 * @param address
	 * @param lable
	 * @return
	 */
	public String setAccount(String address,String account){
		return accountService.setAccount(address, account);
	}
	
	
	/**
	 * 
	 * @param size (默认100)新的密钥池
	 * @return
	 */
	public String setKeyPoolRefill(String size) {
		return qtumWalletService.setKeyPoolRefill(size);
	}
}
