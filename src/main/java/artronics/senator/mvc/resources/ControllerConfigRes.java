package artronics.senator.mvc.resources;

import artronics.gsdwn.model.ControllerConfig;
import org.springframework.hateoas.ResourceSupport;

public class ControllerConfigRes extends ResourceSupport
{
    //we can not use id because it clashes with supper class
    private Long rid;

    private String ip;

    private String desc;

    public ControllerConfig toControllerConfig()
    {
        ControllerConfig cnf = new ControllerConfig();

        cnf.setId(rid);
        cnf.setIp(ip);
        cnf.setDescription(desc);

        return cnf;
    }

    public Long getRid()
    {
        return rid;
    }

    public void setRid(Long rid)
    {
        this.rid = rid;
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
