package artronics.senator.core;

import artronics.chaparMini.DeviceConnectionConfig;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SenatorConfig
{
    private String controllerIp;
    private ControllerConfig controllerConfig;
    private DeviceConnectionConfig deviceConnectionConfig;

    @Autowired
    public SenatorConfig(@Value("${controller.ip}") String controllerIp,
                         @Value("${controller.sinkAddress}") int sinkAddress,
                         @Value("${deviceConnection.connectionString}") String connectionString)
    {
        //Ip is already in controllerConfig. we just use it as a separate for ease of access
        this.controllerIp = controllerIp;

        this.deviceConnectionConfig = new DeviceConnectionConfig(connectionString);

        this.controllerConfig = new ControllerConfig(controllerIp);

        controllerConfig.setConnectionConfig(this.deviceConnectionConfig);
        controllerConfig.setSinkAddress(sinkAddress);
        controllerConfig.setStatus(ControllerStatus.NOT_CONNECTED);
    }

    public String getControllerIp()
    {
        return controllerIp;
    }

    public DeviceConnectionConfig getDeviceConnectionConfig()
    {
        return deviceConnectionConfig;
    }

    public ControllerConfig getControllerConfig()
    {
        return controllerConfig;
    }
}
