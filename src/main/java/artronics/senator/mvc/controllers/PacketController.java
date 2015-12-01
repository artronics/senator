package artronics.senator.mvc.controllers;

import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PacketRes> sendPacket()
    {

        return new ResponseEntity<PacketRes>(HttpStatus.OK);
    }
}
