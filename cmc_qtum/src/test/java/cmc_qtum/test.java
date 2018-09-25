package cmc_qtum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import javax.ws.rs.PUT;

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

import com.alibaba.druid.sql.visitor.functions.Isnull;
import com.yunlian.cmc.dna.exception.DnaDebugException;
import com.yunlian.cmc.dna.service.transaction.QtumTransactionService;

public class test {

	public   int a = 2;
//	@Test
//	public void test1() throws DnaDebugException {
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//		QtumTransactionService bean = applicationContext.getBean(QtumTransactionService.class);
//		bean.getValidTransactionsAndInsertToLocal("150898");
//		
//	}
    public static void main(String[] args) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
//    	User user = new User(212, "testName", "testAddress");
//		Field field = User.class.getField("uname");
//		Object object = field.get(user);
//		Class<?> type = field.getType();
//		String name = type.getName();
//		System.err.println(name);
//		System.out.println(object.toString());
//		System.gc();
			
//    	 String hello ="Hello",lo ="lo";
//         System.out.println("1"+(hello =="Hello")+"");
//         System.out.println("2"+(Other.hello == hello)+"");
//         System.out.println("3"+(Other.hello == hello)+"");
//         System.out.println("4"+(hello ==("Hel"+"lo"))+"");
//         System.out.println("5"+(hello ==("Hel"+ lo))+"");
//         System.out.println("6"+(hello ==("Hel"+ lo).intern()));
//         System.out.println("7"+(hello ==("Hel".concat("lo").intern())));
         double pi = Math.PI;
         System.err.println(pi);
    }
}
class Other {static String hello ="Hello"; }
