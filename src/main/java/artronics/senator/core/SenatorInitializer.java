package artronics.senator.core;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PacketFactory;
import artronics.gsdwn.packet.SdwnPacketFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SenatorInitializer
{
    private final static int QUEUE_CAP = 20;
    private final LinkedList<List<Integer>> rxMessages = new LinkedList<>();
    private final LinkedList<List<Integer>> txMessages = new LinkedList<>();
    private final BlockingQueue<Packet> rxPackets = new ArrayBlockingQueue<Packet>(QUEUE_CAP);
    private final BlockingQueue<Packet> txPackets = new ArrayBlockingQueue<Packet>(QUEUE_CAP);

    private final Chapar chapar = new Chapar(rxMessages, txMessages);

    private PacketFactory packetFactory = new SdwnPacketFactory();

    public void init()
    {

    }

    class ControllerInitializer implements Runnable
    {
        private volatile boolean isControllerStarted = false;

        @Override
        public void run()
        {
//            try {
            while (isControllerStarted) {
                while (!rxPackets.isEmpty()) {
                    Packet receivedPck = rxPackets.peek();
                    if (receivedPck != null) {
                    }
                }
            }
//            }catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }

        private void start()
        {
            isControllerStarted = true;
        }

        private void stop()
        {
            isControllerStarted = false;
        }
    }

    class ChaparInitializer implements Runnable
    {
        private volatile boolean isChaparStarted = false;

        @Override
        public void run()
        {
            try {
                while (isChaparStarted) {
                    while (!rxMessages.isEmpty()) {
                        Packet packet = packetFactory.create(rxMessages.pollFirst());
                        rxPackets.put(packet);
                    }
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void start() throws ChaparConnectionException
        {
            chapar.connect();
            isChaparStarted = true;
        }

        private void stop()
        {
            chapar.stop();
            isChaparStarted = false;
        }
    }
}
