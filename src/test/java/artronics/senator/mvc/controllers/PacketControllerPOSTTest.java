package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnPacketType;
import artronics.senator.config.TestBootApp;
import artronics.senator.config.TestRepositoryConfig;
import artronics.senator.core.PacketBroker;
import artronics.senator.core.SenatorBootApplication;
import artronics.senator.core.SenatorConfig;
import artronics.senator.core.config.BeanDefinition;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.mvc.resources.asm.PacketResAsm;
import artronics.senator.repositories.PacketRepo;
import artronics.senator.services.PacketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {TestRepositoryConfig.class} )
@ContextConfiguration(classes = {
        TestRepositoryConfig.class,
        BeanDefinition.class
}, loader = AnnotationConfigContextLoader.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@IntegrationTest("server.port:0")
public class PacketControllerPOSTTest
{
    String otherIp = "192.10.12.13";
    @InjectMocks
    PacketController packetController;
    @Mock
    PacketService packetService;
    @Mock
    PacketBroker packetBroker;
    @Mock
    SenatorConfig mockConfig;

    MockMvc mockMvc;
    FakePacketFactory packetFactory = new FakePacketFactory();
    @Autowired
    SenatorConfig config;
    @Autowired
    PacketRepo packetRepo;
    private String ourIp;

    @Autowired
    private WebApplicationContext webApplicationContext;

    public static <E> Collection<E> makeCollection(Iterable<E> iter)
    {
        Collection<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                 .dispatchOptions(true)
                                 .defaultRequest(post("/rest")
                                                         .contentType(MediaType.APPLICATION_JSON))
                                 .build();

        this.ourIp = config.getControllerIp();
    }

    @Test
    public void if_dstIp_equals_ourIp_packet_should_be_persisted() throws Exception
    {
        SdwnBasePacket expPacket = createPacket();

        mockMvc.perform(post("/rest/packets")
                                .content(createJsonPacket(expPacket)));

        List<SdwnBasePacket> packets = (List<SdwnBasePacket>) makeCollection(packetRepo.findAll());

        assertThat(packets.size(), equalTo(1));

        SdwnBasePacket persistedPacket = packets.get(0);
        FakePacketFactory.assertPacketEqual(expPacket, persistedPacket);
    }

    @Test
    public void if_dstIp_is_not_ourIp_packet_should_not_be_persisted() throws Exception
    {
        SdwnBasePacket packet = createPacket(ourIp, otherIp);

        mockMvc.perform(post("/rest/packets")
                                .content(createJsonPacket(packet)));

        List<SdwnBasePacket> packets = (List<SdwnBasePacket>) makeCollection(packetRepo.findAll());

        assertThat(packets.size(), equalTo(0));
    }


    /*
        VALIDATION
     */

    @Test
    public void response_should_be_CREATED() throws Exception
    {
        SdwnBasePacket packet = createPacket();//packet with ourIp

        mockMvc.perform(post("/rest/packets")
                                .content(createJsonPacket(packet)))

               .andDo(print())
               .andExpect(status().isCreated());
    }

    @Test
    public void if_validation_fails_response_should_contained_original_sent_data() throws Exception
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        packet.setSessionId(10L);
        //Validation will fail because there there are null values(like srcIp)
        String jsonPacket = createJsonPacket(packet);

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(packet);

        mockMvc.perform(post("/rest/packets")
                                .content(jsonPacket))

               .andDo(print())

               .andExpect(jsonPath("$.data.srcIp").value(IsNull.nullValue()))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void send_packet_validation_test_SrcIp_must_be_notNull() throws Exception
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        packet.setId(1L);
        packet.setSessionId(10L);
        //we don't add srcIp
        String jsonPacket = createJsonPacket(packet);

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(packet);

        mockMvc.perform(post("/rest/packets")
                                .content(jsonPacket))

               .andDo(print())

               .andExpect(jsonPath("$.data.srcIp").value(IsNull.nullValue()))
               .andExpect(status().isBadRequest());
    }

    private SdwnBasePacket createPacket(String srcIp, String dstIp)
    {
        SdwnBasePacket dataPacket = (SdwnBasePacket) packetFactory.createDataPacket();
        dataPacket.setSrcIp(srcIp);
        dataPacket.setDstIp(dstIp);
        dataPacket.setSessionId(10L);
        dataPacket.setType(SdwnPacketType.DATA);
        dataPacket.setReceivedAt(new Timestamp(new Date().getTime()));

        return dataPacket;
    }

    private SdwnBasePacket createPacket()
    {
        return createPacket(ourIp, ourIp);
    }

    private String createJsonPacket()
    {
        SdwnBasePacket dataPacket = createPacket();

        return createJsonPacket(dataPacket);
    }

    private String createJsonPacket(SdwnBasePacket packet)
    {
        PacketRes packetRes = new PacketResAsm().toResource(packet);

        ObjectMapper mapper = new ObjectMapper();
        String output = null;

        try {
            output = mapper.writeValueAsString(packetRes);
            System.out.println(output);

        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return output;
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
                        return new ServletInvocableHandlerMethod(new RestErrorHandler
                                                                         (),
                                                                 method);
                    }
                };
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }
}

