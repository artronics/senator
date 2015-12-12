package artronics.senator.services;

import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerStatus;

public interface ControllerConfigService
{
    ControllerConfig save(ControllerConfig controllerConfig);

    ControllerConfig find(Long id);

    ControllerConfigList findAll();

    void delete(Long id);

    ControllerConfig findByIp(String ip);

    ControllerConfigList findByStatus(ControllerStatus status);
//
//    ControllerConfig updateControllerConfig(ControllerConfig controllerConfig);
//
    ControllerConfig getLatest();
}
