package artronics.senator.services;

import artronics.senator.mvc.resources.PacketRes;

public interface PacketForwarderService
{
    PacketRes forwardPacket(String url);
}
