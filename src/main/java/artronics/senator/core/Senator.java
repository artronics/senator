package artronics.senator.core;

import artronics.gsdwn.packet.Packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Senator
{
    //For Controller
    private final BlockingQueue<Packet> cntRxPackets = new LinkedBlockingQueue<>();
    private final BlockingQueue<Packet> cntTxPackets = new LinkedBlockingQueue<>();

    //For Senator Controller
    private final BlockingQueue<Packet> senRxPackets = new LinkedBlockingQueue<>();
    private final BlockingQueue<Packet> senTxPackets = new LinkedBlockingQueue<>();

    private final PacketBroker packetBroker = new SenatorPacketBroker(
            cntRxPackets,

            senRxPackets
    );

    private final SenatorController senController = new SenatorSdwnController(senRxPackets,senTxPackets);


    public void startThreads(){
        Thread pckBrokerThr =new Thread(packetBroker,"PckBroker");
        Thread senConThr = new Thread(senController,"SenContr");

        pckBrokerThr.start();
        senConThr.start();
    }

}
