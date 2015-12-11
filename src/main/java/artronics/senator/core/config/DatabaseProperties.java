package artronics.senator.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseProperties
{
    private String url;
    private String driver;
    private String username;
    private String password;
    private String dialect;
    private String hmb2ddl;

    @Autowired
    public DatabaseProperties(
            @Value("${database.url}") String url,
            @Value("${database.driver}") String driver,
            @Value("${database.username}") String username,
            @Value("${database.password}") String password,
            @Value("${database.dialect}") String dialect,
            @Value("${database.dialect}") String hbm2ddl)

    {
        this.url = url;
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.dialect = dialect;
        this.hmb2ddl = hbm2ddl;
    }

    public String getUrl()
    {
        return url;
    }

    public String getDriver()
    {
        return driver;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getDialect()
    {
        return dialect;
    }

    public String getHmb2ddl()
    {
        return hmb2ddl;
    }
}
