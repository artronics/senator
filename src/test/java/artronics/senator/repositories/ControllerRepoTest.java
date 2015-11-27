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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class ControllerRepoTest
{
    @Autowired
    private ControllerRepo repo;

    private ControllerConfig controllerConfig;

    private ControllerConfig controllerConfig;
    @Autowired
    Controller controller;

    @Before
    @Transactional
    @Rollback(value = false)
    public void setUp() throws Exception
    {
        controllerConfig = new ControllerConfig();
        controllerConfig.setIp("192.168.1.1");

        repo.create(controllerConfig);
    }

    @Test
    @Transactional
    public void it_should_create_cont()
    {
        ControllerConfig cnt = repo.find(controllerConfig.getId());

        assertNotNull(cnt);
        assertThat(cnt.getIp(), equalTo("192.168.1.1"));
    }

    @Test
    public void it()
    {
        repo.create(controller);

    }

    @Test
    @Transactional
    public void it_should_create_controller_by_upcasting()
    {
        ControllerConfig cnt = (ControllerConfig) controller;
        cnt.setIp("192.168.2.2");
        repo.create(cnt);
        ControllerConfig actCnt = repo.find(cnt.getId());

        assertNotNull(actCnt);
    }
}