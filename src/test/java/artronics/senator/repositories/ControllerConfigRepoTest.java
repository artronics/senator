package artronics.senator.repositories;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class ControllerConfigRepoTest
{
    @Autowired
    Controller controller;
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
    @Transactional
    public void it_should_create_cont()
    {
        repo.create(controllerConfig);

        ControllerConfig cnt = repo.find(controllerConfig.getIp());

        assertNotNull(cnt);
        assertThat(cnt.getIp(), equalTo("192.168.10.11"));
    }

    @Test
    @Transactional
    public void it_should_create_created_timestamp()
    {
        repo.create(controllerConfig);

        ControllerConfig cnt = repo.find(controllerConfig.getIp());

        assertNotNull(cnt.getCreated());
    }


    // TODO detach entity to test update properly
    //http://stackoverflow.com/questions/19160398/how-can-i-reliably-unit-test-updates-of-a-jpa
    // -entity-in-a-unit-test
    @Ignore
    @Test
    @Transactional
    public void it_should_update_entity()
    {
        repo.create(controllerConfig);

        ControllerConfig cnt = repo.find(controllerConfig.getIp());

        repo.update(cnt);

        assertThat(controllerConfig.getIp(), equalTo("192.168.2.2"));
    }

    @Test
    @Transactional
    public void test_getLatest() throws InterruptedException
    {
        repo.create(controllerConfig);
        Thread.sleep(1000);
        ControllerConfig cnt = new ControllerConfig();
        cnt.setIp("192.168.2.2");
        repo.create(cnt);

        ControllerConfig actCnt = repo.getLatest();

        assertThat(actCnt.getIp(), equalTo("192.168.2.2"));
    }

    @Test
    @Transactional
    public void getLatest_should_return_null_if_there_is_no_record()
    {
        ControllerConfig cnt = repo.getLatest();

        assertNull(cnt);
    }
}