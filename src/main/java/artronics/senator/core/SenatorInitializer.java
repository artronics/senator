package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PoisonPacket;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.services.ControllerConfigService;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.concurrent.BlockingQueue;

@Component
public class SenatorInitializer
{
    private final static PoisonPacket POISON_PILL = new PoisonPacket();

    private final Controller controller;
    private final BlockingQueue<Packet> cntRxQueue;
    private ControllerConfig controllerConfig;
    @Autowired
    private ControllerConfigService controllerService;
    @Autowired
    private PacketService packetService;
    private final Runnable peristence = new Runnable()
    {
        @Override
        public void run()
        {
            try {
                while (true) {
                    Packet packet = cntRxQueue.take();
                    if (packet == POISON_PILL)
                        break;

                    packetService.create((SdwnBasePacket) packet);
                }

            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    };

    @Autowired
    public SenatorInitializer(Controller controller)
    {
        this.controller = controller;

        this.cntRxQueue = controller.getCntRxPacketsQueue();
    }

    public void init()
    {
        //get latest config. if there is no config yet create one
        try {
            controllerConfig = controllerService.getLatest();

        }catch (EntityNotFoundException e) {
            controllerConfig = new ControllerConfig();
            controllerConfig.setIp("192.168.1.1");

            controllerService.create(controllerConfig);
        }

        controller.setConfig(controllerConfig);
    }

    public void start()
    {
        Thread persistenceThr = new Thread(peristence, "Persist");
        persistenceThr.start();
    }

    public void stop()
    {
        cntRxQueue.add(POISON_PILL);
    }
}
