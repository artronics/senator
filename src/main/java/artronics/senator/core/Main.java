package artronics.senator.core;

import artronics.chaparMini.exceptions.ChaparConnectionException;

public class Main
{
    public static void main(String args[]){
        PacketBroker initializer = new PacketBroker();

        try {
            initializer.start();
        }catch (ChaparConnectionException e) {
            e.printStackTrace();
        }

        Thread init = new Thread(initializer);
        init.start();


    }
}
