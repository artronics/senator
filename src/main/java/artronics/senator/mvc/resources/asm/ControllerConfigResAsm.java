package artronics.senator.mvc.resources.asm;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.mvc.resources.ControllerConfigRes;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class ControllerConfigResAsm extends
                                    ResourceAssemblerSupport<ControllerConfig, ControllerConfigRes>
{
    public ControllerConfigResAsm()
    {
        super(ControllerConfig.class, ControllerConfigRes.class);
    }

    @Override
    public ControllerConfigRes toResource(ControllerConfig controllerConfig)
    {
        ControllerConfigRes res = new ControllerConfigRes();

        res.setIp(controllerConfig.getIp());
        res.setDesc(controllerConfig.getDescription());

        res.add(linkTo(ControllerConfig.class).slash(controllerConfig.getIp()).withSelfRel());

        return res;
    }
}
