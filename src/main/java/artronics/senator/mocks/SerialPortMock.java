package artronics.senator.mocks;

import artronics.chaparMini.DeviceConnection;
import artronics.chaparMini.connection.Connection;
import artronics.chaparMini.connection.ConnectionStatusType;
import artronics.chaparMini.exceptions.DeviceConnectionException;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SerialPortMock implements Connection
{

    private final BlockingQueue<List<Integer>> deviceRx = new LinkedBlockingQueue<>();
    private final BlockingQueue<List<Integer>> deviceTx = new LinkedBlockingQueue<>();

    @Override
    public void establishConnection(String connectionString) throws DeviceConnectionException
    {

    }

    @Override
    public void open() throws DeviceConnectionException
    {

    }

    @Override
    public void start()
    {

    }

    @Override
    public void close()
    {

    }

    @Override
    public BlockingQueue<List<Integer>> getDeviceRx()
    {
        return deviceRx;
    }

    @Override
    public BlockingQueue<List<Integer>> getDeviceTx()
    {
        return deviceTx;
    }

    @Override
    public ConnectionStatusType getStatus()
    {
        return null;
    }

    @Override
    public Hashtable<String, ?> getConnections()
    {
        return null;
    }
}
