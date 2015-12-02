package artronics.senator.repositories;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.services.PacketList;

import java.util.List;

public interface PacketRepo
{
    SdwnBasePacket create(SdwnBasePacket packet);

    SdwnBasePacket find(Long id);

    List<SdwnBasePacket> pagination(int pageNumber, int pageSize);

    PacketList getNew(Long packetId, String ip, Long sessionId);

    PacketList getNew(Long packetId);
}
