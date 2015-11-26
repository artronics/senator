package artronics.senator.mvc.controllers;

import artronics.gsdwn.model.ControllerEntity;
import artronics.senator.services.ControllerEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/cnt")
public class ControllerEntityController
{
    private ControllerEntityService controllerEntityService;

    @Autowired
    public ControllerEntityController(
            ControllerEntityService controllerEntityService)
    {
        this.controllerEntityService = controllerEntityService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get()
    {
        ControllerEntity en = new ControllerEntity();
        en.setIp("uyi67");
        controllerEntityService.create(en);
        return "kir";
    }
}
