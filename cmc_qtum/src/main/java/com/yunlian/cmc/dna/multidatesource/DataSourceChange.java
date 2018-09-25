package com.yunlian.cmc.dna.multidatesource;




import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.cmc.exception.MedicalDebugException;
import cn.cmc.exception.MedicalException;







/**
 * @author mc
 *
 */
@Aspect
@Order(0)
@Component(value = "dataSourceChange")
@Transactional(rollbackFor = Exception.class,noRollbackFor = MedicalDebugException.class)
public class DataSourceChange implements Ordered
{
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* *.yunlian..*service..*.*(..))")
    public void serviceAspect()
    {}

    public void setdataSourceOne(JoinPoint jp)
    {
        DatabaseContextHolder.setCustomerType("QTUM");
    }

    public void setdataSourceTwo(JoinPoint jp)
    {

    }

    @Before("serviceAspect()")
    public void doBefore(JoinPoint joinPoint)
        throws MedicalException
    {
        try
        {
            Class<?> targetClass = joinPoint.getTarget().getClass();

            MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
            Method m = methodSignature.getMethod();
            
            // 默认使用类型注解
            if (targetClass.isAnnotationPresent(YunlianDataSource.class))
            {
                YunlianDataSource source = targetClass.getAnnotation(YunlianDataSource.class);
                DatabaseContextHolder.setCustomerType(source.value());
            }
            // 方法注解可以覆盖类型注解
            if (m != null && m.isAnnotationPresent(YunlianDataSource.class))
            {
                YunlianDataSource source = m.getAnnotation(YunlianDataSource.class);
                DatabaseContextHolder.setCustomerType(source.value());
            }
            
            logger.debug("DataSourceChange, method="+m.toString()+",datasource="+DatabaseContextHolder.getCustomerType());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @After("serviceAspect()")  
    public void after(){  
    	DatabaseContextHolder.clearCustomerType();//调用后清除数据源，系统然后就调用默认数据源了  
    } 
    
    @Override
    public int getOrder()
    {
        return 0;
    }
}
