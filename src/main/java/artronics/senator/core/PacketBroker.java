package artronics.senator.core;

import artronics.chaparMini.exceptions.ChaparConnectionException;

public interface PacketBroker extends Runnable
{
    void stop();
}
