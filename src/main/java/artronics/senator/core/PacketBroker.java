package artronics.senator.core;

import artronics.gsdwn.packet.SdwnBasePacket;

import java.util.concurrent.BlockingQueue;

public interface PacketBroker
{
    void start();
    void stop();

    BlockingQueue<SdwnBasePacket> getReceivedPacketsQueue();
}
