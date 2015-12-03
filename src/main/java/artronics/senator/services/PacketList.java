package artronics.senator.services;

import artronics.gsdwn.packet.SdwnBasePacket;

import java.util.List;

public class PacketList
{
    private long lastPacketId;
    private List<SdwnBasePacket> packets;


    public PacketList(long lastPacketId,
                      List<SdwnBasePacket> packets)
    {
        this.lastPacketId = lastPacketId;
        this.packets = packets;
    }

    public Long getLastPacketId()
    {
        return lastPacketId;
    }

    public void setLastPacketId(long lastPacketId)
    {
        this.lastPacketId = lastPacketId;
    }

    public List<SdwnBasePacket> getPackets()
    {
        return packets;
    }

    public void setPackets(List<SdwnBasePacket> packets)
    {
        this.packets = packets;
    }
}
