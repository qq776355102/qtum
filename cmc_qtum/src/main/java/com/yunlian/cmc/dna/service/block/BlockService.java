package com.yunlian.cmc.dna.service.block;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.http.Qtumrpc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class BlockService {

	@Autowired
	private Qtumrpc httpPost;

	/**
	 * 获得最新块高
	 * @return
	 */
	public String getLastBlockCount() {
		Object[] str = {};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "getblockcount");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}
	
	/**
	 * 根据块高获取hash
	 * @return
	 */
	public String getblockhash (String height) {
		Object[] str = {new BigInteger(height)};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "getblockhash");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}
	
	/**
	 * 根据块hash获取所有txid
	 * @param blockHash
	 * @return 返回txid数组
	 */
	public JSONArray getblock(String blockHash) {
		Object[] str = {blockHash};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "getblock");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		JSONObject resultJson = JSONObject.fromObject(rString).getJSONObject("result");
		JSONArray txids = resultJson.getJSONArray("tx");
		return txids;
	}
	
	/**
	 * 根据blockHash获取块高
	 * @param blockHash
	 * @return height
	 */
	public String getblockheight(String blockHash) {
		Object[] str = {blockHash};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "getblockheader");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		String height = JSONObject.fromObject(rString).getJSONObject("result").get("height").toString();
		return height;
	}
	
	
	
	
}
