package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
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

    }
}
