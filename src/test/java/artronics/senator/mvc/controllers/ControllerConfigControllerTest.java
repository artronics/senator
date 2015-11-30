package artronics.senator.mvc.controllers;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.services.ControllerConfigService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(locations="classpath:senator-beans.xml")
public class ControllerConfigControllerTest
{
    @InjectMocks
    ControllerConfigController configController;

    @Mock
    ControllerConfigService configService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(configController).build();
    }

    @Test
    public void get_controllerConfig() throws Exception
    {
        ControllerConfig config = new ControllerConfig("1.1.1.1");
        config.setId(1L);
        config.setDescription("foo");

        when(configService.find(1L)).thenReturn(config);

        mockMvc.perform(get("/rest/controllers/1"))
               .andExpect(jsonPath("$.desc", is("foo")))

               .andExpect(jsonPath("$.links[*].href",
                                   hasItem(endsWith("/controllers/1"))))

               .andExpect(jsonPath("$.links[*].rel",
                                   hasItems(is("self"))))

               .andExpect(status().isOk())
//               .andDo(print())
        ;

    }
}