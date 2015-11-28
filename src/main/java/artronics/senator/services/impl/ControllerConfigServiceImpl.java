package artronics.senator.services.impl;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.repositories.ControllerRepo;
import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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
    public ControllerConfig find(String ip)
    {
        return controllerRepo.find(ip);
    }

    @Override
    public ControllerConfig updateControllerConfig(ControllerConfig controllerConfig)
    {
        return controllerRepo.update(controllerConfig);
    }

    @Override
    public ControllerConfig getLatest()
    {
        ControllerConfig cfg = controllerRepo.getLatest();

        if (cfg != null) {
            return cfg;
        }

        throw new EntityNotFoundException();
    }
}
