package sales.application.sales.config;



import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.Properties;
import javax.sql.DataSource;

//@Configuration

public class DBConfig {

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;


    @Value("${spring.datasource.password}")
    String password;


    @Value("${jdbc.driverClassName}")
    private String driverClass;

    @Value("${hibernate.dialect}")
    private String dialect;

    @Value("${tomcat.initial-size}")
    private int initialSize;

    @Value("${tomcat.max-wait}")
    private int maxWait;

    @Value("${tomcat.max-active}")
    private int maxActive;

    @Value("${tomcat.max-idle}")
    private int maxIdle;

    @Value("${tomcat.min-idle}")
    private int minIdle;

    @Value("${tomcat.default-auto-commit}")
    private boolean defaultAutoCommit;

    @Value("${tomcat.test-while-idle}")
    private boolean testWhileIdle;

    @Value("${tomcat.test-on-borrow}")
    private boolean testOnBorrow;

    @Value("${tomcat.validation-query}")
    private String validationQuery;


    @Bean
    public DataSource getDataSource() {

        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driverClass);
        ds.setInitialSize(initialSize);
        ds.setMaxWait(maxWait);
        ds.setMaxActive(maxActive);
        ds.setMaxIdle(maxIdle);
        ds.setMinIdle(minIdle);
        ds.setDefaultAutoCommit(defaultAutoCommit);
        ds.setTestOnBorrow(testOnBorrow);
        ds.setValidationQuery(validationQuery);
        return ds;
    }


    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(getDataSource());
        factory.setHibernateProperties(hibernateProperties());

        return factory;
    }


    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");


        return properties;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory factory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(factory);
        return transactionManager;
    }

}