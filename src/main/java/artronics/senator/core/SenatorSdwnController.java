package artronics.senator.core;

import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PoisonPacket;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.model.SenatorPacket;
import artronics.senator.model.SenatorPacketFactory;
import artronics.senator.model.SenatorSdwnPacketFactory;
import artronics.senator.repositories.PacketRepo;

import javax.xml.ws.soap.Addressing;
import java.util.concurrent.BlockingQueue;

public class SenatorSdwnController implements SenatorController
{
    private final static PoisonPacket POISON_PACKET= new PoisonPacket();

    private final BlockingQueue<Packet> senRxPackets;
    private final BlockingQueue<Packet> senTxPackets;

    private final SenatorPacketFactory senPckFactory = new SenatorSdwnPacketFactory();

    @Addressing
    private PacketRepo packetRepo;

    public SenatorSdwnController(
            BlockingQueue<Packet> senRxPackets,
            BlockingQueue<Packet> senTxPackets)
    {
        this.senRxPackets = senRxPackets;
        this.senTxPackets = senTxPackets;
    }

    @Override
    public void run()
    {
        while (true){
            try {
                Packet packet = senRxPackets.take();
                if (packet == POISON_PACKET)
                    break;

                SenatorPacket senPacket = senPckFactory.create((SdwnBasePacket) packet);
                packetRepo.create(senPacket);

            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void stop()
    {
        senRxPackets.add(POISON_PACKET);

    }
}
