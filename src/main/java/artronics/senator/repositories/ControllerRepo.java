package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerConfig;

public interface ControllerRepo
{
    ControllerConfig create(ControllerConfig controller);

    ControllerConfig find(String ip);

    ControllerConfig update(ControllerConfig newConfig);

    ControllerConfig getLatest();
}
