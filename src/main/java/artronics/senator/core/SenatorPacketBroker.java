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

                    cntTxPackets.add(packet);
                }

            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    };

    @Autowired
    public SenatorPacketBroker(PacketService packetService,
                               Controller sdwnController)
    {
        this.packetService = packetService;
        this.sdwnController = sdwnController;

        this.cntTxPackets = sdwnController.getCntTxPacketsQueue();
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

    }

    @Override
    public BlockingQueue<SdwnBasePacket> getReceivedPacketsQueue()
    {
        return receivedPackets;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }
}
