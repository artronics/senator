package artronics.senator.mvc.resources;

import artronics.chaparMini.DeviceConnectionConfig;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerStatus;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

public class ControllerConfigRes extends ResourceSupport
{
    //we can not use id because it clashes with supper class
    private Long rid;

    private String ip;

    private int sinkAddress;

    private DeviceConnectionConfig connectionConfig;

    private String desc;

    private ControllerStatus status;

    private String errorMsg;

    private Date created;

    private Date updated;

    public ControllerConfig toControllerConfig()
    {
        ControllerConfig cnf = new ControllerConfig();

        cnf.setIp(ip);
        cnf.setDescription(desc);
        cnf.setSinkAddress(sinkAddress);
        cnf.setStatus(status);
        cnf.setConnectionConfig(connectionConfig);

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

    public int getSinkAddress()
    {
        return sinkAddress;
    }

    public void setSinkAddress(int sinkAddress)
    {
        this.sinkAddress = sinkAddress;
    }

    public DeviceConnectionConfig getConnectionConfig()
    {
        return connectionConfig;
    }

    public void setConnectionConfig(DeviceConnectionConfig connectionConfig)
    {
        this.connectionConfig = connectionConfig;
    }

    public ControllerStatus getStatus()
    {
        return status;
    }

    public void setStatus(ControllerStatus status)
    {
        this.status = status;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated)
    {
        this.updated = updated;
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
