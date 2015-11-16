package artronics.senator.services;

import artronics.gsdwn.packet.SdwnPacket;

public interface PacketService
{
    SdwnPacket create(SdwnPacket packet);
}
