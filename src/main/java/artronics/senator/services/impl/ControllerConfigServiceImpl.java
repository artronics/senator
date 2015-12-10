package artronics.senator.services.impl;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.repositories.ControllerConfigRepo;
import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ControllerConfigServiceImpl implements ControllerConfigService
{
    @Autowired
    private ControllerConfigRepo controllerConfigRepo;


    @Override
    public ControllerConfig save(ControllerConfig controllerConfig)
    {
        return controllerConfigRepo.save(controllerConfig);
    }

    @Override
    public ControllerConfig find(Long id)
    {
        return controllerConfigRepo.findOne(id);
    }

//    @Override
//    public ControllerConfig findByIp(String ip)
//    {
//        return null;
//    }
//
//    @Override
//    public ControllerConfig updateControllerConfig(ControllerConfig controllerConfig)
//    {
//        return null;
//    }
//
    @Override
    public ControllerConfig getLatest()
    {
        return null;
    }
}
