package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.core.config.BeanDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {BeanDefinition.class})
public class ControllerConfigRepoTest
{
    @Autowired
    private ControllerConfigRepo repo;

    private ControllerConfig controllerConfig;

    @Before
    @Transactional
    public void setUp() throws Exception
    {
        controllerConfig = new ControllerConfig("192.168.10.11");
    }

    @Test
    public void it_should_create_cont()
    {
        assertNull(controllerConfig.getId());
        repo.save(controllerConfig);

        assertNotNull(controllerConfig.getId());
    }

    @Test
    public void it_should_create_created_timestamp()
    {
        controllerConfig.setIp("10.10.11.12");
        ControllerConfig config = repo.save(controllerConfig);

        assertNotNull(config.getCreated());
    }

    @Test
    public void test_findByIp(){
        ControllerConfig config1 = new ControllerConfig("1.1.1.1");
        ControllerConfig config2 = new ControllerConfig("2.2.2.2");
        ControllerConfig config3 = new ControllerConfig("3.3.3.3");
        config3.setDescription("foo");

        repo.save(config1);
        repo.save(config2);
        repo.save(config3);

        ControllerConfig actCnt = repo.findByIp("3.3.3.3");
        assertNotNull(actCnt);
        assertEquals("foo",actCnt.getDescription());
    }

    @Test
    public void findByIp_should_return_null_if_there_is_no_entity(){
        assertNull(repo.findByIp("50.23.56.123"));
    }

    @Test
    public void test_getLatest() throws InterruptedException
    {
        int num =2;
        List<ControllerConfig>configs= createConfigs(num);

        ControllerConfig actCnf = repo.getLatest();

        assertThat(actCnf.getIp(),equalTo("192.200.0."+(num-1)));
        assertThat(actCnf.getDescription(),equalTo("num: "+(num-1)));
    }

    private List<ControllerConfig> createConfigs(int num) throws InterruptedException
    {
        List<ControllerConfig> configs = new ArrayList<>();
        String baseIp = "192.200.0.";
        for (int i = 0; i < num; i++) {
            ControllerConfig config = new ControllerConfig(baseIp+i);
            config.setDescription("num: "+i);
            configs.add(repo.save(config));
            Thread.sleep(1000);
        }

        return configs;
    }

    @Test
    public void getLatest_should_return_null_if_there_is_no_record()
    {
        repo.deleteAll();
        assertNull(repo.getLatest());
    }
}