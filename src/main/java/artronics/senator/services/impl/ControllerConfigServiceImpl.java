package artronics.senator.services.impl;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.repositories.ControllerConfigRepo;
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
    private ControllerConfigRepo controllerConfigRepo;


    @Override
    public ControllerConfig create(ControllerConfig controllerConfig)
    {
        return controllerConfigRepo.create(controllerConfig);
    }

    @Override
    public ControllerConfig find(Long id)
    {
        return controllerConfigRepo.find(id);
    }

    @Override
    public ControllerConfig findByIp(String ip)
    {
        return controllerConfigRepo.findByIp(ip);
    }

    @Override
    public ControllerConfig updateControllerConfig(ControllerConfig controllerConfig)
    {
        return controllerConfigRepo.update(controllerConfig);
    }

    @Override
    public ControllerConfig getLatest()
    {
        ControllerConfig cfg = controllerConfigRepo.getLatest();

        if (cfg != null) {
            return cfg;
        }

        throw new EntityNotFoundException();
    }
}
