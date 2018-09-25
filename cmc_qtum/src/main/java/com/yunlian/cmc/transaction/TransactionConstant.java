package com.yunlian.cmc.transaction;

import com.yunlian.cmc.dna.service.QtumConstant;

public class TransactionConstant  extends QtumConstant {

	
	public interface TRANSACTION_SCRIPTPUBKEY_TYPE{
		public static final String pubkey = "pubkey";
		public static final String pubkeyhash = "pubkeyhash";
		public static final String nonstandard = "nonstandard";
		public static final String nulldata = "nulldata";
		public static final String call = "call";
	}
	
	//交易 vout中asm
	public interface TRANSACTION_ASM_TYPE{
		public static final String OP_CALL = "OP_CALL";
		public static final String OP_CHECKSIG = "OP_CHECKSIG";
	}
	
	//zat合约地址
	public static final String QTUM_CONTACT_ADDRESSS_ZAT = "dd8ef078a5699ea3dac26b12cb5c5d8f0a2f6e4f";
	
	public interface  TRANSACTION_TYPE{
		/**
		 * 产量交易
		 */
		public static final String GENERATION_TX = "coinbase";
		//合成地址交易
		
		//通用地址交易
		
	}
	
	//erc20
	public interface  ERC20_CODE_METHOD {
		
		public static final String TRANFER = "a9059cbb";
	}
	
}
