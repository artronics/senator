package artronics.senator.repositories;

import artronics.gsdwn.packet.SdwnPacket;
import artronics.senator.model.SenatorPacket;

public interface PacketRepo
{
    SenatorPacket create(SenatorPacket packet);
}
