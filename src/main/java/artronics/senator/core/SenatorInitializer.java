package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SenatorInitializer
{
    private Controller controller;

    @Autowired
    private ControllerConfigService controllerService;

    @Autowired
    public SenatorInitializer(Controller controller)
    {
        this.controller = controller;
    }

    public void init()
    {
        ControllerConfig controllerConfig = (ControllerConfig) controller;
        controllerConfig.setIp("192.168.1.1");
        controllerService.create(controllerConfig);
    }
}
