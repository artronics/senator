package artronics.senator.mvc.resources.asm;

import artronics.senator.mvc.controllers.PacketController;
import artronics.senator.mvc.resources.PacketListRes;
import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.services.PacketList;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
        res.setPackets(resources);
        res.setLastPacketId(packetList.getLastPacketId());

//when we use RequestParam link to controller stay the same.
        //TODO find a way so links also reflect RequestParam
        res.add(linkTo(
                methodOn(PacketController.class).getAllPackets())
                        .withSelfRel());

        return res;
    }
}
