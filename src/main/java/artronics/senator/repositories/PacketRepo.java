package artronics.senator.repositories;

import artronics.gsdwn.packet.SdwnBasePacket;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PacketRepo extends PagingAndSortingRepository<SdwnBasePacket,Long>,PacketCustomRepo
{
}
