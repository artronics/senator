package artronics.senator.mvc.resources.asm;

import artronics.gsdwn.model.ControllerSession;
import artronics.senator.mvc.resources.ControllerSessionRes;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

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

        return res;
    }
}
