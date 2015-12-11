package artronics.senator.core.db.seeder;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Seeder
{
    public static void seed()
    {
        ClassPathXmlApplicationContext cnt =
                new ClassPathXmlApplicationContext("senator-beans.xml");

        DbSeeder seeder = cnt.getBean(DbSeeder.class);

        seeder.createPackets(10);
    }
}
