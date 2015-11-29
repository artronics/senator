package artronics.senator.services;

import artronics.gsdwn.model.ControllerSession;

import java.util.List;

public interface ControllerSessionService
{
    ControllerSession create(ControllerSession controllerSession);

    ControllerSession find(Long id);

    List<ControllerSession> paginate(int pageNumber, int pageSize);
}
