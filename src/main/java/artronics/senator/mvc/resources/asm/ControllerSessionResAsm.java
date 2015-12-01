package artronics.senator.mvc.resources.asm;

import artronics.gsdwn.model.ControllerSession;
import artronics.senator.mvc.controllers.ControllerSessionController;
import artronics.senator.mvc.resources.ControllerSessionRes;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


public class ControllerSessionResAsm extends
                                     ResourceAssemblerSupport<ControllerSession,
                                             ControllerSessionRes>
{
    public ControllerSessionResAsm()
    {
        super(ControllerSession.class, ControllerSessionRes.class);
    }

    @Override
    public ControllerSessionRes toResource(ControllerSession controllerSession)
    {
        ControllerSessionRes res = new ControllerSessionRes();

        res.setRid(controllerSession.getId());
        res.setDescription(controllerSession.getDescription());

        res.add(linkTo(ControllerSessionController.class)
                        .slash(controllerSession.getId()).withSelfRel());

        return res;
    }
}
