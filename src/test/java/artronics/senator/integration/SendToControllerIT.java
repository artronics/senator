package artronics.senator.integration;

import artronics.senator.config.TestRepositoryConfig;
import artronics.senator.helper.FakeRequest;
import artronics.senator.mvc.controllers.PacketController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {TestRepositoryConfig.class})
//@ContextConfiguration(locations = {"classpath:senator-beans-test.xml"})
@IntegrationTest("server.port:0")   // 4
@Profile("dev")
public class SendToControllerIT
{
    @Autowired
    WebApplicationContext wac;

    @Autowired
    PacketController packetController;

    MockMvc mockMvc;

    //test utils and const
    FakeRequest fakeRequest = new FakeRequest();
    String ourIp = FakeRequest.OUR_IP;
    String otherIp = FakeRequest.OTHER_IP;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                                 .dispatchOptions(true)
                                 .defaultRequest(post("/rest")
                                                         .contentType(MediaType.APPLICATION_JSON))
                                 .build();
//        mockMvc = MockMvcBuilders.standaloneSetup(packetController)
//                                 .dispatchOptions(true)
//                                 .setHandlerExceptionResolvers(fakeRequest.createExceptionResolver())
//                                 .defaultRequest(post("/rest")
//                                                         .contentType(MediaType.APPLICATION_JSON))
//                                 .build();
    }

    @Test
    public void when_packet_has_same_ip_as_us() throws Exception
    {
        String packet = fakeRequest.createJsonPacket();

        mockMvc.perform(post("/rest/packets").content(packet))
               .andDo(print())
               .andExpect(status().isCreated())
        ;

    }
}
