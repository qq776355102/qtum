package com.yunlian.cmc.dna.http;


import java.io.IOException;

import javax.xml.bind.DatatypeConverter;


import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.hpl.sparta.xpath.ThisNodeTest;







@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Qtumrpc{

    private Logger logger= Logger.getLogger(Qtumrpc.class);
    
	private static final String  user = "test";//rpc接口用户名
	private static final String  password = "123456";//rpc接口密码
	private static String url = "http://192.168.1.2:3889";
	private static String cred = null; 
	
	static{
			cred = DatatypeConverter.printBase64Binary((user+ ":" +password).getBytes());
	}

    private RequestConfig requestConfig = RequestConfig.custom()  
            .setSocketTimeout(15000)  
            .setConnectTimeout(15000)  
            .setConnectionRequestTimeout(15000)  
            .build();
    

	
    /** 
     * 发送 post请求 
     * @param params 参数(格式:key1=value1&key2=value2) 
     */  
    public String sendHttpPost(String params) {  
        HttpPost httpPost = new HttpPost(url);// 创建httpPost  
        try {  
            //设置参数  
            StringEntity stringEntity = new StringEntity(params, "UTF-8");  
            httpPost.setHeader("Authorization", "Basic " + cred);
            stringEntity.setContentType(APPLICATION_JSON);  
            httpPost.setEntity(stringEntity);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return sendHttpPost(httpPost);  
    }
	
    /** 
     * 发送Post请求 
     * @param httpPost 
     * @return 
     */  
    private String sendHttpPost(HttpPost httpPost) {  
        CloseableHttpClient httpClient = null;  
        CloseableHttpResponse response = null;  
        HttpEntity entity = null;  
        String responseContent = null;  
        try {  
            // 创建默认的httpClient实例.  
            httpClient = HttpClients.createDefault();  
            httpPost.setConfig(requestConfig);  
            // 执行请求  
            response = httpClient.execute(httpPost);  
            entity = response.getEntity();  
            responseContent = EntityUtils.toString(entity, "UTF-8");  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace(); 
                logger.debug(e);
            }  
        }  
        return responseContent;  
    }  

    private String APPLICATION_JSON = "application/json; charset=utf-8";

}
