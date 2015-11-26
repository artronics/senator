package artronics.senator.core;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SenatorMain
{
    public static void main(String args[]){
        ClassPathXmlApplicationContext cnt =
//                new ClassPathXmlApplicationContext("di/sdwn_controller_DI.xml");

                new ClassPathXmlApplicationContext("senator-beans.xml");

//        Controller controller = cnt.getBean(Controller.class);

        SenatorInitializer initializer = cnt.getBean(SenatorInitializer.class);
//        try {

//            controller.start();
            initializer.init();

//        }catch (ChaparConnectionException e) {
//            e.printStackTrace();
//        }
    }
}
