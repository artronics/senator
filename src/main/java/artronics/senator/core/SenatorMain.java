package artronics.senator.core;

import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.Controller;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SenatorMain
{
    public static void main(String args[]){
        ClassPathXmlApplicationContext cnt =
                new ClassPathXmlApplicationContext("di/sdwn_controller_DI.xml");

        Controller controller = cnt.getBean(Controller.class);

        try {

            controller.start();

        }catch (ChaparConnectionException e) {
            e.printStackTrace();
        }
    }
}
