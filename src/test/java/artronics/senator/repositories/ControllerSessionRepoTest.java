package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryConfig.class})
@Profile("dev")
@WebAppConfiguration
public class ControllerSessionRepoTest
{
    @Autowired
    ControllerSessionRepo controllerSessionRepo;

    ControllerSession controllerSession;

    @Before
    public void setUp() throws Exception
    {
        controllerSession = new ControllerSession();
        controllerSession.setDescription("foo");
    }

    @Test
    public void it_should_create_session()
    {
        controllerSessionRepo.save(controllerSession);

        ControllerSession act = controllerSessionRepo.findOne(controllerSession.getId());

        assertNotNull(act);
    }

    @Test
    public void it_should_create_created_timestamp()
    {
        controllerSessionRepo.save(controllerSession);

        ControllerSession cnt = controllerSessionRepo.findOne(controllerSession.getId());

        assertNotNull(cnt.getCreated());
    }

    @Test
    public void test_all_fields()
    {
        controllerSessionRepo.save(controllerSession);

        ControllerSession act = controllerSessionRepo.findOne(controllerSession.getId());

        assertThat(act.getDescription(), equalTo("foo"));
    }

    //delete all tests related to pagination.PagingAndSortingRepository contains pagination.

    private void createSessions(String dsc, int num)
    {
        for (int i = 0; i < num; i++) {
            ControllerSession cs = new ControllerSession();
            cs.setDescription(dsc + i);
            controllerSessionRepo.save(cs);
        }
    }

    private void createSessionsWithDelay(String dsc, int num, long delay)
    {
        for (int i = 0; i < num; i++) {
            ControllerSession cs = new ControllerSession();
            cs.setDescription(dsc + i);
            controllerSessionRepo.save(cs);

            try {
                Thread.sleep(delay);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}