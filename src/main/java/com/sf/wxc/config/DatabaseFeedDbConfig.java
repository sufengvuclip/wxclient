package com.sf.wxc.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
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
@PropertySource(value="file:/Users/sufeng/dev/wxc/conf/persistence-multiple-db.properties", ignoreResourceNotFound=true)
@PropertySource(value="file:/program/wxc/conf/persistence-multiple-db.properties", ignoreResourceNotFound=true)
@EnableJpaRepositories(
        basePackages = "com.sf.wxc.repository.db.feeddb",
        entityManagerFactoryRef = "feedDbEntityManager",
        transactionManagerRef = "feedDbTransactionManager"
)
public class DatabaseFeedDbConfig {
    @Autowired
    private Environment env;

    @Bean(name = "feedDbMysqlDataSource")
    @Primary
    /*@ConfigurationProperties(prefix = "spring.datasource")*/
    public DataSource feedDbMysqlDataSource() {
        //return DataSourceBuilder.create().build();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean(name = "feedDbEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean feedDbEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("feedDb");
        em.setDataSource(feedDbMysqlDataSource());
        em.setPackagesToScan(new String[]{"com.sf.wxc.beans"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "feedDbTransactionManager")
    @Primary
    public PlatformTransactionManager feedDbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(feedDbEntityManager().getObject());
        return transactionManager;
    }
}
