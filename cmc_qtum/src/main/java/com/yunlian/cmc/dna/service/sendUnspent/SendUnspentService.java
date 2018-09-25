package com.yunlian.cmc.dna.service.sendUnspent;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.http.Qtumrpc;
import com.yunlian.cmc.dna.service.account.AccountService;
import com.yunlian.cmc.dna.service.unspent.UnspentService;

import net.sf.json.JSONObject;

@Component
public class SendUnspentService {

	@Autowired
	private UnspentService unspentService;
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private Qtumrpc qtumrpc;

	public String sendFromAccount(String fromaccount, String toaddress, BigDecimal amount, Integer minconf, String comment,
			String comment_to) throws DnaDebugException {
		//锁定其他账户未使用交易
		boolean lockOtherUnspend = this.lockOtherUnspend(fromaccount);
		if (!lockOtherUnspend) {
			throw new DnaDebugException("锁定其它未使用交易失败");
		}
		
		Object[] str = { fromaccount, toaddress, amount, minconf, comment, comment_to };

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "sendfrom");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String sendPostWithJson = qtumrpc.sendHttpPost(jsonObject.toString());
		return sendPostWithJson;
	}
	
	
	

	/**
	 * 从指定地址发送资金
	 * @param fromaddress
	 * @param toaddress
	 * @param amount
	 * @param minconf
	 * @param comment
	 * @param comment_to
	 * @return
	 * @throws DnaDebugException
	 */
	public String sendFromAddress(String fromaddress, String toaddress, BigDecimal amount, Integer minconf, String comment,
			String comment_to) throws DnaDebugException {
		//根据地址获取账户
		String account = accountService.getaccount(fromaddress);
		String fromaccount = JSONObject.fromObject(account).getString("result");
		if (fromaccount.equals("null")) {
			throw new DnaDebugException("发送地址不存在");
		}
		return this.sendFromAccount(fromaccount, toaddress, amount, minconf, comment, comment_to);
	}

	

	/**
	 * 
	 * 对多个地址发送资产
	 * 		可以指定扣除旷工费的地址
	 * 
	 * @param fromaddress
	 *            发送者地址
	 * @param toaddressAndAmountJson
	 *            接收者 ,带地址和金额的json对象 {"地址1":amount1(注:金额),"地址2":amount2....}
	 * @param amount
	 *            金额
	 * @param subtractfeefrom
	 *            扣除旷工费的地址
	 * @return
	 * @throws DnaDebugException
	 * 
	 *             demo1 小红有10个qtum,要发送给小绿2个qtum和小明8个qtum,旷工费从小明账户扣除
	 *             "fromaddress" (注:写小红地址) toaddressAndAmountJson
	 *             {"小绿地址":2,"小明地址":8} subtractfeefrom "小明地址"(注:旷工费从小明地址扣除)
	 *            
	 * 
	 */
	public String sendFromAddress(String fromaddress, String toaddressAndAmountJson, String subtractfeefrom,
			String comment) throws DnaDebugException {
		String accountString = accountService.getaccount(fromaddress);
		String fromaccount = JSONObject.fromObject(accountString).getString("result");
		if (fromaccount.equals("null")) {
			throw new DnaDebugException("发送地址不存在");
		}
		String[] subtractfeefromes = { subtractfeefrom };
		String rsString = this.sendFromAddressesToAddresses(fromaccount, toaddressAndAmountJson, "1", comment,
				subtractfeefromes);
		return rsString;

	}

	/**
	 * 从多个地址发送资产并到多个指定地址,并指定旷工费扣除地址,如果没有指定地址，发件人支付费用
	 * 
	 * @param fromaccount
	 *            发送者账户
	 * @param amountsJson
	 *            接收者 ,带地址和金额的json对象 {"地址1":amount1(注:金额),"地址2":amount2....}
	 * @param minconf
	 *            确认(默认为1)
	 * @param comment
	 *            评论
	 * @param subtractfeefrom
	 *            扣除旷工费的地址数组 ["地址"....]
	 * @return
	 * @throws DnaDebugException 
	 * 
	 * 
	 * @type 模型:账户-[地址...] account[address...]
	 * 
	 */
	public String sendFromAddressesToAddresses(String fromaccount, String amountsJson, String minconf, String comment,
			String[] subtractfeefrom) throws DnaDebugException {
		// 锁定其他账户未使用交易
		boolean lockOtherUnspend = this.lockOtherUnspend(fromaccount);
		if (!lockOtherUnspend) {
			throw new DnaDebugException("锁定其它未使用交易失败");
		}
		Object[] str = { fromaccount, amountsJson, minconf, comment, subtractfeefrom };
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "sendmany");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rsString = qtumrpc.sendHttpPost(jsonObject.toString());
		return rsString;

	}
	
	public String sendFromAddress(String fromaddress, String toaddress, BigDecimal amount) throws DnaDebugException {
		return this.sendFromAddress(fromaddress, toaddress, amount, 1, "", "");
	}
	
	/**
	 * 将钱包交易<txid>标记为废弃 这将标记此交易及其所有的钱包后代被放弃，这将允许 因为他们的投入要被重新获得。
	 * 它可以用来代替“卡住”或被驱逐的交易。 它仅适用于未包含在块中并且当前不在mempool中的交易。 它对已经发生冲突或放弃的交易没有影响。
	 * 
	 * @param txid
	 * @return
	 */
	public String abandontransaction(String txid) {
		Object[] str = { txid };
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "abandontransaction");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = qtumrpc.sendHttpPost(jsonObject.toString());
		return rString;
	}
	
	
	
	/**
	 * 锁定其他账户为使用交易
	 * @param account
	 * @return
	 * @throws DnaDebugException
	 */
	public boolean lockOtherUnspend(String account) throws DnaDebugException {
		boolean b = true;
		String listaddressesbyaccount = accountService.getaddressesbyaccount(account);
		if (JSONObject.fromObject(listaddressesbyaccount).getJSONArray("result").toString().equals("[]")) {
			throw new DnaDebugException("此账户不存在");
		}
		for (Object object : JSONObject.fromObject(listaddressesbyaccount).getJSONArray("result")) {
			boolean lockOtherAcountUnpent = unspentService.lockOtherAcountUnpent(String.valueOf(object));
			b = lockOtherAcountUnpent;
		}
		return b;
	}

}
