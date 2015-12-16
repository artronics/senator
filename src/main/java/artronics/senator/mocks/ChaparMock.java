package artronics.senator.mocks;

import artronics.chaparMini.DeviceConnection;
import artronics.chaparMini.DeviceConnectionConfig;
import artronics.chaparMini.PacketLogger;
import artronics.chaparMini.connection.Connection;
import artronics.chaparMini.exceptions.DeviceConnectionException;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChaparMock implements DeviceConnection
{
    private final BlockingQueue<List<Integer>> chaparRxQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<List<Integer>> chaparTxQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<List<Integer>> deviceRxQueue;
    private final BlockingQueue<List<Integer>> deviceTxQueue;

    private final DeviceConnectionConfig connectionConfig;

    private final Connection connection;
    private PacketLogger packetLogger;

    public ChaparMock(Connection connection,
                  DeviceConnectionConfig connectionConfig,
                  PacketLogger packetLogger
    )
    {
        this.connection = connection;
        this.deviceRxQueue = connection.getDeviceRx();
        this.deviceTxQueue = connection.getDeviceTx();

        this.connectionConfig = connectionConfig;

        this.packetLogger = packetLogger;
    }
    @Override
    public void connect() throws DeviceConnectionException
    {

    }

    @Override
    public void start()
    {

    }

    @Override
    public void stop()
    {

    }

    @Override
    public BlockingQueue<List<Integer>> getChaparTxQueue()
    {
        return chaparTxQueue;
    }

    @Override
    public BlockingQueue<List<Integer>> getChaparRxQueue()
    {
        return chaparRxQueue;
    }
}
