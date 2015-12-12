package artronics.senator.services;

import artronics.gsdwn.model.ControllerConfig;

public interface ControllerConfigService
{
    ControllerConfig save(ControllerConfig controllerConfig);

    ControllerConfig find(Long id);

    void delete(Long id);

    ControllerConfig findByIp(String ip);
//
//    ControllerConfig updateControllerConfig(ControllerConfig controllerConfig);
//
    ControllerConfig getLatest();
}
