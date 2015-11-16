package artronics.senator.services.impl;

import artronics.senator.model.SenatorPacket;
import artronics.senator.services.PacketService;
import artronics.senator.services.SenatorPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SenatorPersistenceImpl implements SenatorPersistence
{
    @Autowired
    PacketService packetService;

    @Override
    public SenatorPacket createSenatorPacket(SenatorPacket packet)
    {
        return null;
    }

    @Override
    public void run()
    {

    }
}
