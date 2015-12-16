package artronics.senator.repositories;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
@EnableTransactionManagement
@Profile("dev")
public class TestRepositoryConfig
{
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory( emf );
        return transactionManager;
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(dataSource() );

        emf.setPackagesToScan("artronics.gsdwn.model",
                              "artronics.gsdwn.packet");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties( buildHibernateProperties() );

        return emf;
    }
    @Bean
    @Primary
    public DataSource dataSource()
    {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }
    protected Properties buildHibernateProperties()
    {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("spring.datasource.url",
                                        "jdbc:h2:mem:SenatorTest;DB_CLOSE_DELAY=-1;" +
                                                "DB_CLOSE_ON_EXIT=FALSE");

        hibernateProperties.setProperty("spring.datasource.driverClassName",
                                        "org.h2.Driver");

        hibernateProperties.setProperty("spring.datasource.username","artronics");
        hibernateProperties.setProperty("spring.datasource.password","artronics");

        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("hibernate.use_sql_comments", "false");
        hibernateProperties.setProperty("hibernate.format_sql", "false");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        hibernateProperties.setProperty("hibernate.generate_statistics", "false");

        return hibernateProperties;
    }
}
