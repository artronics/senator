package artronics.senator.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SenatorConfig
{
    private String controllerIp;

    @Autowired
    public SenatorConfig(@Value("${controller.ip}") String controllerIp)
    {
        this.controllerIp = controllerIp;
    }

    public String getControllerIp()
    {
        return controllerIp;
    }
}
