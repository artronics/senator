package artronics.senator.services;

import artronics.gsdwn.model.ControllerSession;

public interface ControllerSessionService
{
    ControllerSession create(ControllerSession controllerSession);

    ControllerSession find(Long id);
}
