package artronics.senator.services;

import artronics.senator.mvc.resources.PacketRes;
import org.springframework.http.ResponseEntity;

public interface PacketForwarderService
{
    ResponseEntity<PacketRes> forwardPacket(PacketRes packet);
}
