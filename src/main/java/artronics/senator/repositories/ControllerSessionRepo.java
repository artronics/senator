package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerSession;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ControllerSessionRepo extends PagingAndSortingRepository<ControllerSession,Long>
{
}
