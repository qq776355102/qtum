package com.yunlian.cmc.dna.service.unspent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.http.Qtumrpc;
import com.yunlian.cmc.dna.model.Unspent;
import com.yunlian.cmc.output.model.po.Output;
import com.yunlian.cmc.output.service.OutputServiceImpl;

import cn.cmc.core.BeanUtils;
import cn.cmc.json.JsonPluginsUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.jsqlparser.statement.select.FromItem;

@Component
public class UnspentService {

	@Autowired
	private Qtumrpc httpPost;
	
	/**
	 * @return 返回未使用的事务输出数组
	 * @throws Exception
	 */
	public String listUnspent() {
		Object[] str = {};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "listunspent");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}
	
	public String createrawtransaction() throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("txid", "953cb032c177610320cfb4ce46b594011896e900b9f0b59f3949dfe2462b1085");
		jsonObject2.put("vout", 0);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("txid", "b185b9aebc16c66562cbfbff5ba7963d174619a296f6307bbbbe5d590a36143d");
		jsonObject3.put("vout", 2);
		jsonArray.add(jsonObject2);
		jsonArray.add(jsonObject3);
		
		JSONObject jsonObject4 = new JSONObject();
		jsonObject4.put("QjbhYPpc7GhtdaFz29MYMyvWsvB3LGfBhN", "2");
		
		JSONObject jsonObject = new JSONObject();
		
		JSONArray jsonArray2 = new JSONArray();
		jsonArray2.add(jsonArray);
		jsonArray2.add(jsonObject4);
		
		jsonObject.put("method", "createrawtransaction");
		jsonObject.put("params", jsonArray2);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}
	
	
	
	public String fundrawtransaction() throws Exception {
//		ox70a08231000000000000000000000000f6d0b03da88bdaa7a8ce62369dbda08a34a68b64
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("feeRate", 0.004);
		Object[] str = {"020000000285102b46e2df49399fb5f0b900e996180194b546ceb4cf20036177c132b03c950000000000ffffffff3d14360a595dbebb7b30f696a21946173d96a75bfffbcb6265c616bcaeb985b10200000000ffffffff01a0860100000000001976a914fc3d1035fecbcbfafef486d5b53ace0410e42fcd88ac00000000",jsonObject2};
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "callcontract");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}
	
	
	/**
	 * 返回指定地址可用余额
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public String getTotalAvailableBalance(String address) throws Exception {
		this.unLockAllAccountUnspent();
		String[] addresses = {address};
		String listUnspent = listUnspent(1,9999999,addresses);
		JSONObject fromObject = JSONObject.fromObject(listUnspent);
		JSONArray jsonArray = fromObject.getJSONArray("result");
		if (jsonArray.toString().equals("[]")) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("result", 0);
			jsonObject.put("error", null);
			jsonObject.put("id", "java.lang.Object");
			return jsonObject.toString();
		}
		BigDecimal balance = new BigDecimal(0);
		for (Object object : jsonArray) {
			String string = JSONObject.fromObject(object).getString("amount");
			balance = balance.add(new BigDecimal(string));
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", balance.toString());
		jsonObject.put("error", null);
		jsonObject.put("id", "java.lang.Object");
		return jsonObject.toString();
	}
	
	public static void main(String[] args) {
		BigDecimal balance = new BigDecimal(0);
		BigDecimal bigDecimal = new BigDecimal("2");
		BigDecimal add = balance.add(bigDecimal);
		BigDecimal bigDecimal2 = new BigDecimal("0.00033");
		BigDecimal add2 = add.add(bigDecimal2);
		System.err.println(add2.toString());
	}
	/**
	 * 
	 * @param address
	 * @return 返回地址可用未花费的交易输出
	 * @throws Exception
	 */
	public String listUnspent(String address) throws Exception {
		String[] addresses = {address};
		String listUnspent = listUnspent(1,9999999,addresses);
		return listUnspent;
	}

	/**
	 * @param minconf
	 *            （可选,默认值= 1）要过滤的最小确认数
	 * @return 返回大于minconf的确认未使用的事务输出数组
	 * @throws Exception
	 */
	public String listUnspent(Integer minconf) throws Exception {
		String[] addresses = {};
		String listUnspent = listUnspent(minconf,9999999,addresses);
		return listUnspent;
	}

	/**
	 * @param addresses
	 *            要过滤的qtum地址的json数组,
	 * @return 可选地筛选以仅包含支付给指定地址的txouts
	 * @throws Exception
	 */
	public String listUnspent(String[] addresses) throws Exception {
		String listUnspent = listUnspent(1,9999999,addresses);
		return listUnspent;
	}

	/**
	 * 
	 * @param minconf
	 *            （可选,默认值= 1）要过滤的最小确认数
	 * @param maxconf
	 *            (可选,默认= 9999999）要过滤的最大确认数
	 * @param addresses
	 *            要过滤的qtum地址的json数组,可选地筛选以仅包含支付给指定地址的txouts
	 * @return minconf和maxconf（含）之间的确认返回未使用的事务输出数组
	 * @throws Exception
	 */
	public String listUnspent(Integer minconf, Integer maxconf, String[] addresses) throws Exception {
		Object[] str = {minconf,maxconf,addresses};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "listunspent");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}

	/**
	 * @return 获取1和9999999（含）之间的确认返回未使用的事务输出数组数量
	 * @throws Exception
	 */
	public Integer getUnspentCount() throws Exception {
		String listUnspent = this.listUnspent();
		JSONObject fromObject = JSONObject.fromObject(listUnspent);
		if (fromObject != null && fromObject.containsKey("result")) {
			JSONArray array = JSONArray.fromObject(fromObject.get("result"));
			return array.size();
		}
		return -1;
	}

	/**
	 * @return 返回暂时不可输出的输出列表。
	 * @throws Exception
	 */
	public String listLockUnspent() throws Exception {
		Object[] str = {};
		JSONObject json = new JSONObject();
		json.put("method", "listlockunspent");
		json.put("params", str);
		json.put("id", Object.class);
		String result = httpPost.sendHttpPost(json.toString());
		return result;
	}

	/**
	 * 更新暂时不可输出的输出列表。
	 * 暂时锁定（unlock = false）或解锁（unlock = true）指定的事务输出。
	 * 如果在解锁时没有指定事务输出，则所有当前锁定的事务输出都将被解锁。消费qtum时，锁定的交易输出不会被自动选择硬币选择。
	 * 锁仅存储在内存中。节点以零锁定输出和锁定输出列表开始 总是在节点停止或失败时清除（凭借进程退出）。
	 * 
	 * 
	 * @param unlock
	 *            (必须 )是否解锁（true）或锁定（false）指定的事务
	 * @param Unspents
	 *            字符串，可选）一个json对象数组。每个对象的txid（字符串）vout（数字）      [（json对象的json数组）
	 *                   {          “txid”：“id”，（字符串）事务标识
	 *                     “vout”：n（数字）输出编号        }        ...      ]
	 * @return 节点数据
	 * @throws Exception
	 */
	public String lockUnspent(boolean unlock, JSONArray Unspents) throws Exception {
		Object[] str = {unlock,Unspents};
		JSONObject json = new JSONObject();
		json.put("method", "lockunspent");
		json.put("params", str);
		json.put("id", Object.class);
		String result = httpPost.sendHttpPost(json.toString());
		return result;
	}
	public String lockUnspent(boolean unlock, String txid,Integer vout) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("txid", txid);
		jsonObject.put("vout", vout);
		JSONArray Unspents = JSONArray.fromObject(jsonObject);
		Object[] str = {unlock,Unspents};
		JSONObject json = new JSONObject();
		json.put("method", "lockunspent");
		json.put("params", str);
		json.put("id", Object.class);
		String result = httpPost.sendHttpPost(json.toString());
		return result;
	}
	/**
	 * 从节点拉取
	 * 锁定所有的未花费txid
	 * @return boolean
	 * @throws Exception
	 */
	public boolean lockAllUnspent() throws Exception {
		boolean b = true;
		String lockUnspent = "";
		try {
			String listUnspent = this.listUnspent();
			JSONObject fromObject = JSONObject.fromObject(listUnspent);
			List<Output> outputs = JsonPluginsUtil.jsonToBeanList(JSONArray.fromObject(fromObject.get("result")), Output.class);
//			outputService.insertOutputOfUnspent(outputs);
			List<Unspent> Unspents = BeanUtils.transform(outputs, Unspent.class);
			lockUnspent = this.lockUnspent(false,JSONArray.fromObject(Unspents));
			JSONObject fromObject2 = JSONObject.fromObject(lockUnspent);
			boolean object = (boolean) fromObject2.get("result");
			if (object == false) {
				b = false;
			}
			JSONObject fromObject3 = JSONObject.fromObject(this.listUnspent());
			if (JSONArray.fromObject(fromObject3.get("result")).size() > 0) {
				b = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			b = false;
		}
		return b;
	}
	
	/**
	 * 解锁指定账户未花费输出
	 * @param account
	 * @return 布尔值
	 */
	public boolean unLockAccountUnspent(String account) {
		
		return false;
		
	}
	
	/** 
	 * @return 返回暂时不可输出的输出列表。
	 */
	public String listlockunspent () {
		Object[] str = {};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "listlockunspent");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
		
	}
	
	/**
	 * 解锁所有未花费输出
	 * @return
	 * @throws Exception
	 */
	public boolean unLockAllAccountUnspent() throws DnaDebugException{
		Boolean b = true;
		String listlockunspent = this.listlockunspent();
		JSONObject fromObject = JSONObject.fromObject(listlockunspent);
		if (fromObject.get("result").toString().equals("[]")) {
			return b;
		}
		
		try {
			
			JSONArray jsonArray = JSONArray.fromObject(fromObject.get("result"));
			for (Object object : jsonArray) {
				JSONObject fromObject2 = JSONObject.fromObject(object);
				this.lockUnspent(true, fromObject2.get("txid").toString(),Integer.parseInt(String.valueOf(fromObject2.get("vout"))) );
			}
		} catch (Exception e) {
			// TODO: handle exception
			b = false;
		}
		
		return b;
		
	}
	
	/**
	 * 锁定其他账户地址未用输出
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public boolean lockOtherAcountUnpent(String address) throws DnaDebugException {
		boolean unLockAllAccountUnspent = this.unLockAllAccountUnspent();
		if (!unLockAllAccountUnspent) {
			return false;
		}
		String listUnspent = this.listUnspent();
		JSONObject fromObject = JSONObject.fromObject(listUnspent);
		boolean equals = fromObject.get("result").toString().equals("[]");
		if (equals) {
			throw new DnaDebugException("账户资金不足");
		}
		JSONArray fromObject2 = JSONArray.fromObject(fromObject.get("result"));
		try {
			for (Object object : fromObject2) {
				JSONObject fromObject3 = JSONObject.fromObject(object);
				String object2 = fromObject3.get("address").toString();
				if (!object2.equals(address)) {
					this.lockUnspent(false, fromObject3.getString("txid"), fromObject3.getInt("vout"));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new DnaDebugException("锁定其他账户未用输出出错了");
		}
		return true;
		
	}


	
	
}
