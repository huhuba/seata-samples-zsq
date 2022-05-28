/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.samples.mutiple.mybatisplus.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HelloWoodes
 */
@Configuration
public class DataSourceProxyConfig {

    @Bean("originOrder")
    @ConfigurationProperties(prefix = "spring.datasource.order")
    public DataSource dataSourceMaster() {
        return new DruidDataSource();
    }

    @Bean("originStock")
    @ConfigurationProperties(prefix = "spring.datasource.stock")
    public DataSource dataSourceStock() {
        return new DruidDataSource();
    }

    @Bean("originPay")
    @ConfigurationProperties(prefix = "spring.datasource.pay")
    public DataSource dataSourcePay() {
        return new DruidDataSource();
    }

    @Bean(name = "order")
    public DataSourceProxy masterDataSourceProxy(@Qualifier("originOrder") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "stock")
    public DataSourceProxy stockDataSourceProxy(@Qualifier("originStock") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "pay")
    public DataSourceProxy payDataSourceProxy(@Qualifier("originPay") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("order") DataSource dataSourceOrder,
                                        @Qualifier("stock") DataSource dataSourceStock,
                                        @Qualifier("pay") DataSource dataSourcePay) {

        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>(3);
        dataSourceMap.put(DataSourceKey.ORDER.name(), dataSourceOrder);
        dataSourceMap.put(DataSourceKey.STOCK.name(), dataSourceStock);
        dataSourceMap.put(DataSourceKey.PAY.name(), dataSourcePay);

        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSourceOrder);
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

        DynamicDataSourceContextHolder.getDataSourceKeys().addAll(dataSourceMap.keySet());

        return dynamicRoutingDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dynamicDataSource") DataSource dataSource) {
        // 这里用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        return mybatisSqlSessionFactoryBean;
    }

}