package artronics.senator.services;

import artronics.gsdwn.packet.SdwnBasePacket;

public interface PacketService
{
    SdwnBasePacket create(SdwnBasePacket packet);
}
