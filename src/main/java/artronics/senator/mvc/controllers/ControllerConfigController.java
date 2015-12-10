package artronics.senator.mvc.controllers;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.mvc.resources.ControllerConfigRes;
import artronics.senator.mvc.resources.asm.ControllerConfigResAsm;
import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/{cntId}", method = RequestMethod.GET)
    public ResponseEntity<ControllerConfigRes> getController(@PathVariable long cntId)
    {
        ControllerConfig cnf = controllerConfigService.find(cntId);

        if (cnf != null) {
            ControllerConfigRes configRes = new ControllerConfigResAsm().toResource(cnf);

            return new ResponseEntity<>(configRes, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
