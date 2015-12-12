package artronics.senator.mvc.controllers;

import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerStatus;
import artronics.senator.mvc.resources.ControllerConfigListRes;
import artronics.senator.mvc.resources.ControllerConfigRes;
import artronics.senator.mvc.resources.asm.ControllerConfigListResAsm;
import artronics.senator.mvc.resources.asm.ControllerConfigResAsm;
import artronics.senator.services.ControllerConfigList;
import artronics.senator.services.ControllerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin(origins = "http://localhost:9000")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ControllerConfigListRes> getControllers(){
        ControllerConfigList configList = controllerConfigService.findAll();
        ControllerConfigListRes configListRes =
                new ControllerConfigListResAsm().toResource(configList);

        return new ResponseEntity<>(configListRes, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:9000")
    @RequestMapping(method = RequestMethod.GET,params = {"status"})
    public ResponseEntity<ControllerConfigListRes> getControllers(@RequestParam String status){
        ControllerStatus statusEn=ControllerStatus.valueOf(status.toUpperCase());

        ControllerConfigList configList = controllerConfigService.findByStatus(statusEn);

        ControllerConfigListRes configListRes =
                new ControllerConfigListResAsm().toResource(configList);

        return new ResponseEntity<>(configListRes, HttpStatus.OK);
    }
}
