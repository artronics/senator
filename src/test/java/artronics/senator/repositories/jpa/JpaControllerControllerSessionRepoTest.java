package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerSession;
import artronics.senator.repositories.ControllerRepo;
import artronics.senator.repositories.ControllerSessionRepo;
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
public class JpaControllerControllerSessionRepoTest
{
    @Autowired
    ControllerSessionRepo controllerSessionRepo;

    @Autowired
    ControllerRepo controllerRepo;

    ControllerConfig controllerConfig = new ControllerConfig();
    ControllerSession controllerSession;


    @Before
    @Transactional
    @Rollback(false)
    public void setUp() throws Exception
    {
        controllerConfig.setIp("192.168.1.1");
        controllerRepo.create(controllerConfig);

        controllerSession = new ControllerSession();
        controllerSession.setControllerConfig(controllerConfig);
        controllerSession.setDescription("foo");
    }

    @Test
    @Transactional
    public void it_should_create_session(){
        controllerSessionRepo.create(controllerSession);

        ControllerSession act = controllerSessionRepo.find(controllerSession.getId());

        assertNotNull(act);
    }

    @Test
    @Transactional
    public void it_should_create_created_timestamp()
    {
        controllerSessionRepo.create(controllerSession);

        ControllerSession cnt = controllerSessionRepo.find(controllerSession.getId());

        assertNotNull(cnt.getCreated());
    }

    @Test
    @Transactional
    public void test_all_fields()
    {
        controllerSessionRepo.create(controllerSession);

        ControllerSession act = controllerSessionRepo.find(controllerSession.getId());

        assertThat(act.getDescription(), equalTo("foo"));
    }
}