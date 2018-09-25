package com.yunlian.cmc.dna.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.http.Qtumrpc;

import net.sf.json.JSONObject;

@Component
public class AccountService {

	@Autowired
	private Qtumrpc qtumrpc;

	/**
	 * 
	 * 根据账户获取对应的地址列表
	 * 
	 * @param account
	 * @return address
	 * @throws Exception
	 */
	public String getaddressesbyaccount(String account) {
		Object[] str = new Object[] { account };
		JSONObject json = new JSONObject();
		json.put("method", "getaddressesbyaccount");
		json.put("params", str);
		json.put("id", Object.class);
		String sendPostWithJson = qtumrpc.sendHttpPost(json.toString());
		return sendPostWithJson;
	}

	/**
	 * 根据账户生成地址
	 * 
	 * @param account
	 *            账户
	 * @param fixed
	 *            Boolean 默认为false,一个账户对应一个地址,设置为true,则可以创建多个地址
	 * @return 新创建的地址
	 * @throws DnaDebugException
	 */
	public String getnewaddress(String account, boolean fixed) throws DnaDebugException {
		if (fixed == false) {
			String getaddressesbyaccount = this.getaddressesbyaccount(account);
			String result = JSONObject.fromObject(getaddressesbyaccount).getString("result");
			if (!result.equals("[]")) {
				throw new DnaDebugException("该账户已存在");
			}
		}
		Object[] str = new Object[] { account };
		JSONObject json = new JSONObject();
		json.put("method", "getnewaddress");
		json.put("params", str);
		json.put("id", Object.class);
		String sendPostWithJson = qtumrpc.sendHttpPost(json.toString());
		return sendPostWithJson;
	}

	/**
	 * 根据账户生成地址
	 * 
	 * @param account
	 * @return 新创建的地址
	 * @throws DnaDebugException
	 */
	public String getnewaddress(String account) throws DnaDebugException {
		return this.getnewaddress(account, false);
	}

	/**
	 * 根据已存在地址,获取账户信息
	 * 
	 * @param address
	 * @return account
	 */
	public String getaccount(String address) {
		Object[] str = new Object[] { address };
		JSONObject json = new JSONObject();
		json.put("method", "getaccount");
		json.put("params", str);
		json.put("id", Object.class);
		String sendPostWithJson = qtumrpc.sendHttpPost(json.toString());
		return sendPostWithJson;
	}

	/**
	 * 验证地址“地址” 返回关于给定的qtum地址的信息。
	 * 
	 * @param address
	 * @return
	 */
	public String validateaddress(String address) {
		Object[] str = new Object[] { address };
		JSONObject json = new JSONObject();
		json.put("method", "validateaddress");
		json.put("params", str);
		json.put("id", Object.class);
		String sendPostWithJson = qtumrpc.sendHttpPost(json.toString());
		return sendPostWithJson;
	}

	/**
	 * 导出地址对应的私钥
	 * 
	 * @param address
	 * @return privatekey
	 */
	public String dumpprivkey(String address) {
		Object[] str = new Object[] { address };
		JSONObject json = new JSONObject();
		json.put("method", "dumpprivkey");
		json.put("params", str);
		json.put("id", Object.class);
		String sendPostWithJson = qtumrpc.sendHttpPost(json.toString());
		return sendPostWithJson;
	}

	/**
	 * 返回base64地址
	 * 
	 * @param hexAddress
	 * @return
	 */
	public String fromhexaddress(String hexAddress) {
		Object[] str = { hexAddress };
		JSONObject json = new JSONObject();
		json.put("method", "fromhexaddress");
		json.put("params", str);
		json.put("id", Object.class);
		String sendPostWithJson = qtumrpc.sendHttpPost(json.toString());
		return sendPostWithJson;
	}

	/**
	 * @return 获取钱包中的所有账户
	 */
	public String listAccounts() {
		Object[] str = {};
		JSONObject json = new JSONObject();
		json.put("method", "listaccounts");
		json.put("params", str);
		json.put("id", Object.class);
		return qtumrpc.sendHttpPost(json.toString());
	}

	/**
	 * 已过时。 设置与给定地址关联的帐户。
	 * 
	 * @param address
	 *            地址
	 * @param account
	 *            账户
	 * @return
	 */
	public String setAccount(String address, String account) {
		Object[] str = { address, account };
		JSONObject json = new JSONObject();
		json.put("method", "setaccount");
		json.put("params", str);
		json.put("id", Object.class);
		return qtumrpc.sendHttpPost(json.toString());
	}

}
