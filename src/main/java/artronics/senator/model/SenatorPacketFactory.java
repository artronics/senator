package artronics.senator.model;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnPacket;

public interface SenatorPacketFactory
{
    SenatorPacket create(SdwnBasePacket packet);
}
