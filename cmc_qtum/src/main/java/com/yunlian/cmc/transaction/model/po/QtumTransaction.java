package com.yunlian.cmc.transaction.model.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class QtumTransaction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2238098429668972940L;

	private Integer id;

	private String guid;

	private String hash;

	private String fromadd;

	private String toadd;

	private BigDecimal gas;

	private BigDecimal gasprice;

	private BigDecimal gasused;

	private BigDecimal value;

	private BigDecimal minerfee;

	private Integer blocknumber;

	private String blockhash;

	private String contractadd;

	private String time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid == null ? null : guid.trim();
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash == null ? null : hash.trim();
	}

	public String getFromadd() {
		return fromadd;
	}

	public void setFromadd(String fromadd) {
		this.fromadd = fromadd == null ? null : fromadd.trim();
	}

	public String getToadd() {
		return toadd;
	}

	public void setToadd(String toadd) {
		this.toadd = toadd == null ? null : toadd.trim();
	}

	public BigDecimal getGas() {
		return gas;
	}

	public void setGas(BigDecimal gas) {
		this.gas = gas;
	}

	public BigDecimal getGasprice() {
		return gasprice;
	}

	public void setGasprice(BigDecimal gasprice) {
		this.gasprice = gasprice;
	}

	public BigDecimal getGasused() {
		return gasused;
	}

	public void setGasused(BigDecimal gasused) {
		this.gasused = gasused;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getMinerfee() {
		return minerfee;
	}

	public void setMinerfee(BigDecimal minerfee) {
		this.minerfee = minerfee;
	}

	public Integer getBlocknumber() {
		return blocknumber;
	}

	public void setBlocknumber(Integer blocknumber) {
		this.blocknumber = blocknumber;
	}

	public String getBlockhash() {
		return blockhash;
	}

	public void setBlockhash(String blockhash) {
		this.blockhash = blockhash == null ? null : blockhash.trim();
	}

	public String getContractadd() {
		return contractadd;
	}

	public void setContractadd(String contractadd) {
		this.contractadd = contractadd == null ? null : contractadd.trim();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time == null ? null : time.trim();
	}

	public boolean equals(QtumTransaction qtumTransaction) {
		boolean b = false;
		// TODO Auto-generated method stub
		try {
			if (qtumTransaction.getFromadd().equals(this.fromadd) && qtumTransaction.getToadd().equals(this.toadd)
					&& qtumTransaction.getTime().equals(this.time) && qtumTransaction.getBlockhash().equals(this.blockhash)
					&& qtumTransaction.getMinerfee().toString().equals(this.minerfee.toString())) {
				b = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			b = false;
		}
		
		return b;
	}

}