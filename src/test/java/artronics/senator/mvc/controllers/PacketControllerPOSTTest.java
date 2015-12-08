package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.services.PacketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(packet);

        mockMvc.perform(post("/rest/packets")
                                .content(createJsonPacket())
                                .contentType(MediaType.APPLICATION_JSON))

               .andDo(print())
               .andExpect(status().isCreated());
    }

    @Test
    public void send_packet_validation_test_SrcIp_must_be_notNull() throws Exception
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        String jsonPacket = createJsonPacket(packet);

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(packet);

        mockMvc.perform(post("/rest/packets")
                                .content(jsonPacket)
                                .contentType(MediaType.APPLICATION_JSON))

               .andDo(print())
               .andExpect(status().isCreated());

    }

    private String createJsonPacket()
    {
        SdwnBasePacket dataPacket = (SdwnBasePacket) packetFactory.createDataPacket();
        dataPacket.setId(1L);
        dataPacket.setSrcIp("192.168.13.12");

        return createJsonPacket(dataPacket);
    }

    private String createJsonPacket(SdwnBasePacket packet)
    {
        ObjectMapper mapper = new ObjectMapper();
        String output = null;

        try {
            output = mapper.writeValueAsString(packet);
            System.out.println(output);

        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return output;
    }
}

