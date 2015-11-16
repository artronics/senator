package artronics.senator.services;

import artronics.senator.model.SenatorPacket;

public interface SenatorPersistence extends Runnable
{
    SenatorPacket createSenatorPacket(SenatorPacket packet);
}
