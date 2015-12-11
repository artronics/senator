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

    @Autowired
    public DatabaseProperties(
            @Value("${database.url}")String url,
            @Value("${database.driver}") String driver,
            @Value("${database.username}") String username,
            @Value("${database.password}") String password,
            @Value("${database.dialect}") String dialect)
    {
        this.url = url;
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.dialect = dialect;
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
}
