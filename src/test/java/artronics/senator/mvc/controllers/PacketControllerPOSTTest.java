package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnDataPacket;
import artronics.gsdwn.packet.SdwnPacketType;
import artronics.senator.core.PacketBroker;
import artronics.senator.core.SenatorConfig;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.helper.FakeRequest;
import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.services.PacketForwarderService;
import artronics.senator.services.PacketService;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators
        .withCreatedEntity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PacketControllerPOSTTest
{
    String ourIp = "localhost:8080";
    String otherIp = "127.0.0.1:9000";
    String urlToOtherIp = "http://" + otherIp + "/rest/packets";

    FakeRequest fakeRequest=new FakeRequest();
    MockRestServiceServer mockServer;

    MockMvc mockMvc;

    @InjectMocks
    PacketController packetController;
    FakePacketFactory packetFactory = new FakePacketFactory();
    @Mock
    private PacketService packetService;
    @Mock
    private PacketBroker packetBroker;
    @Mock
    private SenatorConfig config;
    @Mock
    private PacketForwarderService packetForwarder;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

//        mockServer = MockRestServiceServer.createServer(restTemplate);

        mockMvc = MockMvcBuilders.standaloneSetup(packetController)
                                 .dispatchOptions(true)
//                                 .setHandlerExceptionResolvers(createExceptionResolver())
                                 .defaultRequest(post("/rest")
                                                         .contentType(MediaType.APPLICATION_JSON))
                                 .build();

        when(config.getControllerIp()).thenReturn(ourIp);
    }

    @Test
    public void response_should_be_CREATED() throws Exception
    {
        SdwnBasePacket packet = fakeRequest.createPacket();//packet with ourIp
        packet.setId(1L);

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(packet);

        mockMvc.perform(post("/rest/packets")
                                .content(FakeRequest.createJsonPacket(packet)))

               .andDo(print())
               .andExpect(jsonPath("$.links[*].rel", hasItem(is("self"))))
               .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/packets/1"))))
               .andExpect(status().isCreated());

        verify(packetService, times(1)).create(any(SdwnBasePacket.class));
    }

    @Test
    public void if_dstIp_equals_ourIp_packet_should_be_persisted() throws Exception
    {
        SdwnBasePacket persistedPacket = fakeRequest.createPacket();
        persistedPacket.setId(1L);

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(persistedPacket);

        mockMvc.perform(post("/rest/packets")
                                .content(FakeRequest.createJsonPacket(persistedPacket)))
               .andExpect(status().isCreated());

        verify(packetService, times(1)).create(any(SdwnBasePacket.class));
    }

    @Test
    public void if_dstIp_is_not_ourIp_packet_should_not_be_persisted() throws Exception
    {
        SdwnBasePacket packet = fakeRequest.createPacket(ourIp, otherIp);

        mockMvc.perform(post("/rest/packets")
                                .content(FakeRequest.createJsonPacket(packet)));

        verify(packetService, times(0)).create(any(SdwnBasePacket.class));
    }

    /*
        We should persist packet and add it to PacketBroker.
        SdwnBasePacket is not appropriate, but we should create
        correct packet base on SdwnBasePacket type.
        For each packet's type there should be a test, however I'm
        gonna add them on demand.
     */
    @Test
    public void it_should_create_data_packet() throws Exception
    {
        //DataPacket
        SdwnBasePacket persistedPacket = fakeRequest.createPacket();
        persistedPacket.setType(SdwnPacketType.DATA);
        persistedPacket.setId(1L);

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(persistedPacket);

        mockMvc.perform(post("/rest/packets")
                                .content(FakeRequest.createJsonPacket(persistedPacket)));

        verify(packetService, times(1)).create(isA(SdwnDataPacket.class));
        verify(packetBroker,times(1)).addPacket(isA(SdwnDataPacket.class));
    }

    //This is a temp solution until we create validation layer
    @Test
    public void if_packet_is_not_match_sdwn_types_it_should_create_malformed_packet(){

    }

    /*
        In a real web app we should create a packet and send it with minimal
        fields. Remember generated packet from req is valid until it hits service.
     */
    @Test
    public void send_minimal_packet() throws Exception
    {
        PacketRes packet = new PacketRes();
        packet.setDstIp(ourIp);
        packet.setSrcIp(otherIp);
        packet.setType("DATA");
        packet.setSrcShortAdd(80);
        packet.setDstShortAdd(23);
        packet.setContent(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        packet.setTtl(20);
        packet.setNextHop(0);
        packet.setCreatedAt(new Timestamp(new Date().getTime()));


        SdwnBasePacket persistedPacket = fakeRequest.createPacket();
        persistedPacket.setId(1L);
        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(persistedPacket);

        mockMvc.perform(post("/rest/packets")
                                .content(FakeRequest.createJsonPacket(packet)))

               .andDo(print())
               .andExpect(status().isCreated())
        ;
    }


    //TODO fix validation tests
    /*
        VALIDATION
     */

    //TODO move this test to PacketForwarder test
    @Ignore
    @Test
    public void should_forward_packet_to_its_destination() throws URISyntaxException
    {
        PacketRes packetRes = new PacketRes();
        packetRes.setDstIp(otherIp);
        mockServer
                .expect(requestTo(urlToOtherIp))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withCreatedEntity(new URI("127.0.0.1")))
        ;

        packetController.sendPacket(packetRes);

        mockServer.verify();
    }

    //Exp handler hits but test doesn't work
    @Ignore
    @Test
    public void if_validation_fails_response_should_contained_original_sent_data() throws Exception
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        packet.setSessionId(10L);
        //Validation will fail because there are null values(like srcIp)
        String jsonPacket = FakeRequest.createJsonPacket(packet);

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(packet);

        mockMvc.perform(post("/rest/packets")
                                .content(jsonPacket))

               .andDo(print())

               .andExpect(jsonPath("$.data.srcIp").value(IsNull.nullValue()))
               .andExpect(status().isBadRequest());
    }

    @Ignore
    @Test
    public void send_packet_validation_test_SrcIp_must_be_notNull() throws Exception
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        packet.setId(1L);
        packet.setSessionId(10L);
        //we don't add srcIp
        String jsonPacket = FakeRequest.createJsonPacket(packet);

//        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(packet);

        mockMvc.perform(post("/rest/packets")
                                .content(jsonPacket))

               .andDo(print())

               .andExpect(jsonPath("$.data.srcIp").value(IsNull.nullValue()))
               .andExpect(status().isBadRequest());
    }

    private ExceptionHandlerExceptionResolver createExceptionResolver()
    {
        ExceptionHandlerExceptionResolver exceptionResolver = new
                ExceptionHandlerExceptionResolver()
        {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(
                    HandlerMethod handlerMethod, Exception exception)
            {
                Method method = new ExceptionHandlerMethodResolver(RestErrorHandler.class)
                        .resolveMethod(
                        exception);
                return new ServletInvocableHandlerMethod(new RestErrorHandler(), method);
            }
        };
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

}

