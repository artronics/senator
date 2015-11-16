package artronics.senator.model;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnPacket;

public class SenatorSdwnPacketFactory implements SenatorPacketFactory
{

    @Override
    public SenatorPacket create(SdwnBasePacket packet)
    {
        SenatorPacket sPacket = null;

        sPacket.setSrcShortAddress(packet.getSrcShortAddress());
        sPacket.setDstShortAddress(packet.getDstShortAddress());

        return sPacket;
    }
}
