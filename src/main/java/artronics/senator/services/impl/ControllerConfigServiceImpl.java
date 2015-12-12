package artronics.senator.services.impl;

import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerStatus;
import artronics.senator.helpers.CollectionHelper;
import artronics.senator.repositories.ControllerConfigRepo;
import artronics.senator.services.ControllerConfigList;
import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

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


    @Override
    public ControllerConfigList findAll()
    {
        List<ControllerConfig> configList = (List<ControllerConfig>)
                CollectionHelper.makeCollection(controllerConfigRepo.findAll());

        return new ControllerConfigList(configList);
    }

    @Override
    public void delete(Long id)
    {
        controllerConfigRepo.delete(id);
    }

    @Override
    public ControllerConfig findByIp(String ip)
    {
        return controllerConfigRepo.findByIp(ip);
    }

    @Override
    public ControllerConfigList findByStatus(ControllerStatus status)
    {
        List<ControllerConfig> configs = controllerConfigRepo.findByStatus(status);

        return new ControllerConfigList(configs);
    }

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
        throw new NotImplementedException();
    }
}
