package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerSession;
import artronics.senator.services.ControllerConfigService;
import artronics.senator.services.ControllerSessionService;
import artronics.senator.services.PacketService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:senator-beans.xml")
////@WebAppConfiguration
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SenatorInitializerTest
{
    private  final static String ourIp="localhost:8080";

    @InjectMocks
    SenatorInitializer initializer;

    @Mock
    Controller sdwnController;
    @Mock
    ControllerConfigService configService;
    @Mock
    PacketService packetService;
    @Mock
    ControllerSessionService sessionService;
//    @Mock
    SenatorConfig config;

    @Before
    public void setUp() throws Exception
    {
        config = new SenatorConfig(ourIp,0,"str connection");
        initializer = new SenatorInitializer(config,sdwnController);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void if_there_is_no_controller_config_for_this_ip_it_should_crate_one(){
        when(configService.findByIp(ourIp)).thenReturn(null);
        initializer.init();

        verify(configService).findByIp(ourIp);
        verify(configService,times(1)).save(config.getControllerConfig());
        verifyNoMoreInteractions(configService);
    }
    @Test
    public void if_there_is_controller_config_for_this_ip_it_should_delete_and_create_new_one(){
        when(configService.findByIp(ourIp)).thenReturn(config.getControllerConfig());
        initializer.init();

        verify(configService).findByIp(ourIp);
        verify(configService,times(1)).save(config.getControllerConfig());
        verify(configService,times(1)).delete(any(Long.class));
        verifyNoMoreInteractions(configService);
    }
    @Test
    public void in_every_init_we_should_create_a_new_session(){
        when(configService.findByIp(ourIp)).thenReturn(config.getControllerConfig());
        initializer.init();

        verify(sessionService,times(1)).create(any(ControllerSession.class));
        verifyNoMoreInteractions(sessionService);
    }

}