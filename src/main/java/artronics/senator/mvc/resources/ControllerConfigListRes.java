package artronics.senator.mvc.resources;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class ControllerConfigListRes extends ResourceSupport
{
    List<ControllerConfigRes> controllers = new ArrayList<>();

    public List<ControllerConfigRes> getControllers()
    {
        return controllers;
    }

    public void setControllers(
            List<ControllerConfigRes> controllers)
    {
        this.controllers = controllers;
    }
}
