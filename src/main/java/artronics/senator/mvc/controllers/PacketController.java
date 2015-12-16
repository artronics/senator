package artronics.senator.mvc.controllers;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.core.PacketBroker;
import artronics.senator.core.SenatorConfig;
import artronics.senator.mvc.resources.PacketListRes;
import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.mvc.resources.asm.PacketListResAsm;
import artronics.senator.mvc.resources.asm.PacketResAsm;
import artronics.senator.services.ControllerSessionService;
import artronics.senator.services.PacketForwarderService;
import artronics.senator.services.PacketList;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/packets")
public class PacketController
{
    private Long sessionId;

    private PacketService packetService;

    private ControllerSessionService sessionService;

    private PacketBroker packetBroker;

    private SenatorConfig config;

    private PacketForwarderService packetForwarder;

    @Autowired
    public PacketController(SenatorConfig config,
                            PacketService packetService,
                            PacketBroker packetBroker,
                            PacketForwarderService packetForwarder,
                            ControllerSessionService sessionService
                            )
    {
        this.packetService = packetService;
        this.packetBroker = packetBroker;
        this.config = config;
        this.packetForwarder = packetForwarder;
        this.sessionService = sessionService;

        this.packetBroker.start();
    }

    @CrossOrigin(origins = "http://localhost:9000")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<PacketRes> sendPacket(@Valid @RequestBody PacketRes sentPacket)
    {
        String ourIp = config.getControllerIp();
        String dstIp = sentPacket.getDstIp();

        if (dstIp.equals(ourIp)) {
            //ReceivedAt will be set as soon as packet hit the first server
            if (sentPacket.getReceivedAt() == null) {
                sentPacket.setReceivedAt(new Timestamp(new Date().getTime()));
            }
            //TODO change so it get sesseionId from service
            Long sessionId = sentPacket.getSessionId();
            if (sessionId==null){
                sentPacket.setSessionId(110L);
            }

            SdwnBasePacket packet = sentPacket.toSdwnBasePacket();

            //it cast SdwnBasePacket to its data type persist it and
            //add it to broker. Then return persisted packet which contains
            //id and etc.
            packet =persistAndAddToBroker(packet);

            PacketRes packetRes = new PacketResAsm().toResource(packet);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(packetRes.getLink("self").getHref()));

            return new ResponseEntity<PacketRes>(packetRes, headers, HttpStatus.CREATED);

        }else {
            ResponseEntity<PacketRes> receivedRes= packetForwarder.forwardPacket(sentPacket);

            return receivedRes;
        }
    }

    private SdwnBasePacket persistAndAddToBroker(SdwnBasePacket packet)
    {
        SdwnBasePacket persistedPacket = null;
        List<Integer> content = packet.getContent();

        switch (packet.getType())
        {
            case DATA:
//                SdwnDataPacket dataPacket=SdwnDataPacket.create(content) ;
//                SdwnDataPacket dataPacket = (SdwnDataPacket) packet ;
                SdwnBasePacket dataPacket = packet;
                 persistedPacket= packetService.create(dataPacket);
                packetBroker.addPacket(dataPacket);
                break;

            default:
        }

        return persistedPacket;
    }

    @CrossOrigin(origins = "http://localhost:9000")
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
