package artronics.senator.mvc.resources;

import artronics.gsdwn.packet.SdwnBasePacket;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

public class PacketRes extends ResourceSupport
{
    private long rid;

    //Network Level
    @NotNull
    private String srcIp;

    private String dstIp;

    private long sessionId;

    //Controller level
    private Timestamp receivedAt;

    //Sdwn data
    private int netId;

    private int srcShortAdd;

    private int dstShortAdd;

    private int ttl;

    private int nextHop;

    private List<Integer> content;

    public SdwnBasePacket toSdwnBasePacket()
    {
        SdwnBasePacket packet = new SdwnBasePacket();

        packet.setSrcIp(srcIp);
        packet.setDstIp(dstIp);
        packet.setSessionId(sessionId);

        packet.setReceivedAt(receivedAt);

        packet.setNetId(netId);
        packet.setSrcShortAddress(srcShortAdd);
        packet.setDstShortAddress(dstShortAdd);
        packet.setTtl(ttl);
        packet.setNextHop(nextHop);
        packet.setContent(content);

        return packet;
    }

    public long getRid()
    {
        return rid;
    }

    public void setRid(long rid)
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

    public long getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(long sessionId)
    {
        this.sessionId = sessionId;
    }

    public int getNetId()
    {
        return netId;
    }

    public void setNetId(int netId)
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

    public int getSrcShortAdd()
    {
        return srcShortAdd;
    }

    public void setSrcShortAdd(int srcShortAdd)
    {
        this.srcShortAdd = srcShortAdd;
    }

    public int getDstShortAdd()
    {
        return dstShortAdd;
    }

    public void setDstShortAdd(int dstShortAdd)
    {
        this.dstShortAdd = dstShortAdd;
    }

    public int getTtl()
    {
        return ttl;
    }

    public void setTtl(int ttl)
    {
        this.ttl = ttl;
    }

    public int getNextHop()
    {
        return nextHop;
    }

    public void setNextHop(int nextHop)
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
