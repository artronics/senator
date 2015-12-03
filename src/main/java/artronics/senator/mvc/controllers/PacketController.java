package artronics.senator.mvc.controllers;

import artronics.senator.mvc.resources.PacketListRes;
import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.mvc.resources.asm.PacketListResAsm;
import artronics.senator.services.PacketList;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/rest/packets")
public class PacketController
{
    private PacketService packetService;

    @Autowired
    public PacketController(PacketService packetService)
    {
        this.packetService = packetService;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"lastPacketId"})
    public ResponseEntity<PacketListRes> getAllPackets(@RequestParam long lastPacketId)
    {
        PacketList packetList = packetService.getNew(lastPacketId);

        PacketListRes packetListRes = new PacketListResAsm().toResource(packetList);

        return new ResponseEntity<PacketListRes>(packetListRes, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PacketListRes> getAllPackets()
    {
        PacketList packetList = packetService.getAllPackets();

        PacketListRes packetListRes = new PacketListResAsm().toResource(packetList);

        return new ResponseEntity<PacketListRes>(packetListRes, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PacketRes> sendPacket()
    {

        return new ResponseEntity<PacketRes>(HttpStatus.OK);
    }

}
