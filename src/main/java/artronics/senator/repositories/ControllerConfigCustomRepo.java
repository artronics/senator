package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerConfig;

public interface ControllerConfigCustomRepo
{
    ControllerConfig getLatest();
}
