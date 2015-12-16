package artronics.senator.core;

import artronics.chaparMini.exceptions.DeviceConnectionException;
import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerSession;
import artronics.gsdwn.model.ControllerStatus;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PoisonPacket;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.services.ControllerConfigService;
import artronics.senator.services.ControllerSessionService;
import artronics.senator.services.PacketService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class SenatorInitializer implements ApplicationListener<ContextRefreshedEvent>
{
    private final static Logger log = Logger.getLogger(SenatorInitializer.class);

    private final static SdwnBasePacket POISON_PILL = (SdwnBasePacket) new PoisonPacket();

//    private final Controller controller;
    private final BlockingQueue<Packet> cntRxQueue;
    private ControllerConfig controllerConfig;


    private String controllerIp;
    private Long sessionId;

    //At runtime we need a session. default constructor makes a default session.
    //set your desire session and set it before calling start method.
    private ControllerSession controllerSession;

    private SenatorConfig senatorConfig;

    private Controller controller;

    @Autowired
    private ControllerConfigService controllerService;

    @Autowired
    private ControllerSessionService sessionService;

    @Autowired
    private PacketService packetService;


    private final Runnable persistence = new Runnable()
    {
        @Override
        public void run()
        {
            try {
                while (true) {
                    final SdwnBasePacket packet = (SdwnBasePacket) cntRxQueue.take();
                    if (packet == POISON_PILL)
                        break;

                    //add current session to packet and controllerIp
                    packet.setSrcIp(controllerIp);
                    //if there is no dstIp then source and destination are the same
                    //i.e packet is a local packet
                    if (packet.getDstIp() == null) {
                        packet.setDstIp(controllerIp);
                    }
                    packet.setSessionId(sessionId);
                    packetService.create(packet);
                }

            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Autowired
    public SenatorInitializer(SenatorConfig senatorConfig,
    Controller controller)
    {

        this.senatorConfig = senatorConfig;

        this.controllerConfig = senatorConfig.getControllerConfig();

        this.controller = controller;
        this.controller.setConfig(controllerConfig);

        this.cntRxQueue = controller.getCntRxPacketsQueue();

        this.controllerIp = senatorConfig.getControllerIp();

        //Start threads related to persist packets
        Thread persistenceThr = new Thread(persistence, "Persist");
        persistenceThr.start();
    }

    public void init()
    {
        //with each run we need a new session
        log.debug("create new session.");
        controllerSession = new ControllerSession();

        //look if there is any controller for this ip
        ControllerConfig preConfig = controllerService.findByIp(controllerIp);

        if (preConfig == null) {
            log.debug("No Controller configuration found in DB with ip:" + controllerIp);
            controllerService.save(controllerConfig);
            log.debug("New Controller Configuration has created with default data.");
        }
        else {
            //delete the old one and create a new one
            controllerService.delete(preConfig.getId());
            controllerService.save(controllerConfig);
        }

        sessionService.create(controllerSession);

        sessionId = controllerSession.getId();
    }

    public void startSdwnController()
    {
        try {
            controller.start();

        }catch (DeviceConnectionException e) {
            log.error("SDWN-Controller failed");
            log.error(e.getMessage());
            e.printStackTrace();

            controllerConfig.setStatus(ControllerStatus.ERROR);
            controllerConfig.setErrorMsg(e.getMessage());

            controllerService.save(controllerConfig);
            return;
        }
        log.debug("Controller is connected to device.");
        controllerConfig.setStatus(ControllerStatus.CONNECTED);
        controllerService.save(controllerConfig);

    }

    public void stop()
    {
        cntRxQueue.add(POISON_PILL);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)
    {
        log.debug("Senator is Initializing.");
        init();
        log.debug("Starting controller...");
        startSdwnController();
    }
}
