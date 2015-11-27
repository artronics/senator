package artronics.senator.core;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.services.ControllerConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class SenatorInitializerTest
{
    @Autowired
    SenatorInitializer initializer;

    @Autowired
    ControllerConfigService configService;

    ControllerConfig cnf;

    @Before
    @Transactional
    @Rollback(value = false)
    public void setUp() throws Exception
    {


    }

    @Test
    @Transactional
    public void test_init_it_should_create_default_config_if_there_is_no_config()
    {
        initializer.init();

        cnf = configService.getLatest();

        assertNotNull(cnf);
        assertThat(cnf.getIp(), equalTo("192.168.1.1"));
    }
}