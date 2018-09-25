package com.yunlian.cmc.dna.service.transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.http.Qtumrpc;
import com.yunlian.cmc.dna.service.account.AccountService;
import com.yunlian.cmc.dna.service.block.BlockService;
import com.yunlian.cmc.dna.timer.QtumTransactionHolder;
import com.yunlian.cmc.transaction.TransactionConstant;
import com.yunlian.cmc.transaction.service.TransactionServiceImpl;

import cn.cmc.core.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class QtumTransactionService {

	Logger log = LoggerFactory.getLogger(QtumTransactionService.class);

	@Autowired
	private Qtumrpc httpPost;

	@Autowired
	private AccountService accountService;

	@Autowired
	private BlockService blockService;

	@Autowired
	private TransactionServiceImpl transactionService;

	private static final ExecutorService transactionExecutor = Executors.newFixedThreadPool(5);

	/**
	 * 返回最近一次交易的“计数”，跳过帐户“帐户”的第一个“来自”交易。
	 * 
	 * @param account
	 * @param count
	 *            要返回的事务数量
	 * 
	 * @param skip
	 *            要跳过的交易数量
	 * 
	 * @param include_watchonly(默认=
	 *            false）将事务包含到只能看的地址
	 * @return
	 */
	public String listtransactions(String account, Integer count, Integer skip, boolean include_watchonly) {
		Object[] str = { account, count, skip, include_watchonly };
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "listtransactions");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}

	/**
	 * 获取最近10笔的交易记录
	 * 
	 * @param account
	 * @return
	 */
	public String listtransactionsbyaccount(String account) {
		Object[] str = { account, 10, 0, false };
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "listtransactions");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}

	/**
	 * 
	 * @param address
	 * @return
	 * @throws DnaDebugException
	 */
	public String listtransactionsbyaddress(String address, Integer limit, Integer offset) throws DnaDebugException {
		String reString = accountService.getaccount(address);
		String account = JSONObject.fromObject(reString).getString("result");
		if (account.equals("null")) {
			throw new DnaDebugException("账户地址存在|Invalid Qtum address");
		}
		return this.listtransactions(account, limit, offset, false);
	}

	/**
	 * 获取最近10笔的交易记录
	 * 
	 * @param address
	 * @return
	 * @throws DnaDebugException
	 */
	public String listtransactionsbyaddress(String address) throws DnaDebugException {
		String reString = accountService.getaccount(address);
		String account = JSONObject.fromObject(reString).getString("result");
		if (account.equals("null")) {
			throw new DnaDebugException("账户地址存在|Invalid Qtum address");
		}
		return this.listtransactionsbyaccount(account);
	}

	/**
	 * 获取交易记录包含发送地址
	 * 
	 * @param address
	 * @return
	 * @throws DnaDebugException
	 */
	public String listTransactionsContainFromaddressByAddress(String address) throws DnaDebugException {
		String rString = this.listtransactionsbyaddress(address);
		JSONObject fromObject = JSONObject.fromObject(rString);
		List jsonArray = fromObject.getJSONArray("result");
		JSONArray jsonArray2 = new JSONArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject fromObject2 = JSONObject.fromObject(jsonArray.get(i));
			Map jsonObject = fromObject2;
			// if (fromObject2.getString("category").equals("receive")) {
			String txid = fromObject2.getString("txid");
			String transactionOfFromAddress = this.getTransactionOfFromAddress(txid);
			// 补存fromaddress
			fromObject2.put("fromaddress", transactionOfFromAddress);
			// };
			jsonArray2.add(fromObject2);
		}
		return jsonArray2.toString();

	}

	public String getrawtransaction(String txid) {
		Object[] str = { txid, true };
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "getrawtransaction");
		jsonObject.put("params", str);
		jsonObject.put("id", Object.class);
		String rString = httpPost.sendHttpPost(jsonObject.toString());
		return rString;
	}

	public String getTransactionOfFromAddress(String txid) {
		String rawtransactionString = this.getrawtransaction(txid);
		JSONObject rawtransactionJSON = JSONObject.fromObject(rawtransactionString);
		JSONObject result = rawtransactionJSON.getJSONObject("result");
		JSONArray inputTransactions = result.getJSONArray("vin");
		for (Object inputObject : inputTransactions) {
			JSONObject inputJson = JSONObject.fromObject(inputObject);
			if (inputJson.containsKey("coinbase"))
				break;
			int vout = inputJson.getInt("vout");
			String preRawtransactionString = this.getrawtransaction(inputJson.getString("txid"));
			JSONObject preRawtransactionJson = JSONObject.fromObject(preRawtransactionString);
			JSONObject preResult = preRawtransactionJson.getJSONObject("result");
			JSONArray preVout = preResult.getJSONArray("vout");
			for (Object preVoutObject : preVout) {
				JSONObject preVoutJson = JSONObject.fromObject(preVoutObject);
				int n = preVoutJson.getInt("n");
				if (n == vout) {
					JSONObject preScriptPubKey = preVoutJson.getJSONObject("scriptPubKey");
					JSONArray jsonArray = preScriptPubKey.getJSONArray("addresses");
					return jsonArray.getString(0);
				}
			}

		}
		return "";
	}

	/*
	 ************ 供彭朝方交易记录使用*************
	 * 
	 */

	/**
	 * 返回有关我们钱包用户交易的txid
	 * 
	 * @param jsonArray[txid...]
	 * @return 关于钱包的[txid...]
	 */
	public JSONArray getTxidsOfWallet(JSONArray jsonArray) {
		JSONArray txidsInWallet = new JSONArray();
		for (Object txidObject : jsonArray) {

			boolean b = false;
			// object是块中所有的txid
			String rawtransactionString = this.getrawtransaction(txidObject.toString());
			JSONObject result = JSONObject.fromObject(rawtransactionString).getJSONObject("result");
			JSONArray voutArray = result.getJSONArray("vout");
			for (Object voutObject : voutArray) {
				JSONObject scriptPubKey = JSONObject.fromObject(voutObject).getJSONObject("scriptPubKey");
				String asm = scriptPubKey.getString("asm");
				// qtum交易
				if (asm.endsWith(TransactionConstant.TRANSACTION_ASM_TYPE.OP_CHECKSIG)) {
					JSONArray addresses = scriptPubKey.getJSONArray("addresses");
					for (Object addressObject : addresses) {
						// 判断地址是否是我们钱包账户
						String accountString = accountService.getaccount(addressObject.toString());
						JSONObject accountJson = JSONObject.fromObject(accountString);
						// 不是我们钱包用户
						if (accountJson.getString("result").equals(""))
							break;
						// 是我们钱包用户
						b = true;
						break;
					}
				}

				// 合约交易
				if (asm.equals(TransactionConstant.TRANSACTION_ASM_TYPE.OP_CALL)) {
					String contactCode = asm.split(" ")[3];
					// erc20-tranfer交易
					if (contactCode.substring(0, 8).equals(TransactionConstant.ERC20_CODE_METHOD.TRANFER)) {
						String hexAddress_64 = contactCode.substring(8, 72);
						String hexAddress = "";
						for (int i = 0; i < 64; i++) {
							if (!String.valueOf(hexAddress_64.charAt(i)).equals("0")) {
								hexAddress = hexAddress_64.substring(i);
								break;
							}
						}
						String addressString = accountService.fromhexaddress(hexAddress);
						String address = JSONObject.fromObject(addressString).getString("result");
						String accountString = accountService.getaccount(address);
						String account = JSONObject.fromObject(accountString).getString("result");
						if (!account.equals("")) {
							b = true;
						}
					}
				}
			}

			if (!b) {
				// 判断输入中是否有有钱包地址
				boolean isInWallet = this.txidIsInWallet(txidObject.toString());
				if (isInWallet) {
					b = true;
				}
			}

			if (b) {
				txidsInWallet.add(txidObject.toString());
			}

			System.err.println("txidObject.toString()" + txidObject.toString());

		}
		return txidsInWallet;
	}

	// 判断输入地址中是否有钱包的地址
	public boolean txidIsInWallet(String txid) {
		String rawtransaction = this.getrawtransaction(txid);
		JSONObject rawtransactionRs = JSONObject.fromObject(rawtransaction);
		JSONObject result = rawtransactionRs.getJSONObject("result");
		JSONArray vin = result.getJSONArray("vin");
		for (Object inputObject : vin) {
			// 单个输入对象
			JSONObject input = JSONObject.fromObject(inputObject);
			// 产量交易
			if (input.containsKey(TransactionConstant.TRANSACTION_TYPE.GENERATION_TX))
				break;
			// 单个输入 对应的输出索引
			int vout = input.getInt("vout");
			String preRawtransaction = this.getrawtransaction(input.getString("txid"));
			JSONObject preRs = JSONObject.fromObject(preRawtransaction);
			JSONObject preResult = preRs.getJSONObject("result");
			// 所有输出
			JSONArray preOutputs = preResult.getJSONArray("vout");

			for (Object preOutputObject : preOutputs) {
				JSONObject preOutput = JSONObject.fromObject(preOutputObject);
				int n = preOutput.getInt("n");
				if (vout == n) {
					// 输入的总金额
					JSONObject scriptPubKey = preOutput.getJSONObject("scriptPubKey");
					if (scriptPubKey.getString("asm").endsWith("OP_CALL"))
						return false;
					JSONArray addresses = scriptPubKey.getJSONArray("addresses");
					for (Object addressObject : addresses) {
						String accountString = accountService.getaccount(addressObject.toString());
						String account = JSONObject.fromObject(accountString).getString("result");
						if (!account.equals("")) {
							return true;
						}
					}
				}
			}

		}

		return false;

	}

	// qtum完整交易输入
	// Intact
	public String getInputTransaction(String txid) {
		JSONObject inputTransaction = new JSONObject();
		String rawtransaction = this.getrawtransaction(txid);
		JSONObject rawtransactionRs = JSONObject.fromObject(rawtransaction);
		JSONObject result = rawtransactionRs.getJSONObject("result");
		// 交易输入
		JSONArray vin = result.getJSONArray("vin");

		// 放交易输入address
		Set<String> fromAddresses = new HashSet<>();

		// 输入总金额
		BigDecimal totalSpent = new BigDecimal("0");

		for (Object inputObject : vin) {
			// 单个输入对象
			JSONObject input = JSONObject.fromObject(inputObject);
			// 单个输入 对应的输出索引
			int vout = input.getInt("vout");
			String preRawtransaction = this.getrawtransaction(input.getString("txid"));
			JSONObject preRs = JSONObject.fromObject(preRawtransaction);
			JSONObject preResult = preRs.getJSONObject("result");
			// 所有输出
			JSONArray preOutputs = preResult.getJSONArray("vout");

			for (Object preOutputObject : preOutputs) {
				JSONObject preOutput = JSONObject.fromObject(preOutputObject);
				int n = preOutput.getInt("n");
				if (vout == n) {
					String value = preOutput.getString("value");
					// 输入的总金额
					totalSpent = totalSpent.add(new BigDecimal(value));
					JSONObject scriptPubKey = preOutput.getJSONObject("scriptPubKey");
					JSONArray addresses = scriptPubKey.getJSONArray("addresses");
					for (Object addressObject : addresses) {
						fromAddresses.add(addressObject.toString());
					}
				}
			}
		}
		inputTransaction.put("totalSpent", totalSpent);
		inputTransaction.put("fromAddresses", fromAddresses);
		return inputTransaction.toString();
	}

	public String getOuputTransaction(String txid) {
		JSONObject ouputTrans = new JSONObject();
		JSONArray voutArray = new JSONArray();
		String rs = this.getrawtransaction(txid);
		JSONObject resultJson = JSONObject.fromObject(rs).getJSONObject("result");
		JSONArray vout = JSONObject.fromObject(rs).getJSONObject("result").getJSONArray("vout");
		// 所有输出金额总计
		BigDecimal totaloutputAmount = new BigDecimal("0");
		boolean b = false;
		for (Object voutObject : vout) {
			JSONObject voutJson = new JSONObject();
			// object是每一个输出对象
			boolean bl = false;
			JSONObject output = JSONObject.fromObject(voutObject);
			String value = output.getString("value");
			totaloutputAmount = totaloutputAmount.add(new BigDecimal(value));
			JSONObject scriptPubKey = output.getJSONObject("scriptPubKey");
			String asm = scriptPubKey.getString("asm");
			// qtum 交易
			JSONArray addressesInWallet = new JSONArray();
			if (asm.endsWith(TransactionConstant.TRANSACTION_ASM_TYPE.OP_CHECKSIG)) {
				JSONArray addresses = scriptPubKey.getJSONArray("addresses");
				for (Object addressObject : addresses) {
					String account = accountService.getaccount(addressObject.toString());
					if (!JSONObject.fromObject(account).getString("result").equals("")) {
						bl = true;
						b = true;
						addressesInWallet.add(addressObject.toString());
					}
				}
				// ouputTransaction.put("contactaddress",
				// "ffffffffffffffffffffffffffffffffffffffff");
			}

			if (bl) {
				voutJson.put("toaddress", addressesInWallet.toString());
				voutJson.put("receive", new BigDecimal(value).toString());
			}

			// 合约交易
			if (asm.endsWith(TransactionConstant.TRANSACTION_ASM_TYPE.OP_CALL)) {
				// 判断是否是ZAT
				// if (StringUtils.contains(asm,
				// TransactionConstant.QTUM_CONTACT_ADDRESSS_ZAT)) {
				String[] split = asm.split(" ");

				// split[3] = "a9059cbb"
				// +
				// "000000000000000000000000f6d0b03da88bdaa7a8ce62369dbda08a34a68b64"
				// +
				// "0000000000000000000000000000000000000000000000000000000002faf080";
				if (split[3].substring(0, 8).equals(TransactionConstant.ERC20_CODE_METHOD.TRANFER)) {
					String hexAddress_64 = split[3].substring(8, 72);
					String hexAddress = "";
					for (int i = 20; i < 64; i++) {
						if (!String.valueOf(hexAddress_64.charAt(i)).equals("0")) {
							hexAddress = hexAddress_64.substring(i);
							break;
						}
					}
					String addressString = accountService.fromhexaddress(hexAddress);
					String address = JSONObject.fromObject(addressString).getString("result");
					String account = accountService.getaccount(address);
					// 是钱包账户
					if (!JSONObject.fromObject(account).getString("result").equals("")) {

						bl = true;
						b = true;

						// 是钱包账户
						String amount_64 = split[3].substring(72, 136);
						String amount_ox16 = "";
						for (int i = 0; i < 64; i++) {
							if (!String.valueOf(amount_64.charAt(i)).equals("0")) {
								amount_ox16 = amount_64.substring(i);
								break;
							}
						}
						String string = new BigInteger(amount_ox16, 16).toString(10);
						String amount = new BigDecimal(string).divide(new BigDecimal("100000000")).toPlainString();
						String gisPrice = new BigDecimal(split[2]).movePointLeft(8).toPlainString();

						// 合约交易
						voutJson.put("toaddress", address);
						voutJson.put("receive", amount);
						voutJson.put("contractaddress", split[4]);
						voutJson.put("gasLimit", split[1]);
						voutJson.put("gasPrice", gisPrice);
						voutJson.put("contractTransaction", true);
					}
				}

				// }
			}

			voutArray.add(voutJson);

		}
		if (b) {
			ouputTrans.put("totaloutput", totaloutputAmount.toPlainString());
			ouputTrans.put("txid", txid);
			ouputTrans.put("blocktime",
					DateUtil.formatDate(resultJson.getString("blocktime").concat("000"), DateUtil.DATE_TIME_PATTERN));
			ouputTrans.put("blockhash", resultJson.getString("blockhash"));
			ouputTrans.put("vout", voutArray);
		}

		return ouputTrans.toString();
	}

	/**
	 * 根据块中txid获取我们钱包中所有交易记录 完整交易输出提供给pcf
	 * 
	 * @param jsonArray
	 * @return
	 */
	public String getFullTransactions(JSONArray jsonArray) {
		JSONArray txidsOfWallet = this.getTxidsOfWallet(jsonArray);

		JSONArray fullTransactionArray = new JSONArray();

		for (Object txidObject : txidsOfWallet) {
			System.err.println("块中hash是" + txidObject.toString());
			JSONObject fullTransaction = new JSONObject();
			// vin
			String inputTransaction = this.getInputTransaction(txidObject.toString());
			JSONObject inputTransactionJson = JSONObject.fromObject(inputTransaction);
			fullTransaction.putAll(inputTransactionJson);
			// vout
			String ouputTransaction = this.getOuputTransaction(txidObject.toString());
			JSONObject ouputTransactionJson = JSONObject.fromObject(ouputTransaction);
			fullTransaction.putAll(ouputTransactionJson);
			BigDecimal totalSpent = new BigDecimal(inputTransactionJson.get("totalSpent").toString());
			BigDecimal totaloutput = new BigDecimal(ouputTransactionJson.get("totaloutput").toString());
			BigDecimal minerfee = totalSpent.subtract(totaloutput);
			if (minerfee.compareTo(BigDecimal.ZERO) >= 0) {
				fullTransaction.put("minerfee", minerfee);
			} else {
				fullTransaction.put("minerfee", 0);
			}
			String height = blockService.getblockheight(ouputTransactionJson.get("blockhash").toString());
			fullTransaction.put("height", height);
			fullTransactionArray.add(fullTransaction);
		}
		return fullTransactionArray.toString();
	}

	@Async
	public void syncQtumBlock() throws InterruptedException {
		while (true) {
			// QtumTransactionHolder.put(i+"");
			String height = QtumTransactionHolder.take();
			log.info("块高已经同步到"+height);
			System.out.println("从队列获取的块高的线程是=" + Thread.currentThread().getName() + "=:*******" + height + "***********");
			QtumTransactionHolder.setFINISHED_BLOCK(new BigInteger(height));

			getValidTransactionsAndInsertToLocal(height);

		}
	}
	public void getValidTransactionsAndInsertToLocal(final String height) {

		log.debug("执行getValidTransactionsAndInsertToLocal方法的线程名是**********" + Thread.currentThread().getName() + "***");

		Callable<String> callable = new Callable<String>() {

			@Override
			public String call() throws Exception {
				String blockhashString = blockService.getblockhash(height);
				JSONArray txids = blockService.getblock(JSONObject.fromObject(blockhashString).getString("result"));
				String result = getFullTransactions(txids);

				// 插入数据库
				System.out.println(
						"线程名-" + Thread.currentThread().getName() + "- 执行getFullTransactions531行:===" + result);
				if (!cn.cmc.util.StringUtils.isEmpty(result) || !result.equals("[]")) {
					try {
						transactionService.insertFullTransaction(result);
					} catch (Exception e) {
						// TODO: handle exception
						System.err.println("插入交易记录异常");
						log.debug("插入交易记录异常 FullTransactions"+result);
					}
				}
				return null;
			}
		};
		transactionExecutor.submit(callable);
	}

}
