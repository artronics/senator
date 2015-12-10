package artronics.senator.services.impl;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.repositories.PacketRepo;
import artronics.senator.services.PacketList;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
        return packetRepo.save(packet);
    }

    @Override
    public SdwnBasePacket find(Long id)
    {
        return packetRepo.findOne(id);
    }

    @Override
    public PacketList getAllPackets()
    {
//        List<SdwnBasePacket> packets = packetRepo.getAllPackets();
//        long lastPacketId = packets.get(packets.size() - 1).getId();
//        PacketList packetList = new PacketList(lastPacketId, packets);
//
//        return packetList;
        throw new NotImplementedException();
    }

    @Override
    public PacketList getNew(long lastPacketId, String controllerIp, long sessionId)
    {
        List<SdwnBasePacket> packets = packetRepo.getNew(lastPacketId, controllerIp, sessionId);

        return setLastPacketId(lastPacketId, packets);
    }

    @Override
    public PacketList getNew(long lastPacketId)
    {
        List<SdwnBasePacket> packets = packetRepo.getNew(lastPacketId);

        return setLastPacketId(lastPacketId, packets);
    }

    private PacketList setLastPacketId(long oldPacketId, List<SdwnBasePacket> packets)
    {
        long newLastPacketId = oldPacketId;

        if (!packets.isEmpty())
            newLastPacketId = packets.get(0).getId();

        PacketList packetList = new PacketList(newLastPacketId, packets);

        return packetList;
    }

    @Override
    public List<SdwnBasePacket> pagination(int pageNumber, int pageSize)
    {
//        return packetRepo.pagination(pageNumber, pageSize);
        throw new NotImplementedException();
    }
}
