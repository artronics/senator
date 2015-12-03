package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.services.PacketList;
import artronics.senator.services.PacketService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void get_recent_added_packets() throws Exception
    {
        PacketList packetList = createPacketList(10);

        when(packetService.getNew(1L)).thenReturn(packetList);

        mockMvc.perform(get("/rest/packets/recent?lastPacketId=1"))
               .andExpect(jsonPath("$.packets[*]", hasSize(10)))
               .andExpect(jsonPath("$.links[*].href",
                                   hasItems(
                                           endsWith("/packets/recent?lastPacketId=10")
                                   )))

               .andExpect(status().isOk())
               .andDo(print())
        ;
    }

    @Test
    public void test_lastPacketId_when_calling_recentPackets() throws Exception
    {
        PacketList packetList = createPacketList(10);

        when(packetService.getNew(1L)).thenReturn(packetList);

        mockMvc.perform(get("/rest/packets/recent?lastPacketId=1"))
               .andExpect(jsonPath("$.lastPacketId", is(10)))
        ;

        //Now if we get an empty result list. The value of lastPacketId
        //must be what it was before
        PacketList emptyList = new PacketList(10L, new ArrayList<SdwnBasePacket>());
        //ask for recent packets
        when(packetService.getNew(10L)).thenReturn(emptyList);

        mockMvc.perform(get("/rest/packets/recent?lastPacketId=10"))
               .andExpect(jsonPath("$.lastPacketId", is(10)))
               .andExpect(jsonPath("$.packets[*]", hasSize(0)))

               .andExpect(jsonPath("$.links[*].href",
                                   hasItems(
                                           endsWith("/packets/recent?lastPacketId=10")
                                   )))

               .andDo(print())
        ;


    }

    @Test
    public void send_packet()
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());

        when(packetService.find(1L)).thenReturn(packet);

//        mockMvc.perform(get("rest/packet"))
    }

    private PacketList createPacketList(int num)
    {
        List<SdwnBasePacket> dataPck = new ArrayList<>();
        for (int i = num; i > 0; i--) {
            SdwnBasePacket dataPacket = (SdwnBasePacket) packetFactory.createDataPacket(i, 0);
            dataPacket.setId(Integer.toUnsignedLong(i));
            dataPck.add(dataPacket);
        }
        PacketList packetList = new PacketList(Integer.toUnsignedLong(num), dataPck);

        return packetList;
    }
}