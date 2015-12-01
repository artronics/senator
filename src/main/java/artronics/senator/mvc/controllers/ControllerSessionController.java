package artronics.senator.mvc.controllers;

import artronics.gsdwn.model.ControllerSession;
import artronics.senator.mvc.resources.ControllerSessionRes;
import artronics.senator.mvc.resources.asm.ControllerSessionResAsm;
import artronics.senator.services.ControllerSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/rest/sessions")
public class ControllerSessionController
{
    private ControllerSessionService sessionService;

    public ControllerSessionController(
            ControllerSessionService sessionService)
    {
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity<ControllerSessionRes> getSession(@PathVariable long sessionId)
    {
        ControllerSession cs = sessionService.find(sessionId);

        if (cs != null) {
            ControllerSessionRes res = new ControllerSessionResAsm().toResource(cs);

            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
