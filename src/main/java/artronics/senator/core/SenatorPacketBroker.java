package artronics.senator.core;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.controller.SdwnController;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PacketFactory;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnPacketFactory;
import artronics.senator.model.SenatorPacket;
import artronics.senator.model.SenatorPacketFactory;
import artronics.senator.model.SenatorSdwnPacketFactory;
import artronics.senator.repositories.PacketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SenatorPacketBroker implements PacketBroker
{
    private final static List<Integer> POISON_PILL = new ArrayList<>();

    private final BlockingQueue<List<Integer>> rxMessages;
    private final BlockingQueue<List<Integer>> txMessages;

    private final BlockingQueue<Packet> cntRxPackets;

    private final BlockingQueue<Packet> senRxPackets;

    public SenatorPacketBroker(
            BlockingQueue<List<Integer>> rxMessages,
            BlockingQueue<List<Integer>> txMessages,
            BlockingQueue<Packet> cntRxPackets,
            BlockingQueue<Packet> senRxPackets)
    {
        this.rxMessages = rxMessages;
        this.txMessages = txMessages;
        this.cntRxPackets = cntRxPackets;
        this.senRxPackets = senRxPackets;
    }

    private final PacketFactory packetFactory = new SdwnPacketFactory();
    private final SenatorPacketFactory senatorPacketFactory = new SenatorSdwnPacketFactory();

    @Override
    public void run()
    {
        try {
            while (true) {
                List<Integer> msg = rxMessages.take();
                if (msg == POISON_PILL)
                    break;

                Packet packet = packetFactory.create(msg);

                cntRxPackets.add(packet);

                senRxPackets.add(packet);

//                SenatorPacket sPacket = senatorPacketFactory.create((SdwnBasePacket) packet);

            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO deal with exp and stop services
    }

    @Override
    public void stop()
    {
        rxMessages.add(POISON_PILL);
    }
}
