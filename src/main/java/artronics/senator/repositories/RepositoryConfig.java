package artronics.senator.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"artronics.gsdwn.model", "artronics.gsdwn.packet"})

@EnableJpaRepositories(basePackages = {
        "artronics.senator.repositories",
        "artronics.senator.repositories.jpa"})

@ComponentScan(basePackages = {
        "artronics.gsdwn.model",
        "artronics.gsdwn.packet",
        "artronics.senator.repositories"
})
@PropertySource("classpath:application-prod.properties")
//@Profile("prod")
@EnableTransactionManagement
public class RepositoryConfig
{
    @Autowired
    DatabaseProperties properties;

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf)
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(dataSource());

        emf.setPackagesToScan("artronics.gsdwn.model",
                              "artronics.gsdwn.packet");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(buildHibernateProperties());

        return emf;
    }

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getDriver());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return dataSource;
    }

    protected Properties buildHibernateProperties()
    {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("spring.datasource.url",
                                        properties.getUrl());

        hibernateProperties.setProperty("spring.datasource.driverClassName",
                                        properties.getDriver());

        hibernateProperties.setProperty("spring.datasource.username", properties.getUsername());
        hibernateProperties.setProperty("spring.datasource.password", properties.getPassword());

        hibernateProperties.setProperty("hibernate.dialect", properties.getDialect());
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("hibernate.use_sql_comments", "false");
        hibernateProperties.setProperty("hibernate.format_sql", "false");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", properties.getHbm2ddl());
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        hibernateProperties.setProperty("hibernate.generate_statistics", "false");

        return hibernateProperties;
    }
}
