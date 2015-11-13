package artronics.senator.core;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.senator.controller.Controller;
import artronics.senator.packet.Packet;
import artronics.senator.packet.PacketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class Senator
{
    private final static int QUEUE_CAP = 20;
    private final LinkedList<List<Integer>> rxMessages = new LinkedList<>();
    private final LinkedList<List<Integer>> txMessages = new LinkedList<>();
    private final BlockingQueue<Packet> rxPackets = new ArrayBlockingQueue<Packet>(QUEUE_CAP);

    @Autowired
    private Controller sdwnController;

    @Autowired
    private PacketFactory packetFactory;

    private final Chapar chapar = new Chapar(rxMessages,txMessages);

    private void init(){
        try {
            chapar.connect();

        }catch (ChaparConnectionException e) {
            e.printStackTrace();
        }
    }

    j

}
