package artronics.senator.mvc.resources.asm;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.mvc.controllers.PacketController;
import artronics.senator.mvc.resources.PacketRes;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class PacketResAsm extends ResourceAssemblerSupport<SdwnBasePacket, PacketRes>
{
    public PacketResAsm()
    {
        super(SdwnBasePacket.class, PacketRes.class);
    }

    @Override
    public PacketRes toResource(SdwnBasePacket packet)
    {
        PacketRes res = new PacketRes();

        res.setRid(packet.getId());
        res.setNetId(packet.getNetId());
        res.setSrcShortAdd(packet.getSrcShortAddress());
        res.setDstShortAdd(packet.getDstShortAddress());
        res.setTtl(packet.getTtl());
        res.setNextHop(packet.getNextHop());
        res.setContent(packet.getContent());

        res.add(linkTo(PacketController.class).slash(packet.getId()).withSelfRel());

        return res;
    }
}
