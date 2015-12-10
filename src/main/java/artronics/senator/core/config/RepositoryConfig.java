package artronics.senator.core.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
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
//@EnableAutoConfiguration
@EntityScan(basePackages = {"artronics.gsdwn.model", "artronics.gsdwn.packet"})

@EnableJpaRepositories(basePackages = {
        "artronics.senator.repositories",
        "artronics.senator.repositories.jpa"})

@ComponentScan(basePackages = {
        "artronics.gsdwn.model",
        "artronics.gsdwn.packet"
})

@EnableTransactionManagement
public class RepositoryConfig
{
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

        emf.setDataSource(mysqlDataSource());

        emf.setPackagesToScan(new String[]{
                "artronics.gsdwn.model",
                "artronics.gsdwn.packet"
        });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(buildHibernateProperties());

        return emf;
    }

    @Bean
    public DataSource mysqlDataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/Senator");
        dataSource.setUsername("root");
        dataSource.setPassword("4117084");

        return dataSource;
    }

    protected Properties buildHibernateProperties()
    {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("spring.datasource.url",
                                        "jdbc:mysql://localhost:3306/Senator");

        hibernateProperties.setProperty("spring.datasource.driverClassName",
                                        "com.mysql.jdbc.Driver");

        hibernateProperties.setProperty("spring.datasource.username", "root");
        hibernateProperties.setProperty("spring.datasource.password", "4117084");

        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("hibernate.use_sql_comments", "false");
        hibernateProperties.setProperty("hibernate.format_sql", "false");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");

        hibernateProperties.setProperty("hibernate.generate_statistics", "false");

        return hibernateProperties;
    }
//    @Bean
//    NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new NamedParameterJdbcTemplate(dataSource);
//    }
}
