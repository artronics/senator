package artronics.senator.services.impl;

import artronics.gsdwn.packet.SdwnPacket;
import artronics.senator.repositories.PacketRepo;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PacketServiceImpl implements PacketService
{
    @Autowired
    PacketRepo packetRepo;

    @Override
    public SdwnPacket create(SdwnPacket packet)
    {
        return null;
    }
}
