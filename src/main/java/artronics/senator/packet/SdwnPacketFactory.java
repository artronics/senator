package artronics.senator.packet;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SdwnPacketFactory implements PacketFactory
{
    @Override
    public Packet create(List<Integer> packetContent)
    {
        return null;
    }
}
