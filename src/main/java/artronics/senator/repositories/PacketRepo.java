package artronics.senator.repositories;

import artronics.gsdwn.model.PacketEntity;

public interface PacketRepo
{
    PacketEntity create(PacketEntity packet);

    PacketEntity find(Long id);
}
