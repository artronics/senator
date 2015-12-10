package artronics.senator.services.impl;

import artronics.gsdwn.model.ControllerSession;
import artronics.senator.repositories.ControllerSessionRepo;
import artronics.senator.services.ControllerSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ControllerSessionServiceImpl implements ControllerSessionService
{
    @Autowired
    ControllerSessionRepo controllerSessionRepo;

    @Override
    public ControllerSession create(ControllerSession controllerSession)
    {
        return controllerSessionRepo.save(controllerSession);
    }

    @Override
    public ControllerSession find(Long id)
    {
        return controllerSessionRepo.findOne(id);
    }

    @Override
    public List<ControllerSession> paginate(int pageNumber, int pageSize)
    {
        return null;
    }
}
