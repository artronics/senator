package artronics.senator.repositories;

import artronics.gsdwn.packet.SdwnBasePacket;

public interface PacketRepo
{
    SdwnBasePacket create(SdwnBasePacket packet);

    SdwnBasePacket find(Long id);
}
