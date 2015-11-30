package artronics.senator.mvc.resources;

import artronics.gsdwn.model.ControllerConfig;
import org.springframework.hateoas.ResourceSupport;

public class ControllerConfigRes extends ResourceSupport
{
    private String ip;

    private String desc;

    public ControllerConfig toControllerConfig()
    {
        ControllerConfig cnf = new ControllerConfig();

        cnf.setIp(ip);
        cnf.setDesc(desc);

        return cnf;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
