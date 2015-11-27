package artronics.senator.repositories;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;

public interface ControllerRepo
{
    ControllerConfig create(ControllerConfig controller);

    Controller create(Controller controller);

    ControllerConfig find(Long id);
}
