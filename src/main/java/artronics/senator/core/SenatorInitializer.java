package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
public class SenatorInitializer
{
    private final Controller controller;

    private ControllerConfig controllerConfig;

    @Autowired
    private ControllerConfigService controllerService;

    @Autowired
    public SenatorInitializer(Controller controller)
    {
        this.controller = controller;
    }

    public void init()
    {
        //get latest config. if there is no config yet create one
        try {
            controllerConfig = controllerService.getLatest();

        }catch (EntityNotFoundException e) {
            controllerConfig = new ControllerConfig();
            controllerConfig.setIp("192.168.1.1");

            controllerService.create(controllerConfig);
        }


    }
}
