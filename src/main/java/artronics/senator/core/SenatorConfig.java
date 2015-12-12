package artronics.senator.core;

import artronics.chaparMini.DeviceConnectionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SenatorConfig
{
    private String controllerIp;
    private DeviceConnectionConfig deviceConnectionConfig;

    @Autowired
    public SenatorConfig(@Value("${controller.ip}") String controllerIp,
                         @Value("${deviceConnection.url}") String deviceConnectionUrl)
    {
        this.controllerIp = controllerIp;
        this.deviceConnectionConfig = new DeviceConnectionConfig(deviceConnectionUrl);
    }

    public String getControllerIp()
    {
        return controllerIp;
    }

    public DeviceConnectionConfig getDeviceConnectionConfig()
    {
        return deviceConnectionConfig;
    }
}
