package artronics.senator.core.db.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Seeder
{
    DbSeeder dbSeeder;

    @Autowired
    public Seeder(DbSeeder dbSeeder)
    {
        this.dbSeeder = dbSeeder;
    }

    public void seed()
    {
        dbSeeder.createControllerAndSession();
        dbSeeder.createPackets(10);
    }
}
