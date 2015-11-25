package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerEntity;

public interface ControllerRepo
{
    ControllerEntity create(ControllerEntity controller);

    ControllerEntity find(Long id);
}
