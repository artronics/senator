package artronics.senator.mvc.resources.asm;

import artronics.senator.mvc.controllers.ControllerConfigController;
import artronics.senator.mvc.resources.ControllerConfigListRes;
import artronics.senator.mvc.resources.ControllerConfigRes;
import artronics.senator.services.ControllerConfigList;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ControllerConfigListResAsm extends
                                        ResourceAssemblerSupport
                                                <ControllerConfigList, ControllerConfigListRes>
{
    public ControllerConfigListResAsm()
    {
        super(ControllerConfigController.class, ControllerConfigListRes.class);
    }

    @Override
    public ControllerConfigListRes toResource(ControllerConfigList controllerConfigList)
    {
        List<ControllerConfigRes> resources =
                new ControllerConfigResAsm().toResources(controllerConfigList.getControllerConfigs());

        ControllerConfigListRes res = new ControllerConfigListRes();
        res.setControllers(resources);

        res.add(linkTo(
                methodOn(ControllerConfigController.class).getControllers())
                        .withSelfRel());

        return res;
    }
}
