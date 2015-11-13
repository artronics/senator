package artronics.senator.packet;

import java.util.List;

public interface PacketFactory
{
    Packet create(List<Integer> packetContent);
}
