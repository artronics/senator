package artronics.senator.mvc.controllers;

import artronics.gsdwn.model.ControllerSession;
import artronics.senator.config.TestRepositoryConfig;
import artronics.senator.services.ControllerSessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerSessionControllerTest
{
    @InjectMocks
    ControllerSessionController sessionController;

    @Mock
    ControllerSessionService sessionService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
    }

    @Test
    public void getSessionController() throws Exception
    {
        ControllerSession cs = new ControllerSession();
        cs.setId(1L);
        cs.setDescription("foo");

        when(sessionService.find(1L)).thenReturn(cs);

        mockMvc.perform(get("/rest/sessions/1"))
               .andExpect(jsonPath("$.description", is("foo")))

               .andExpect(jsonPath("$.links[*].href",
                                   hasItems(
                                           endsWith("/sessions/1")
                                   )))

               .andExpect(jsonPath("$.links[*].rel",
                                   hasItems(
                                           is("self")
                                   )))

               .andExpect(status().isOk());
    }

    @Test
    public void get_non_existence_session() throws Exception
    {
        when(sessionService.find(1L)).thenReturn(null);

        mockMvc.perform(get("/rest/sessions/1"))
               .andExpect(status().isNotFound());
    }
}