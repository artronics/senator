package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerStatus;

import java.util.List;

public interface ControllerConfigCustomRepo
{
    ControllerConfig getLatest();

    ControllerConfig findByIp(String ip);

    List<ControllerConfig> findByStatus(ControllerStatus connected);
}
