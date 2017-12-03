package com.taotao.manager.datasource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * @author liu_mw
 * @date 2017/12/3 17:15
 */
public class DynamicDataSource extends AbstractRoutingDataSource{

    @Override
    protected Object determineCurrentLookupKey() {
        // 使用DynamicDataSourceHolder保证线程安全，并且得到当前线程中的数据源key
        return DynamicDataSourceHolder.getDataSourceKey();
    }

}
