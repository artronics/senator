package artronics.senator.mvc.controllers;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/cnt")
public class ControllerConfigController
{
    private ControllerConfigService controllerConfigService;

    @Autowired
    public ControllerConfigController(
            ControllerConfigService controllerConfigService)
    {
        this.controllerConfigService = controllerConfigService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get()
    {
        ControllerConfig en = new ControllerConfig();
        en.setIp("uyi67");
        controllerConfigService.create(en);
        return "kir";
    }
}
