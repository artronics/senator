package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.services.PacketService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

public class PacketControllerTest
{
    @InjectMocks
    PacketController packetController;

    @Mock
    PacketService packetService;

    MockMvc mockMvc;

    FakePacketFactory packetFactory = new FakePacketFactory();

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(packetController).build();
    }

    @Test
    public void send_packet()
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());

        when(packetService.find(1L)).thenReturn(packet);

//        mockMvc.perform(get("rest/packet"))
    }
}