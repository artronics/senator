package artronics.senator.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class SenatorConfigTest
{
    String DEF_IP = "192.168.30.1";

    SenatorConfig senatorConfig;

    @Before
    public void setUp() throws Exception
    {
        ClassPathXmlApplicationContext cnt =
                new ClassPathXmlApplicationContext("senator-beans.xml");

        senatorConfig = cnt.getBean(SenatorConfig.class);
    }

    @Test
    public void test_default_value_for_ip()
    {
        assertEquals(DEF_IP, senatorConfig.getControllerIp());
    }
}