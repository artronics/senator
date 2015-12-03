package artronics.senator.mvc.resources;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class PacketListRes extends ResourceSupport
{
    List<PacketRes> packets = new ArrayList<>();

    long lastPacketId;

    public List<PacketRes> getPackets()
    {
        return packets;
    }

    public void setPackets(
            List<PacketRes> packets)
    {
        this.packets = packets;
    }

    public long getLastPacketId()
    {
        return lastPacketId;
    }

    public void setLastPacketId(long lastPacketId)
    {
        this.lastPacketId = lastPacketId;
    }
}
