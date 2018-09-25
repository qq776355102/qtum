package com.yunlian.cmc.dna.timer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.chainsaw.Main;

import cn.cmc.util.StringUtils;



public class QtumTransactionHolder {

	private static volatile BigInteger INIT_BLOCK = new BigInteger("152100");
	static{
		Properties prop = new Properties();
		try {
			prop.load(Main.class.getResourceAsStream("/qtum.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String init_block = prop.getProperty("INIT_BLOCK_NUMBER");
		if (StringUtils.isEmpty(init_block)) INIT_BLOCK = new BigInteger("154168");
		INIT_BLOCK = new BigInteger(init_block);
	}
	


	private static volatile BigInteger FINISHED_BLOCK = null;
	

	private static volatile BigInteger LAST_BLOCK = null;
	static{
		Properties prop = new Properties();
		try {
			prop.load(Main.class.getResourceAsStream("/qtum.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String init_block = prop.getProperty("INIT_BLOCK_NUMBER");
		if (StringUtils.isEmpty(init_block)) INIT_BLOCK = new BigInteger("154168");
		INIT_BLOCK = new BigInteger(init_block);
	}

	
	
	private static final LinkedBlockingDeque<String> contextHolder = new LinkedBlockingDeque<String>();


	
	private QtumTransactionHolder() {
	}
	
	public synchronized static  void put(String heigt) throws InterruptedException {
		contextHolder.put(heigt);
	}

	public static String take(){
		String string = "";
		try {
			string =  contextHolder.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;
	}

	public static boolean isEmpty() {
		return contextHolder.isEmpty();
	}


	public static BigInteger getLAST_BLOCK() {
		return LAST_BLOCK;
	}

	public synchronized static void setLAST_BLOCK(BigInteger lAST_BLOCK) {
		LAST_BLOCK = lAST_BLOCK;
	}

	public static BigInteger getFINISHED_BLOCK() {
		return FINISHED_BLOCK;
	}

	public synchronized static void setFINISHED_BLOCK(BigInteger fINISHED_BLOCK) {
		FINISHED_BLOCK = fINISHED_BLOCK;
	}

	public static BigInteger getINIT_BLOCK() {
		return INIT_BLOCK;
	}

	public synchronized static void setINIT_BLOCK(BigInteger iNIT_BLOCK) {
		INIT_BLOCK = iNIT_BLOCK;
	}





}
