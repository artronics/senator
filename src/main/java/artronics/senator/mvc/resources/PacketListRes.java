package artronics.senator.mvc.resources;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class PacketListRes extends ResourceSupport
{
    List<PacketRes> packetResList = new ArrayList<>();

    public List<PacketRes> getPacketResList()
    {
        return packetResList;
    }

    public void setPacketResList(
            List<PacketRes> packetResList)
    {
        this.packetResList = packetResList;
    }
}
