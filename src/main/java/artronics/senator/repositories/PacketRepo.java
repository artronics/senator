package artronics.senator.repositories;

import artronics.senator.model.SenatorPacket;

public interface PacketRepo
{
    SenatorPacket create(SenatorPacket packet);

    SenatorPacket find(Long id);
}
