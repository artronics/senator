package artronics.senator.services;

import artronics.gsdwn.packet.SdwnBasePacket;

import java.util.List;

public interface PacketService
{
    SdwnBasePacket create(SdwnBasePacket packet);

    List<SdwnBasePacket> pagination(int pageNumber, int pageSize);

    SdwnBasePacket find(Long id);
}
