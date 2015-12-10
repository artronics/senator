package artronics.senator.repositories;

import artronics.gsdwn.packet.SdwnBasePacket;

import java.util.List;

public interface PacketCustomRepo
{
    List<SdwnBasePacket> getNew(Long packetId, String ip, Long sessionId);

    List<SdwnBasePacket> getNew(Long packetId);
}
