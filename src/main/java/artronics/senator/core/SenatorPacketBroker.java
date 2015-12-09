package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PoisonPacket;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class SenatorPacketBroker implements PacketBroker
{
    private final static PoisonPacket POISON_PACKET = new PoisonPacket();
    private final PacketService packetService;
    private final Controller sdwnController;
    private final BlockingQueue<Packet> cntTxPackets;
    private SenatorConfig config;
    private String ip;
    private BlockingQueue<SdwnBasePacket> receivedPackets =
            new LinkedBlockingQueue<>();
    private final Runnable broker = new Runnable()
    {
        @Override
        public void run()
        {
            try {
                while (true) {
                    SdwnBasePacket packet = receivedPackets.take();
                    if (packet == POISON_PACKET)
                        break;

                    processPacket(packet);
                }

            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    };

    @Autowired
    public SenatorPacketBroker(SenatorConfig config,
                               PacketService packetService,
                               Controller sdwnController)
    {
        this.config = config;
        this.packetService = packetService;
        this.sdwnController = sdwnController;

        this.cntTxPackets = sdwnController.getCntTxPacketsQueue();

        this.ip = config.getControllerIp();
    }

    private void processPacket(SdwnBasePacket packet)
    {
        if (packet.getSrcIp().equals(ip)) {
            cntTxPackets.add(packet);
            packetService.create(packet);
        }
    }

    @Override
    public void addPacket(SdwnBasePacket packet)
    {
        receivedPackets.add(packet);
    }

    @Override
    public void start()
    {
        Thread brokerThr = new Thread(broker, "Pck Broker");
        brokerThr.start();

    }

    @Override
    public void stop()
    {
        receivedPackets.add(POISON_PACKET);
    }

    @Override
    public BlockingQueue<SdwnBasePacket> getReceivedPacketsQueue()
    {
        return receivedPackets;
    }
}
