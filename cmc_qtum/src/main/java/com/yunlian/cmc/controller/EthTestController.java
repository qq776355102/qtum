package com.yunlian.cmc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;


@RestController("/api/eth")
public class EthTestController {
   
	@RequestMapping()
	public String send(String contract,String fromAddress,String toAddress,String amount) throws Exception {
        //设置需要的矿工费
        BigInteger GAS_PRICE = BigInteger.valueOf(22000000000L);
        BigInteger GAS_LIMIT = BigInteger.valueOf(4300000);

        //调用的是kovan测试环境，这里使用的是infura这个客户端
        Web3j web3j = new JsonRpc2_0Web3j(new HttpService("http://localhost:7545"));
        //转账人账户地址
//        String fromAddress = "0x89De0c0175060DF0F731782AcC8977Cc741cEc63";
        //被转人账户地址
//        String toAddress = "0xD242a15070962B2Cf97c34dd0AAf9A82278d4b25";
        //转账人私钥
        Credentials credentials = Credentials.create("8776564192b3453fa69ac41baf393d1730a4fa8c0d2e844f461a230ee1d6d4de");
        //        Credentials credentials = WalletUtils.loadCredentials(
        //                "123",
        //                "src/main/resources/UTC--2018-03-01T05-53-37.043Z--d1c82c71cc567d63fd53d5b91dcac6156e5b96b3");

        
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
        		fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        //创建交易，这里是转20个以太币
        BigInteger value = Convert.toWei("20", Convert.Unit.ETHER).toBigInteger();
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, GAS_PRICE, GAS_LIMIT, toAddress, value);

        //签名Transaction，这里要对交易做签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        //发送交易
        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();

        //获得到transactionHash后就可以到以太坊的网站上查询这笔交易的状态了
        System.out.println(transactionHash);
		return transactionHash;
    }
    
    public void erc20_test() {
//        Web3j web3j = new JsonRpc2_0Web3j(new HttpService("http://localhost:7545"));
//        web3j.ethSendTransaction(new Transaction(from, nonce, gasPrice, gasLimit, to, value, data))
        
//        Transaction
//        web3j.ethSendTransaction(transaction)
	}
}