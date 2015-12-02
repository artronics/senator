package artronics.senator.services;

import artronics.gsdwn.packet.SdwnBasePacket;

import java.util.List;

public interface PacketService
{
    SdwnBasePacket create(SdwnBasePacket packet);

    SdwnBasePacket find(Long id);

    List<SdwnBasePacket> getNew(long lastPacketId, String controllerIp, long sessionId);

    List<SdwnBasePacket> getNew(long lastPacketId);

    List<SdwnBasePacket> pagination(int pageNumber, int pageSize);
}
