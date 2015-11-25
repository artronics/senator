package artronics.senator.core;

import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PacketFactory;
import artronics.gsdwn.packet.SdwnPacketFactory;
import artronics.senator.model.SenatorPacketFactory;
import artronics.senator.model.SenatorSdwnPacketFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SenatorPacketBroker implements PacketBroker
{
    private final static List<Integer> POISON_PILL = new ArrayList<>();

    private final BlockingQueue<Packet> cntRxPackets;

    private final BlockingQueue<Packet> senRxPackets;
    private final PacketFactory packetFactory = new SdwnPacketFactory();
    private final SenatorPacketFactory senatorPacketFactory = new SenatorSdwnPacketFactory();
    public SenatorPacketBroker(
            BlockingQueue<Packet> cntRxPackets,
            BlockingQueue<Packet> senRxPackets)
    {
        this.cntRxPackets = cntRxPackets;
        this.senRxPackets = senRxPackets;
    }

    @Override
    public void run()
    {
            while (true) {

            }

        //TODO deal with exp and stop services
    }

    @Override
    public void stop()
    {

    }
}
