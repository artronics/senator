package artronics.senator.core;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.controller.SdwnController;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PacketFactory;
import artronics.gsdwn.packet.SdwnPacketFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketBroker implements Runnable
{
    private final static List<Integer> POISON_PILL = new ArrayList<>();
    //For chapar
    private final BlockingQueue<List<Integer>> rxMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<List<Integer>> txMessages = new LinkedBlockingQueue<>();

    //For Controller
    private final BlockingQueue<Packet> cntRxPackets = new LinkedBlockingQueue<>();
    private final BlockingQueue<Packet> cntTxPackets = new LinkedBlockingQueue<>();

    private final Controller controller = new SdwnController(cntRxPackets, cntTxPackets);
    private final Chapar chapar = new Chapar(rxMessages, txMessages);

    private PacketFactory packetFactory = new SdwnPacketFactory();

    public void start() throws ChaparConnectionException
    {
        chapar.connect();

        Thread chaparTr = new Thread(chapar, "Chapar");
        Thread contTr = new Thread(controller, "Controller");
        chaparTr.start();
        contTr.start();
    }

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
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO deal with exp and stop services
    }

    public void stop()
    {
        rxMessages.add(POISON_PILL);
    }
}
