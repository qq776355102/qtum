package com.yunlian.cmc.transaction.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunlian.cmc.transaction.mapper.QtumTransactionMapper;
import com.yunlian.cmc.transaction.model.po.QtumTransaction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class TransactionServiceImpl {

	@Autowired
	private QtumTransactionMapper qtumTransactionMapper;

	public void insertFullTransaction(String fullTransactions) throws InterruptedException {

		JSONArray fullTransactionArray = JSONArray.fromObject(fullTransactions);
		for (Object fullTransactionObject : fullTransactionArray) {
			JSONObject fullTransactionJson = JSONObject.fromObject(fullTransactionObject);
			QtumTransaction qtumTransaction = new QtumTransaction();
			String fromadd = "";
			if (fullTransactionJson.containsKey("fromAddresses")) {
				fromadd = fullTransactionJson.getString("fromAddresses");
			}
			String minerfee = "0";
			if (fullTransactionJson.containsKey("minerfee")) {
				minerfee = fullTransactionJson.getString("minerfee");
			}
			String blocknumber = "0";
			if (fullTransactionJson.containsKey("height")) {
				blocknumber = fullTransactionJson.getString("height");
			}
			String blockhash = "0";
			if (fullTransactionJson.containsKey("blockhash")) {
				blockhash = fullTransactionJson.getString("blockhash");
			}

			String time = "";
			if (fullTransactionJson.containsKey("blocktime")) {
				time = fullTransactionJson.getString("blocktime");
			}

			String hash = "";
			if (fullTransactionJson.containsKey("txid")) {
				hash = fullTransactionJson.getString("txid");
			}
			
			qtumTransaction.setFromadd(fromadd);
			qtumTransaction.setMinerfee(new BigDecimal(minerfee));
			qtumTransaction.setBlocknumber(Integer.parseInt(blocknumber));
			qtumTransaction.setBlockhash(blockhash);
			qtumTransaction.setTime(time);
			qtumTransaction.setHash(hash);
	
			
			for (Object voutObject : fullTransactionJson.getJSONArray("vout")) {
				JSONObject voutJson = JSONObject.fromObject(voutObject);
				String toadd = "";
				if (voutJson.containsKey("toaddress")) {
					toadd = voutJson.getString("toaddress");
				}
				String value = "0";
				if (voutJson.containsKey("receive")) {
					value = voutJson.getString("receive");
				}
				qtumTransaction.setToadd(toadd);
				qtumTransaction.setValue(new BigDecimal(value));
			
				qtumTransaction.setContractadd(null);
				qtumTransaction.setGas(null);
				qtumTransaction.setGasprice(null);
				qtumTransaction.setGasused(null);
				qtumTransaction.setId(null);
				String contractadd = null;
				if (voutJson.containsKey("contractTransaction")) {
					if (voutJson.containsKey("contractaddress")) {
						contractadd = voutJson.getString("contractaddress");
						qtumTransaction.setContractadd(voutJson.getString("contractaddress"));
					}
					if (voutJson.containsKey("gasLimit")) {
						qtumTransaction.setGas(new BigDecimal(voutJson.getString("gasLimit")) );
					}
					String gasprice = "0";
					if (voutJson.containsKey("gasPrice")) {
						gasprice = voutJson.getString("gasPrice");
						qtumTransaction.setGasprice(new BigDecimal(voutJson.getString("gasPrice")));
					}
					if (!minerfee.equals("0") && !(gasprice.equals("0"))) {
						BigDecimal b1 = new BigDecimal(minerfee);
						BigDecimal b2 = new BigDecimal(gasprice);
						BigDecimal gasused = b1.divide(b2, 8, BigDecimal.ROUND_HALF_UP);
						qtumTransaction.setGasused(gasused);
					}
				}
				
				// 判断交易是否已存在
				Map<String, Object> param = new HashMap<>();
				param.put("fromadd", fromadd);
				param.put("toadd", toadd);
				param.put("minerfee", minerfee);
				param.put("hash", hash);
				param.put("time", time);
				param.put("value", new BigDecimal(value));
				param.put("contractadd",contractadd);
				
				boolean b = this.isInsert(param);
				if (b){
					qtumTransactionMapper.insertSelective(qtumTransaction);
				}
			}
			
			
		}
	}

	public static void main(String[] args) {
		BigDecimal b1 = new BigDecimal("0.013");
		BigDecimal b2 = new BigDecimal("0.0000003");
		double doubleValue = b1.divide(b2, 8, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.err.println(doubleValue);
	}

	/**
	 * 判断交易是否已经存在
	 * true,交易不存在;
	 * false,交易已经存在;
	 * @param param
	 * @return boolean
	 */
	public boolean isInsert(Map<String, Object> param) {

		QtumTransaction selectBySelective = qtumTransactionMapper.selectBySelective(param);
		return selectBySelective == null;
	}

}
