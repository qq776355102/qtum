package com.yunlian.cmc.dna.qtumutils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.http.Qtumrpc;

import net.sf.json.JSONObject;

/**
 * @author mc
 * 地址转换工具
 *
 */
@Component
public class HexAdressUtil {

	@Autowired
	private Qtumrpc httpPost;

	/**
	 * 将base58 pubkeyhash地址转换为十六进制地址以用于智能合约。
	 * @param address
	 * @return 十六进制地址
	 * @throws DnaDebugException 
	 */
	
	public String gethexaddress(String address) throws DnaDebugException {
		Object[] str = {address};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "gethexaddress");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		JSONObject fromObject = JSONObject.fromObject(rString);
		String hexAddress = fromObject.get("result").toString();
		if (StringUtils.equals(hexAddress, "null")) {
			throw new DnaDebugException("Invalid Qtum address");
		}
		return hexAddress;
	}
	
	/**
	 * 将原始的十六进制地址转换为base58 pubkeyhash地址
	 * @param hexaddress
	 * @return base58 pubkeyhash地址
	 */
	public String fromhexaddress(String hexaddress) {
		Object[] str = {hexaddress};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "fromhexaddress");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
		
	}

}
