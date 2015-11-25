package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerEntity;
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

    private ControllerEntity controller;

    @Before
    @Transactional
    @Rollback(value = false)
    public void setUp() throws Exception
    {
        controller = new ControllerEntity("192.168.1.1");

        repo.create(controller);
    }

    @Test
    @Transactional
    public void it_should_create_cont()
    {
        ControllerEntity cnt = repo.find(controller.getId());

        assertNotNull(cnt);
        assertThat(cnt.getIp(), equalTo("192.168.1.1"));
    }
}