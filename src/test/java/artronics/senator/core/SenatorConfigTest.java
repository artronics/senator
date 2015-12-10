package artronics.senator.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SenatorBootApplication.class})
@WebAppConfiguration
public class SenatorConfigTest
{
    String DEF_IP = "192.168.30.1";

    @Autowired
    SenatorConfig senatorConfig;

    @Test
    public void test_default_value_for_ip()
    {
        assertEquals(DEF_IP, senatorConfig.getControllerIp());
    }
}