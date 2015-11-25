package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.Session;
import artronics.senator.repositories.SessionRepo;
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
public class JpaSessionRepoTest
{
    @Autowired
    SessionRepo sessionRepo;

    Session session;

    @Before
    @Transactional
    @Rollback(false)
    public void setUp() throws Exception
    {
        session = new Session();
        session.setDescription("foo");
    }

    @Test
    @Transactional
    public void it_should_create_session(){
        sessionRepo.create(session);

        Session act = sessionRepo.find(session.getId());

        assertNotNull(act);
        assertThat(act.getDescription(),equalTo("foo"));
    }
}