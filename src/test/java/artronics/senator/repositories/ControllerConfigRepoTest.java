package artronics.senator.repositories;

import artronics.chaparMini.DeviceConnectionConfig;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerStatus;
import artronics.senator.core.config.BeanDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
    public void setUp() throws Exception
    {
        controllerConfig = new ControllerConfig("192.168.10.11");
        controllerConfig.setConnectionConfig(new DeviceConnectionConfig("connection string"));
        controllerConfig.setSinkAddress(0);
        controllerConfig.setStatus(ControllerStatus.NOT_CONNECTED);
    }

    @Test
    public void it_should_create_cont()
    {
        assertNull(controllerConfig.getId());
        repo.save(controllerConfig);

        assertNotNull(controllerConfig.getId());
    }

    @Test
    public void config_should_consist_of_deviceConnectionConfig_which_is_embeddable(){
        repo.save(controllerConfig);

        assertNotNull(controllerConfig.getConnectionConfig());
        assertEquals("connection string",controllerConfig.getConnectionConfig().getConnectionString());
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
        ControllerConfig config1 = createConfig("1.1.1.1");
        ControllerConfig config2 = createConfig("2.2.2.2");
        ControllerConfig config3 = createConfig("3.3.3.3");
        config3.setDescription("foo");

        repo.save(config1);
        repo.save(config2);
        repo.save(config3);

        ControllerConfig actCnt = repo.findByIp("3.3.3.3");
        assertNotNull(actCnt);
        assertEquals("foo",actCnt.getDescription());
    }
    private ControllerConfig createConfig(String ip){
        ControllerConfig config = new ControllerConfig(ip);
        config.setSinkAddress(0);
        config.setConnectionConfig(new DeviceConnectionConfig("connection string"));
        config.setStatus(ControllerStatus.NOT_CONNECTED);
        return config;
    }

    @Test
    public void it_should_update_entity(){
        ControllerConfig config = createConfig("30.30.30.30");
        config.setDescription("foo");
        repo.save(config);

        ControllerConfig updatedConfig = repo.findOne(config.getId());
        updatedConfig.setDescription("bar");
        repo.save(updatedConfig);

        ControllerConfig fetchedConfig = repo.findOne(updatedConfig.getId());

        assertThat(fetchedConfig.getDescription(),equalTo("bar"));
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
            ControllerConfig config = createConfig(baseIp+i);
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