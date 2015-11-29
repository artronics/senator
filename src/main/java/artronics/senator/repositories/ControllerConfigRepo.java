package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerConfig;

public interface ControllerConfigRepo
{
    ControllerConfig create(ControllerConfig controller);

    ControllerConfig find(String ip);

    ControllerConfig update(ControllerConfig newConfig);

    ControllerConfig getLatest();
}
