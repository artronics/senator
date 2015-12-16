package artronics.senator.mvc.resources;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnPacketType;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

public class PacketRes extends ResourceSupport
{
    private Long rid;

    //Network Level
    @NotNull
    private String srcIp;

    @NotNull
    private String dstIp;

    @NotNull
    private Timestamp createdAt;

    private long sessionId;

    //Controller level
    private Timestamp receivedAt;

    //Sdwn data
    private Integer netId;

    private Integer srcShortAdd;

    private Integer dstShortAdd;

    private String type;

    private Integer ttl;

    private Integer nextHop;

    private List<Integer> content;

    public SdwnBasePacket toSdwnBasePacket()
    {
        SdwnBasePacket packet = new SdwnBasePacket();

        packet.setSrcIp(srcIp);
        packet.setDstIp(dstIp);
        packet.setSessionId(sessionId);

        packet.setCreatedAt(createdAt);
        packet.setReceivedAt(receivedAt);

        packet.setNetId(netId);
        packet.setType(SdwnPacketType.valueOf(type));
        packet.setSrcShortAddress(srcShortAdd);
        packet.setDstShortAddress(dstShortAdd);
        packet.setTtl(ttl);
        packet.setNextHop(nextHop);
        packet.setContent(content);

        return packet;
    }

    public Long getRid()
    {
        return rid;
    }

    public void setRid(Long rid)
    {
        this.rid = rid;
    }

    public String getSrcIp()
    {
        return srcIp;
    }

    public void setSrcIp(String srcIp)
    {
        this.srcIp = srcIp;
    }

    public String getDstIp()
    {
        return dstIp;
    }

    public void setDstIp(String dstIp)
    {
        this.dstIp = dstIp;
    }

    public Timestamp getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt)
    {
        this.createdAt = createdAt;
    }

    public long getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(long sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Integer getNetId()
    {
        return netId;
    }

    public void setNetId(Integer netId)
    {
        this.netId = netId;
    }

    public Timestamp getReceivedAt()
    {
        return receivedAt;
    }

    public void setReceivedAt(Timestamp receivedAt)
    {
        this.receivedAt = receivedAt;
    }

    public Integer getSrcShortAdd()
    {
        return srcShortAdd;
    }

    public void setSrcShortAdd(Integer srcShortAdd)
    {
        this.srcShortAdd = srcShortAdd;
    }

    public Integer getDstShortAdd()
    {
        return dstShortAdd;
    }

    public void setDstShortAdd(Integer dstShortAdd)
    {
        this.dstShortAdd = dstShortAdd;
    }

    public Integer getTtl()
    {
        return ttl;
    }

    public void setTtl(Integer ttl)
    {
        this.ttl = ttl;
    }

    public Integer getNextHop()
    {
        return nextHop;
    }

    public void setNextHop(Integer nextHop)
    {
        this.nextHop = nextHop;
    }

    public List<Integer> getContent()
    {
        return content;
    }

    public void setContent(List<Integer> content)
    {
        this.content = content;
    }
}
