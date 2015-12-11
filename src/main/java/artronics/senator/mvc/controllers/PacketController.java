package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.core.PacketBroker;
import artronics.senator.core.SenatorConfig;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/rest/packets")
public class PacketController
{
    private String ourIp;

    private PacketService packetService;

    private PacketBroker packetBroker;

    private SenatorConfig config;

    @Autowired
    public PacketController(SenatorConfig config,
                            PacketService packetService,
                            PacketBroker packetBroker)
    {
        this.packetService = packetService;
        this.packetBroker = packetBroker;
        this.config = config;

        this.ourIp = this.config.getControllerIp();
        this.packetBroker.start();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<PacketRes> sendPacket(@Valid @RequestBody PacketRes sentPacket)
    {
        String dstIp = sentPacket.getDstIp();

        if (dstIp.equals(ourIp)) {
            SdwnBasePacket packet = packetService.create(sentPacket.toSdwnBasePacket());

            packetBroker.addPacket(packet);

            PacketRes packetRes = new PacketResAsm().toResource(packet);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(packetRes.getLink("self").getHref()));

            return new ResponseEntity<PacketRes>(packetRes, headers, HttpStatus.CREATED);

        }else {

        }

        return new ResponseEntity<PacketRes>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{packetId}", method = RequestMethod.GET)
    public ResponseEntity<PacketRes> getPacket(@PathVariable long packetId)
    {
        SdwnBasePacket packet = packetService.find(packetId);
        PacketRes packetRes = new PacketResAsm().toResource(packet);

        return new ResponseEntity<PacketRes>(packetRes, HttpStatus.OK);
    }

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
