package com.yunlian.cmc.dna.service.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.http.Qtumrpc;

import net.sf.json.JSONObject;

@Component
public class QtumWalletService {

	@Autowired
	private Qtumrpc qtumrpc;
	
	
	/**
	 * 填充密钥池。
	 * @param size （默认值= 100）新的keypool大小
	 * @return
	 */
	public  String setKeyPoolRefill(String size){
		Object[] str = {size};
		JSONObject json = new JSONObject();
		json.put("method", "keypoolrefill");
		json.put("params", str);
		json.put("id", Object.class);
		String resultString = qtumrpc.sendHttpPost(json.toString());
		return resultString;
	}
	
	
	/**
	 * 	 获取帮助
	 * @param command qtumcli指令
	 * @return
	 */
	public String help(String command){
		Object[] str = {command};
		JSONObject json = new JSONObject();
		json.put("method", "help");
		json.put("params", str);
		json.put("id", Object.class);
		return qtumrpc.sendHttpPost(json.toString());
	}
	

}
