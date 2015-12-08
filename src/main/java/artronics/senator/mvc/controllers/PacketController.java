package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.mvc.resources.PacketListRes;
import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.mvc.resources.asm.PacketListResAsm;
import artronics.senator.mvc.resources.asm.PacketResAsm;
import artronics.senator.services.PacketList;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

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

    @RequestMapping(value = "/{packetId}", method = RequestMethod.GET)
    public ResponseEntity<PacketRes> getPacket(@PathVariable long packetId)
    {
        SdwnBasePacket packet = packetService.find(packetId);
        PacketRes packetRes = new PacketResAsm().toResource(packet);

        return new ResponseEntity<PacketRes>(packetRes, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<PacketRes> sendPacket(@Valid @RequestBody PacketRes sentPacket)
    {
        SdwnBasePacket packet = packetService.create(sentPacket.toSdwnBasePacket());
        PacketRes packetRes = new PacketResAsm().toResource(packet);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(packetRes.getLink("self").getHref()));

        return new ResponseEntity<PacketRes>(packetRes, headers, HttpStatus.CREATED);
    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ValidationErrorListRes> err(){
//        return new ResponseEntity<>(new ValidationErrorListRes(), HttpStatus.BAD_REQUEST);
//
//    }

    @CrossOrigin(origins = "http://localhost:9000")
    @RequestMapping(method = RequestMethod.GET, params = {"lastPacketId"})
    public ResponseEntity<PacketListRes> getAllPackets(@RequestParam long lastPacketId)
    {
        PacketList packetList = packetService.getNew(lastPacketId);

        PacketListRes packetListRes = new PacketListResAsm().toResource(packetList);

        return new ResponseEntity<PacketListRes>(packetListRes, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:9000")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PacketListRes> getAllPackets()
    {
        PacketList packetList = packetService.getAllPackets();

        PacketListRes packetListRes = new PacketListResAsm().toResource(packetList);

        return new ResponseEntity<PacketListRes>(packetListRes, HttpStatus.OK);
    }
}
