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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        ControllerConfig config = new ControllerConfig("192.168.3.2");

        when(configService.find("192.168.3.2")).thenReturn(config);

        mockMvc.perform(get("rest/controller/192.168.3.2"))
               .andExpect(status().isOk());
    }
}