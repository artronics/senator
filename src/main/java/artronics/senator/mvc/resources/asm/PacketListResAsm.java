package artronics.senator.mvc.resources.asm;

import artronics.senator.mvc.controllers.PacketController;
import artronics.senator.mvc.resources.PacketListRes;
import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.services.PacketList;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

public class PacketListResAsm extends ResourceAssemblerSupport<PacketList, PacketListRes>
{
    public PacketListResAsm()
    {
        super(PacketController.class, PacketListRes.class);
    }

    @Override
    public PacketListRes toResource(PacketList packetList)
    {
        List<PacketRes> resources = new PacketResAsm().toResources(packetList.getPackets());

        PacketListRes res = new PacketListRes();
        res.setPacketResList(resources);

        return res;
    }
}
