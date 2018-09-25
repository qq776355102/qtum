package com.yunlian.cmc.dna.multidatesource;



import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

/**
 * @author mc
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource{
	@Override  
    protected Object determineCurrentLookupKey() {  
        return DatabaseContextHolder.getCustomerType();   
    }
	
	@Override  
    public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {  
        super.setDataSourceLookup(dataSourceLookup);  
    }  
  
    @Override  
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {  
        super.setDefaultTargetDataSource(defaultTargetDataSource);  
    }  
  
    @Override  
    public void setTargetDataSources(Map targetDataSources) {  
        super.setTargetDataSources(targetDataSources);  
        super.afterPropertiesSet();  
    }
}
