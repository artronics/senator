package artronics.senator.services.impl;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.repositories.PacketRepo;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PacketServiceImpl implements PacketService
{
    @Autowired
    PacketRepo packetRepo;

    @Override
    public SdwnBasePacket create(SdwnBasePacket packet)
    {
        return packetRepo.create(packet);
    }

    @Override
    public SdwnBasePacket find(Long id)
    {
        return packetRepo.find(id);
    }

    @Override
    public List<SdwnBasePacket> pagination(int pageNumber, int pageSize)
    {
        return packetRepo.pagination(pageNumber, pageSize);
    }
}
