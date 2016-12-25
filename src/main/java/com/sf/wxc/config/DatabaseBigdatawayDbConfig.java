package com.sf.wxc.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource(value="classpath:application.properties", ignoreResourceNotFound=true)
@PropertySource(value="file:/D:\\ideaworkspace\\github\\persistence-multiple-db.properties", ignoreResourceNotFound=true)
@PropertySource(value="file:/program/wxc/conf/persistence-multiple-db.properties", ignoreResourceNotFound=true)
//@PropertySource({"classpath:application.properties","file:/D:\\ideaworkspace\\github\\persistence-multiple-db.properties","file:/program/wxc/conf/persistence-multiple-db.properties"})
@EnableJpaRepositories(
        basePackages = "com.sf.wxc.repository.db.bigdatawaydb",
        entityManagerFactoryRef = "bigdatawayDbEntityManager",
        transactionManagerRef = "bigdatawayDbTransactionManager"
)
public class DatabaseBigdatawayDbConfig {
    @Autowired
    private Environment env;

    @Bean(name = "bigdatawayDbMysqlDataSource")
    public DataSource bigdatawayDbMysqlDataSource() {
        //return DataSourceBuilder.create().build();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.dbbigdataway.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.dbbigdataway.url"));
        dataSource.setUsername(env.getProperty("spring.dbbigdataway.username"));
        dataSource.setPassword(env.getProperty("spring.dbbigdataway.password"));

        return dataSource;
    }

    @Bean(name = "bigdatawayDbEntityManager")
    public LocalContainerEntityManagerFactoryBean bigdatawayDbEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("bigdatawayDb");
        em.setDataSource(bigdatawayDbMysqlDataSource());
        em.setPackagesToScan(new String[]{"com.sf.wxc.beans"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "bigdatawayDbTransactionManager")
    public PlatformTransactionManager bigdatawayDbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(bigdatawayDbEntityManager().getObject());
        return transactionManager;
    }
}
