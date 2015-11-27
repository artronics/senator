package artronics.senator.repositories;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class ControllerRepoTest
{
    @Autowired
    Controller controller;
    @Autowired
    private ControllerRepo repo;
    private ControllerConfig controllerConfig;

    @Before
    @Transactional
    @Rollback(value = false)
    public void setUp() throws Exception
    {
        controllerConfig = new ControllerConfig();
        controllerConfig.setIp("192.168.1.1");

    }

    @Test
    @Transactional
    public void it_should_create_cont()
    {
        repo.create(controllerConfig);

        ControllerConfig cnt = repo.find(controllerConfig.getId());

        assertNotNull(cnt);
        assertThat(cnt.getIp(), equalTo("192.168.1.1"));
    }

    @Test
    @Transactional
    public void it_should_create_created_timestamp()
    {
        repo.create(controllerConfig);

        ControllerConfig cnt = repo.find(controllerConfig.getId());

        assertNotNull(cnt.getCreated());
    }

    @Test
    @Transactional
    public void it_should_update_entity()
    {
        repo.create(controllerConfig);

        ControllerConfig cnt = repo.find(controllerConfig.getId());
        cnt.setIp("192.168.2.2");
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
    public void getLatest_should_return_null_if_there_is_no_record()
    {
        ControllerConfig cnt = repo.getLatest();

        assertNull(cnt);
    }
}