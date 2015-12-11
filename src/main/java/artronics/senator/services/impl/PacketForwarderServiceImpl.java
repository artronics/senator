package artronics.senator.services.impl;

import artronics.senator.mvc.resources.PacketRes;
import artronics.senator.services.PacketForwarderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PacketForwarderServiceImpl implements PacketForwarderService
{
    @Autowired
    RestTemplate restTemplate;

    @Override
    public PacketRes forwardPacket(String url)
    {
        return null;
    }
}
