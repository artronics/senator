package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.services.PacketService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PacketControllerPOSTTest
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
    public void send_packet() throws Exception
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        packet.setId(1L);
        packet.setSrcIp("kjk");
        PacketRes packetRes = new PacketRes();

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(packet);

        mockMvc.perform(post("/rest/packets")
                                .content(createJsonPacket())
                                .contentType(MediaType.APPLICATION_JSON))

               .andDo(print())
               .andExpect(status().isCreated());
    }

    private String createJsonPacket() throws JSONException
    {
        JSONObject packet = new JSONObject();
        packet.put("srcIp", "128.123.2.2");

        System.out.println(packet.toString());
        return packet.toString();
    }
}

