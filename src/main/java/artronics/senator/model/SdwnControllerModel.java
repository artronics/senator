package artronics.senator.model;

import javax.persistence.*;

@Entity
@Table(name = "sdwn_controller")
public class SdwnControllerModel
{
    private Long id;
    private String ip;

    public SdwnControllerModel(String ip)
    {
        this.ip = ip;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    //TODO add validation
    @Column(name = "controller_ip", unique = false, nullable = false)
    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }
}
