package artronics.senator.mvc.controllers;

import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/rest/controllers")
public class ControllerConfigController
{
    private ControllerConfigService controllerConfigService;

    @Autowired
    public ControllerConfigController(
            ControllerConfigService controllerConfigService)
    {
        this.controllerConfigService = controllerConfigService;
    }

    @RequestMapping(value = "{controllerIp}",method = RequestMethod.GET)
    public String getController(@PathVariable String controllerIp)
    {
        return "kir";
    }
}
