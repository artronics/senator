package artronics.senator.helper;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnPacketType;
import artronics.senator.mvc.controllers.RestErrorHandler;
import artronics.senator.mvc.resources.PacketRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

public class FakeRequest
{
    public static final String OUR_IP= "localhost:8080";
    public static final String OTHER_IP= "localhost:6464";

    public FakePacketFactory packetFactory = new FakePacketFactory();

    public SdwnBasePacket createPacket(String srcIp, String dstIp,
                                        SdwnPacketType type)
    {
        SdwnBasePacket dataPacket = (SdwnBasePacket) packetFactory.createDataPacket();
        dataPacket.setSrcIp(srcIp);
        dataPacket.setDstIp(dstIp);
        dataPacket.setSessionId(10L);
        dataPacket.setType(type);
        dataPacket.setReceivedAt(new Timestamp(new Date().getTime()));
        dataPacket.setCreatedAt(new Timestamp(new Date().getTime()));

        return dataPacket;

    }

    public SdwnBasePacket createPacket(String srcIp, String dstIp)
    {
        return createPacket(srcIp,dstIp,SdwnPacketType.DATA);
    }

    public SdwnBasePacket createPacket()
    {
        return createPacket(OUR_IP, OUR_IP);
    }

    public String createJsonPacket()
    {
        SdwnBasePacket dataPacket = createPacket();

        return createJsonPacket(dataPacket);
    }

    public static String createJsonPacket(PacketRes packetRes)
    {
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

    public static String createJsonPacket(SdwnBasePacket packet)
    {
        return createJsonPacket(createPacketRes(packet));
    }

    public static PacketRes createPacketRes(SdwnBasePacket packet)
    {
        PacketRes packetRes = new PacketRes();

        packetRes.setRid(packet.getId());

        packetRes.setSrcIp(packet.getSrcIp());
        packetRes.setDstIp(packet.getDstIp());

        packetRes.setSessionId(packet.getSessionId());
        packetRes.setReceivedAt(packet.getReceivedAt());
        packetRes.setCreatedAt(packet.getCreatedAt());

        packetRes.setSrcIp(packet.getSrcIp());
        packetRes.setDstIp(packet.getDstIp());

        packetRes.setNetId(packet.getNetId());
        packetRes.setType(packet.getType().toString());

        packetRes.setSrcShortAdd(packet.getSrcShortAddress());
        packetRes.setDstShortAdd(packet.getDstShortAddress());

        packetRes.setTtl(packet.getTtl());
        packetRes.setNextHop(packet.getNextHop());
        packetRes.setContent(packet.getContent());

        return packetRes;
    }
    public static ExceptionHandlerExceptionResolver createExceptionResolver()
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
