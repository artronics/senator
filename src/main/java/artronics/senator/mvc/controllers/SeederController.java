package artronics.senator.mvc.controllers;

import artronics.senator.core.db.seeder.DbSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/seeder")
public class SeederController
{
    @Autowired
    DbSeeder seeder;

    @RequestMapping("/controller")
    @ResponseBody
    public String seedController(){
        seeder.createControllerAndSession();

        return "controller seeded";
    }

    @RequestMapping("/packets")
    @ResponseBody
    public String seedPacket(){
        seeder.createPackets(10);

        return "packet seeded";
    }
}
