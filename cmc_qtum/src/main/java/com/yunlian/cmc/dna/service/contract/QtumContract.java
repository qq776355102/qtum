package com.yunlian.cmc.dna.service.contract;

import java.math.BigInteger;

import javax.ws.rs.DefaultValue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.http.Qtumrpc;
import com.yunlian.cmc.dna.qtumutils.HexAdressUtil;
import com.yunlian.cmc.dna.service.QtumConstant;

import net.sf.json.JSONObject;

@Component
public class QtumContract {

	@Autowired
	private Qtumrpc httpPost;

	@Autowired
	private HexAdressUtil hexAdressUtil;

	/**
	 * qtum代币zat转账
	 * 
	 * @param toAdress
	 * @param amount
	 * @param senderAdress
	 * @return
	 * @throws Exception
	 */
	public String sendZatToAddress(String toAdress, String amount, String senderAdress) throws Exception {

		return this.sendZatToAddress(toAdress, amount, 250000, senderAdress);
	}

	/**
	 * 将资金和数据发送到合同。
	 * 
	 * @param toAdress
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public String sendZatToAddress(String toAdress, String amount, Integer gasLimit, String senderAdress)
			throws Exception {

		return sendtocontract("dd8ef078a5699ea3dac26b12cb5c5d8f0a2f6e4f", toAdress, amount, gasLimit, "0.0000004",
				senderAdress);

	}

	public String sendtocontract(String contractaddress, String toAdress, String amount, Integer gasLimit,
			String gasPrice, String senderAdress) throws DnaDebugException {
		amount = amount.concat("00000000");
		if (amount.contains(".")) {
			int indexOf = amount.indexOf(".") + 8;
			// 十六进制ZAT转账数额
			amount = new BigInteger(amount.replace(".", "").substring(0, indexOf), 10).toString(16);
		}
		// pubkeyhash地址转换为十六进制地址
		toAdress = hexAdressUtil.gethexaddress(toAdress);
		// 地址:=
		// 000000000000000000000000471009e04479c1aed9c5057adbbfcf0764ab4729(64位)
		String toAdress_64 = QtumConstant.var_64.concat(toAdress).substring(toAdress.length());
		// 发送代币数量
		// :=00000000000000000000000000000000000000000000000000000000000186a0(64位)
		String amount_64 = QtumConstant.var_64.concat(amount).substring(amount.length());

		// String data = "23b872dd"
		// + "000000000000000000000000471009e04479c1aed9c5057adbbfcf0764ab4729"
		// + "000000000000000000000000f6d0b03da88bdaa7a8ce62369dbda08a34a68b64"
		// + "0000000000000000000000000000000000000000000000000000000005f5e100";

		String data = "a9059cbb".concat(toAdress_64).concat(amount_64);
		Object[] str = { contractaddress, data, 0, gasLimit, gasPrice, senderAdress };
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "sendtocontract");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}

	/**
	 *  查询qtum代币ZAT某个地址余额
	 * @param contractaddress
	 * @param factionHash
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public String balanceOfZat(String address) throws Exception {

		return callcontract("dd8ef078a5699ea3dac26b12cb5c5d8f0a2f6e4f",address);
		
	}
	
	/**
	 * 查看某个合约地址余额
	 * @param contractaddress
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public String callcontract(String contractaddress,String address) throws Exception {


		// pubkeyhash地址转换为十六进制地址
		address = hexAdressUtil.gethexaddress(address);
		// 地址:=
		// 000000000000000000000000471009e04479c1aed9c5057adbbfcf0764ab4729(64位)
		String adress_64 = QtumConstant.var_64.concat(address).substring(address.length());
		String data = "70a08231".concat(adress_64);
		Object[] str = { contractaddress, data };
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "callcontract");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}
}
