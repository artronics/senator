package artronics.senator.core;

import artronics.chaparMini.DeviceConnectionConfig;
import artronics.gsdwn.model.ControllerConfig;
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
                         @Value("${deviceConnection.connectionString}") String deviceConnectionUrl)
    {
        this.controllerIp = controllerIp;

        this.controllerConfig = new ControllerConfig(controllerIp);
        controllerConfig.setSinkAddress(sinkAddress);

        this.deviceConnectionConfig = new DeviceConnectionConfig(deviceConnectionUrl);
        controllerConfig.setConnectionConfig(this.deviceConnectionConfig);
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
