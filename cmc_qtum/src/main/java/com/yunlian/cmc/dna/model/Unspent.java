package com.yunlian.cmc.dna.model;

import java.io.Serializable;

public class Unspent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8256286715819083382L;

	/**
	 * 事务id
	 */
	private String txid;
	
	/**
	 * 输出索引
	 */
	private Byte vout;

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Byte getVout() {
		return vout;
	}

	public void setVout(Byte vout) {
		this.vout = vout;
	}


	
	
}
