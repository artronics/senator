package artronics.senator.repositories;

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


    ControllerSession controllerSession;


    @Before
    @Transactional
    @Rollback(false)
    public void setUp() throws Exception
    {
        controllerSession = new ControllerSession();
        controllerSession.setDescription("foo");
    }

    @Test
    @Transactional
    public void it_should_create_session()
    {
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
    public void test_pagination()
    {
        createSessions("foo", 3);

        List<ControllerSession> sessions = controllerSessionRepo.pagination(1, 10);

        assertThat(sessions.size(), equalTo(3));
    }

    @Test
    @Transactional
    public void test_pagination_it_should_return_max_result()
    {
        final int MAX_R = 10;
        createSessions("bar", 20);

        List<ControllerSession> sessions = controllerSessionRepo.pagination(1, MAX_R);

        assertThat(sessions.size(), equalTo(MAX_R));
    }

    @Test
    @Transactional
    public void test_pagination_it_should_return_latest()
    {
        final int MAX_R = 10;
        //since sessions are sorted by created date. we should
        //create each session with given some delay.
        createSessionsWithDelay("baz", 20, 500);

        List<ControllerSession> sessions = controllerSessionRepo.pagination(1, MAX_R);

        assertThat(sessions.get(0).getDescription(), equalTo("baz19"));
    }

    private void createSessions(String dsc, int num)
    {
        for (int i = 0; i < num; i++) {
            ControllerSession cs = new ControllerSession();
            cs.setDescription(dsc + i);
            controllerSessionRepo.create(cs);
        }
    }

    private void createSessionsWithDelay(String dsc, int num, long delay)
    {
        for (int i = 0; i < num; i++) {
            ControllerSession cs = new ControllerSession();
            cs.setDescription(dsc + i);
            controllerSessionRepo.create(cs);

            try {
                Thread.sleep(delay);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}