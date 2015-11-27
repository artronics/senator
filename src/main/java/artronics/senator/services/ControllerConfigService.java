package artronics.senator.services;

import artronics.gsdwn.model.ControllerConfig;

public interface ControllerConfigService
{
    ControllerConfig create(ControllerConfig controllerConfig);

    ControllerConfig updateControllerConfig(ControllerConfig controllerConfig);

    ControllerConfig getLatest(ControllerConfig controllerConfig);
}
