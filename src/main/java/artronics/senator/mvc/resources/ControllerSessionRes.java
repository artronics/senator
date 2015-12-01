package artronics.senator.mvc.resources;

import artronics.gsdwn.model.ControllerSession;
import org.springframework.hateoas.ResourceSupport;

public class ControllerSessionRes extends ResourceSupport
{
    private long rid;

    private String description;

    public ControllerSession toControllerSession()
    {
        ControllerSession cs = new ControllerSession();

        cs.setId(rid);
        cs.setDescription(description);

        return cs;
    }

    public long getRid()
    {
        return rid;
    }

    public void setRid(long rid)
    {
        this.rid = rid;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
