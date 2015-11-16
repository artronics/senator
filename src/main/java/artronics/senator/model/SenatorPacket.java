package artronics.senator.model;

import javax.persistence.*;

@Entity
@Table(name = "packets")
public class SenatorPacket
{
    private Long id;
    private Integer srcShortAddress;
    private Integer dstShortAddress;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,unique = true)
    public Long getId()
    {
        return id;
    }

    @Column(name = "src_short_add")
    public Integer getSrcShortAddress()
    {
        return srcShortAddress;
    }

    @Column(name = "dst_short_add")
    public Integer getDstShortAddress()
    {
        return dstShortAddress;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setSrcShortAddress(Integer srcShortAddress)
    {
        this.srcShortAddress = srcShortAddress;
    }

    public void setDstShortAddress(Integer dstShortAddress)
    {
        this.dstShortAddress = dstShortAddress;
    }
}
