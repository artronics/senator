package artronics.senator.services.impl;

import artronics.gsdwn.model.ControllerEntity;
import artronics.senator.repositories.ControllerRepo;
import artronics.senator.services.ControllerEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ControllerEntityServiceImpl implements ControllerEntityService
{
    @Autowired
    private ControllerRepo controllerRepo;


    @Override
    public ControllerEntity create(ControllerEntity controllerEntity)
    {
        ControllerEntity en = new ControllerEntity();
        en.setIp("k87");
        return controllerRepo.create(en);
    }
}
