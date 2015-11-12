package artronics.senator.core;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.senator.core.log.Log;

import java.util.LinkedList;
import java.util.List;

public class Main
{
    public static void main(String args[]){

        LinkedList<List<Integer>> receivedBuffer = new LinkedList<>();
        LinkedList<List<Integer>> transmitBuffer = new LinkedList<>();

        Log.MAIN.debug("sen");
        Chapar chapar = new Chapar(receivedBuffer, transmitBuffer);
        try {
            chapar.connect();
        }catch (ChaparConnectionException e) {
            e.printStackTrace();
            Log.FILE.error(e.getMessage());
        }

        Thread chaparThr = new Thread(chapar,"Chapar");
        chapar.run();
    }
}
