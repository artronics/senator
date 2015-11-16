package artronics.senator.core;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.controller.SdwnController;
import artronics.gsdwn.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Senator
{
    //For chapar
    private final BlockingQueue<List<Integer>> rxMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<List<Integer>> txMessages = new LinkedBlockingQueue<>();

    //For Controller
    private final BlockingQueue<Packet> cntRxPackets = new LinkedBlockingQueue<>();
    private final BlockingQueue<Packet> cntTxPackets = new LinkedBlockingQueue<>();

    //For Senator Controller
    private final BlockingQueue<Packet> senRxPackets = new LinkedBlockingQueue<>();
    private final BlockingQueue<Packet> senTxPackets = new LinkedBlockingQueue<>();

    private final PacketBroker packetBroker = new SenatorPacketBroker(
            rxMessages,
            txMessages,
            cntRxPackets,

            senRxPackets
    );

    private final Chapar chapar = new Chapar(rxMessages, txMessages);
    private final Controller controller = new SdwnController(cntRxPackets, cntTxPackets);
    private final SenatorController senController = new SenatorSdwnController(senRxPackets,senTxPackets);

    public void connectToChapar() throws ChaparConnectionException
    {
        chapar.connect();
    }

    public void startThreads(){
        Thread pckBrokerThr =new Thread(packetBroker,"PckBroker");
        Thread chaparThr = new Thread(chapar,"Chapar");
        Thread contThr = new Thread(controller,"SdwnContr");
        Thread senConThr = new Thread(senController,"SenContr");

        pckBrokerThr.start();
        chaparThr.start();
        contThr.start();
        senConThr.start();
    }

}
