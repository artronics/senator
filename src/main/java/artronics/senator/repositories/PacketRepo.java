package artronics.senator.repositories;

import artronics.gsdwn.packet.PacketEntity;

public interface PacketRepo
{
    PacketEntity create(PacketEntity packet);

    PacketEntity find(Long id);
}
