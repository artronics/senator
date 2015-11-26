package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerEntity;
import artronics.senator.repositories.PacketRepo;
import artronics.senator.services.ControllerEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SenatorInitializer
{
    private Controller controller;
    @Autowired
    private ControllerEntityService controllerService;
    @Autowired
    private PacketRepo packetRepo;

    public SenatorInitializer()
    {
    }

    //    @Autowired
    public SenatorInitializer(Controller controller)
    {
        this.controller = controller;
    }

    public void init()
    {
//        ControllerEntity controllerEntity = (ControllerEntity) controller ;
//        controllerEntity.setIp("192.168.1.1");
//        PacketEntity packet = new PacketEntity();
//        packet.setDstShortAddress(767);
//        packetRepo.create(packet);
        ControllerEntity en = new ControllerEntity();
        en.setIp("k87");
        controllerService.create(en);
    }
}
