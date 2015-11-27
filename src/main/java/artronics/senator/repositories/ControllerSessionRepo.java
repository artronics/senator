package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerSession;

public interface ControllerSessionRepo
{
    ControllerSession create(ControllerSession controllerSession);

    ControllerSession find(Long id);
}
