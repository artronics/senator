package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnPacketType;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.mvc.resources.asm.PacketResAsm;
import artronics.senator.services.PacketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:senator-beans.xml",
        "classpath:test/mvc-dispatcher-servlet.xml"})
public class PacketControllerPOSTTest
{
    @InjectMocks
    PacketController packetController;
    @Mock
    PacketService packetService;

    MockMvc mockMvc;

    FakePacketFactory packetFactory = new FakePacketFactory();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                 .dispatchOptions(true)
                                 .build();
    }


    @Test
    public void send_packet() throws Exception
    {
        mockMvc.perform(post("/rest/packets")
                                .content(createJsonPacket())
                                .contentType(MediaType.APPLICATION_JSON))

               .andDo(print())
               .andExpect(status().isCreated());
    }

    @Test//(expected = MethodArgumentNotValidException.class)
    public void send_packet_validation_test_SrcIp_must_be_notNull() throws Exception
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        packet.setId(1L);
        //we don't add srcIp
        String jsonPacket = createJsonPacket(packet);

        when(packetService.create(any(SdwnBasePacket.class))).thenReturn(packet);

        mockMvc.perform(post("/rest/packets")
                                .content(jsonPacket)
                                .contentType(MediaType.APPLICATION_JSON))

               .andDo(print())
               .andExpect(status().isBadRequest());

    }

    private String createJsonPacket()
    {
        SdwnBasePacket dataPacket = (SdwnBasePacket) packetFactory.createDataPacket();
        dataPacket.setSrcIp("192.168.13.12");
        dataPacket.setSessionId(10L);
        dataPacket.setType(SdwnPacketType.DATA);
        dataPacket.setReceivedAt(new Timestamp(new Date().getTime()));


        return createJsonPacket(dataPacket);
    }

    private String createJsonPacket(SdwnBasePacket packet)
    {
        PacketRes packetRes = new PacketResAsm().toResource(packet);
//        packetRes.setType(SdwnPacketType.DATA.toString());

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

