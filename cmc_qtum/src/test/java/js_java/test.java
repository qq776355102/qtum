package js_java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFManager;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.service.transaction.QtumTransactionService;

public class test {

	@Test
	public void test1() throws DnaDebugException {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		QtumTransactionService bean = applicationContext.getBean(QtumTransactionService.class);
		bean.getValidTransactionsAndInsertToLocal("150898");
		
	}
    public static void main(String[] args) throws IOException{

    	//Script脚本
    	String script=
    	"function dealBean()" +
    	"{"
    	+ " str = bsf.lookupBean('u');"
    	+ "return \"ID:\"+str.id+\"用户名:\"+str.uname+\"地址:\"+str.address+\"_调用方法:\"+str.toString()+"+
    	"'\t'+\"的bean内容被获得了;\";" +
    	"}";

    	try {
    	//BSF管理器
    	BSFManager bsfManager=new BSFManager();

    	//BSF引擎
    	BSFEngine bsfEngine =bsfManager.loadScriptingEngine("javascript");

    	/**
    	* Bean
    	*/
    	User u=new User();
    	u.setId(1);
    	u.setUname("archie");
    	u.setAddress("上海松江");

    	//执行Script脚本
    	bsfEngine.eval("javascript", 0, 0, script);

    	//BSFManager注册一个Bean
    	bsfManager.registerBean("u", u);
    	//bsfManager.registerBean("u", "archie");

    	//执行脚本中方法并返回
    	Object result = bsfEngine.eval("javascript", 0, 0,"dealBean();");

    	System.out.println(result.toString());

    	} catch (Exception e) {
    	e.printStackTrace();
    	}
    }
}
