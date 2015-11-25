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

    @Column(name = "controller_ip", unique = false, nullable = false)
//    @Pattern(regexp="^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}
// ([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }
}
