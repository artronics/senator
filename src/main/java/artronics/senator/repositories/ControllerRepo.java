package artronics.senator.repositories;

import artronics.senator.model.SdwnControllerModel;

public interface ControllerRepo
{
    SdwnControllerModel create(SdwnControllerModel controller);

    SdwnControllerModel find(Long id);
}
