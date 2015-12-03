package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerSession;

import java.util.List;

public interface ControllerSessionRepo
{
    ControllerSession create(ControllerSession controllerSession);

    ControllerSession find(Long id);

    List<ControllerSession> pagination(int pageNumber, int pageSize);
}
