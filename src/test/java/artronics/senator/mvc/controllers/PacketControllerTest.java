package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.core.PacketBroker;
import artronics.senator.core.SenatorConfig;
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
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
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

    @Mock
    SenatorConfig mockConfig;

    @Mock
    PacketBroker packetBroker;

    MockMvc mockMvc;

    FakePacketFactory packetFactory = new FakePacketFactory();

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(packetController).build();
    }

    @Test
    public void getPacket() throws Exception
    {
        SdwnBasePacket packet = (SdwnBasePacket) packetFactory.createDataPacket();
        packet.setId(1L);
        packet.setSessionId(10L);

        when(packetService.find(1L)).thenReturn(packet);

        mockMvc.perform(get("/rest/packets/1"))
               .andDo(print())
               .andExpect(jsonPath("$.links[*].rel",
                                   hasItems(is("self"))
               ))

               .andExpect(jsonPath("$.links[*].href",
                                   hasItems(endsWith("/packets/1"))))

               .andExpect(status().isOk())
        ;
    }

    @Test
    public void getAllPackets() throws Exception
    {
        PacketList packetList = createPacketList(10);

        when(packetService.getAllPackets()).thenReturn(packetList);

        mockMvc.perform(get("/rest/packets"))
               .andDo(print())

               .andExpect(jsonPath("$.packets[*]", hasSize(10)))

               .andExpect(jsonPath("$.packets[*].links[*].href",
                                   hasItems(endsWith("/packets/1"))))

               .andExpect(jsonPath("$.links[*].href",
                                   hasItems(
                                           endsWith("/packets"))
               ))

               .andExpect(jsonPath("$.links[*].rel",
                                   hasItems(
                                           is("self"))
               ))

               .andExpect(status().isOk())
        ;
    }

    @Test
    public void get_recent_added_packets() throws Exception
    {
        PacketList packetList = createPacketList(10);

        when(packetService.getNew(1L)).thenReturn(packetList);

        mockMvc.perform(get("/rest/packets?lastPacketId=1"))
               .andExpect(jsonPath("$.packets[*]", hasSize(10)))

               .andExpect(jsonPath("$.links[*].href",
                                   hasItems(
                                           endsWith("/packets")
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

        mockMvc.perform(get("/rest/packets?lastPacketId=1"))
               .andExpect(jsonPath("$.lastPacketId", is(10)))

               .andDo(print())
        ;

        //Now if we get an empty result list. The value of lastPacketId
        //must be what it was before
        PacketList emptyList = new PacketList(10L, new ArrayList<SdwnBasePacket>());
        //ask for recent packets
        when(packetService.getNew(10L)).thenReturn(emptyList);

        mockMvc.perform(get("/rest/packets?lastPacketId=10"))
               .andExpect(jsonPath("$.lastPacketId", is(10)))
               .andExpect(jsonPath("$.packets[*]", hasSize(0)))

               .andExpect(jsonPath("$.links[*].href",
                                   hasItems(
                                           endsWith("/packets")
                                   )))

               .andDo(print())
        ;
    }

    private PacketList createPacketList(int num)
    {
        List<SdwnBasePacket> dataPck = new ArrayList<>();
        for (int i = num; i > 0; i--) {
            SdwnBasePacket dataPacket = (SdwnBasePacket) packetFactory.createDataPacket(i, 0);
            dataPacket.setId(Integer.toUnsignedLong(i));
            dataPacket.setSessionId(Integer.toUnsignedLong(i));
            dataPck.add(dataPacket);
        }
        PacketList packetList = new PacketList(Integer.toUnsignedLong(num), dataPck);

        return packetList;
    }
}