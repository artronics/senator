package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerConfig;

public interface ControllerConfigRepo
{
    ControllerConfig create(ControllerConfig controller);

    ControllerConfig find(Long id);

    ControllerConfig findByIp(String ip);

    ControllerConfig update(ControllerConfig newConfig);

    ControllerConfig getLatest();
}
