package artronics.senator.services;

import artronics.gsdwn.packet.SdwnBasePacket;

import java.util.List;

public class PacketList
{
    private String controllerIp;
    private List<SdwnBasePacket> packets;

    public PacketList(String controllerIp,
                      List<SdwnBasePacket> packets)
    {
        this.controllerIp = controllerIp;
        this.packets = packets;
    }

    public String getControllerIp()
    {
        return controllerIp;
    }

    public void setControllerIp(String controllerIp)
    {
        this.controllerIp = controllerIp;
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
