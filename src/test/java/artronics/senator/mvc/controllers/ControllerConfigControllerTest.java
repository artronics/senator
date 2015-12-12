package artronics.senator.mvc.controllers;

import artronics.chaparMini.DeviceConnectionConfig;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerStatus;
import artronics.senator.services.ControllerConfigList;
import artronics.senator.services.ControllerConfigService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    public void if_cont_doesnt_exist_return_stat_not_found() throws Exception
    {
        when(configService.find(1L)).thenReturn(null);

        mockMvc.perform(get("/rest/controllers/1"))
               .andExpect(status().isNotFound());

    }

    @Test
    public void get_list_of_controllersConfig() throws Exception
    {
        ControllerConfigList configList = createConfigs(10);

        when(configService.findAll()).thenReturn(configList);

        mockMvc.perform(get("/rest/controllers"))
                .andDo(print())
               .andExpect(jsonPath("$.controllers[*]", hasSize(10)))
               .andExpect(jsonPath("$.controllers[*].links[*].href",
                                   hasItems(endsWith("controllers/0"))))
               .andExpect(jsonPath("$.links[*].rel",hasItem(is("self"))))
               .andExpect(jsonPath("$.links[*].href",hasItem(endsWith("/controllers"))))

               .andExpect(status().isOk())
                ;
    }

    @Test
    public void get_all_controller_with_specific_status() throws Exception
    {
        ControllerConfigList configList = createConfigs(7,ControllerStatus.CONNECTED);

        when(configService.findByStatus(ControllerStatus.CONNECTED)).thenReturn(configList);

        mockMvc.perform(get("/rest/controllers?status=CONNECTED"))
                .andDo(print())
               .andExpect(jsonPath("$.controllers[*]",hasSize(7)))
                .andExpect(status().isOk());

    }
    private ControllerConfigList createConfigs(int num,ControllerStatus status)
    {
        List<ControllerConfig> configs = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ControllerConfig config = new ControllerConfig("198.187.12.23");
            config.setId(Integer.toUnsignedLong(i));
            config.setSinkAddress(0);
            config.setStatus(status);
            config.setDescription("foo: " + i);
            config.setConnectionConfig(new DeviceConnectionConfig("con string " + i));
            configs.add(config);
        }

        return new ControllerConfigList(configs);
    }

    private ControllerConfigList createConfigs(int num)
    {
        return createConfigs(num,ControllerStatus.CONNECTED);
    }
}