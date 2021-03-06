package artronics.senator.mvc.resources.asm;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.mvc.controllers.ControllerConfigController;
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

        res.setRid(controllerConfig.getId());
        res.setIp(controllerConfig.getIp());
        res.setDesc(controllerConfig.getDescription());

        res.setConnectionConfig(controllerConfig.getConnectionConfig());

        res.setStatus(controllerConfig.getStatus());
        res.setErrorMsg(controllerConfig.getErrorMsg());

        res.setCreated(controllerConfig.getCreated());
        res.setUpdated(controllerConfig.getUpdated());

        res.add(linkTo(ControllerConfigController.class).slash(controllerConfig.getId())
                                                        .withSelfRel());

        return res;
    }
}
