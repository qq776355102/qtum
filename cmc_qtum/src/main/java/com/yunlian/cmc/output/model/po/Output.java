package com.yunlian.cmc.output.model.po;

import java.math.BigDecimal;

public class Output {
    private Integer id;

    private String txid;

    private Byte vout;

    private String address;

    private String account;

    private String scriptpubkey;

    private BigDecimal amount;

    private Integer confirmations;

    private Boolean spendable;

    private Boolean solvable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid == null ? null : txid.trim();
    }

    public Byte getVout() {
        return vout;
    }

    public void setVout(Byte vout) {
        this.vout = vout;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getScriptpubkey() {
        return scriptpubkey;
    }

    public void setScriptpubkey(String scriptpubkey) {
        this.scriptpubkey = scriptpubkey == null ? null : scriptpubkey.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public Boolean getSpendable() {
        return spendable;
    }

    public void setSpendable(Boolean spendable) {
        this.spendable = spendable;
    }

    public Boolean getSolvable() {
        return solvable;
    }

    public void setSolvable(Boolean solvable) {
        this.solvable = solvable;
    }
}