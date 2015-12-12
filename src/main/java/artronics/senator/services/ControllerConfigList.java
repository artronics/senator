package artronics.senator.services;

import artronics.gsdwn.model.ControllerConfig;

import java.util.List;

public class ControllerConfigList
{
    private List<ControllerConfig> controllerConfigs;

    public ControllerConfigList(
            List<ControllerConfig> controllerConfigs)
    {
        this.controllerConfigs = controllerConfigs;
    }

    public List<ControllerConfig> getControllerConfigs()
    {
        return controllerConfigs;
    }

    public void setControllerConfigs(
            List<ControllerConfig> controllerConfigs)
    {
        this.controllerConfigs = controllerConfigs;
    }
}
