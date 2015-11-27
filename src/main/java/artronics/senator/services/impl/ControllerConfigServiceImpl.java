package artronics.senator.services.impl;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.repositories.ControllerRepo;
import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ControllerConfigServiceImpl implements ControllerConfigService
{
    @Autowired
    private ControllerRepo controllerRepo;


    @Override
    public ControllerConfig create(ControllerConfig controllerConfig)
    {
        return controllerRepo.create(controllerConfig);
    }

    @Override
    public ControllerConfig updateControllerConfig(ControllerConfig controllerConfig)
    {
        return controllerRepo.update(controllerConfig);
    }

    @Override
    public ControllerConfig getLatest(ControllerConfig controllerConfig)
    {
        return null;
    }
}
