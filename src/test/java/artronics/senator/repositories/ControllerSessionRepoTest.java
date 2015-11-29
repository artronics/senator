package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class ControllerSessionRepoTest
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

    @Test
    @Transactional
    public void test_findByControllerIp()
    {
        controllerSessionRepo.create(controllerSession);

        List<ControllerSession> act = controllerSessionRepo.findByControllerIp(controllerConfig
                                                                                       .getIp(),
                                                                               1,
                                                                               10);

        assertThat(act.size(), equalTo(1));
    }

    @Test
    @Transactional
    public void make_sure_findByControllerIp_returns_all_controllers()
    {

        createSessions("192.168.1.2", "foo", 10);
        createSessions("1.1.1.1", "bar", 4);

        List<ControllerSession> act = controllerSessionRepo.findByControllerIp("192.168.1.2",
                                                                               1,
                                                                               10);

        assertThat(act.size(), equalTo(10));
        assertThat(act.get(0).getDescription(), equalTo("foo"));
    }

    private void createSessions(String cntIp, String dsc, int num)
    {
        ControllerConfig cnt = new ControllerConfig(cntIp);
        controllerRepo.create(cnt);

        for (int i = 0; i < num; i++) {
            ControllerSession cs = new ControllerSession();
            cs.setControllerConfig(cnt);
            cs.setDescription(dsc);
            controllerSessionRepo.create(cs);
        }
    }

    private void createControllers(String ip, int num)
    {
        for (int i = 0; i < num; i++) {
            ControllerConfig cnt = new ControllerConfig(ip);
            controllerRepo.create(cnt);
        }
    }
}