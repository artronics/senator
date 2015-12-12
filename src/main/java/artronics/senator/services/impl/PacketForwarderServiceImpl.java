package artronics.senator.services.impl;

import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.services.PacketForwarderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PacketForwarderServiceImpl implements PacketForwarderService
{
    @Autowired
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<PacketRes> forwardPacket(PacketRes packet)
    {
        String url = "http://" + packet.getDstIp() + "/rest/packets";
        ResponseEntity<PacketRes> res =
                restTemplate.postForEntity(url,packet,PacketRes.class);

        return res;
    }
}
