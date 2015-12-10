package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerConfig;
import org.springframework.data.repository.CrudRepository;

public interface ControllerConfigRepo extends
                                      CrudRepository<ControllerConfig,Long>,
                                      ControllerConfigCustomRepo
{
}
