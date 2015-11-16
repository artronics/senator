package artronics.senator.core;

import artronics.chaparMini.exceptions.ChaparConnectionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main
{
    public static void main(String args[]){
        ClassPathXmlApplicationContext cnt = new ClassPathXmlApplicationContext("senator-beans.xml");
        Senator senator = cnt.getBean(Senator.class);

        try {

            senator.connectToChapar();

        }catch (ChaparConnectionException e) {
            e.printStackTrace();
        }

        senator.startThreads();
    }
}
