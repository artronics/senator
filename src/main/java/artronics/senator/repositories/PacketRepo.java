package artronics.senator.repositories;

import artronics.gsdwn.packet.SdwnBasePacket;

import java.util.List;

public interface PacketRepo
{
    SdwnBasePacket create(SdwnBasePacket packet);

    SdwnBasePacket find(Long id);

    List<SdwnBasePacket> pagination(int pageNumber, int pageSize);

    List<SdwnBasePacket> getNew(Long packetId, String ip, Long sessionId);

    List<SdwnBasePacket> getNew(Long packetId);
}
