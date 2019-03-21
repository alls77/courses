package org.courses.commands.jdbc;

import org.apache.tools.ant.types.Commandline;
import org.courses.DAO.DAO;
import org.courses.SpringConfig;
import org.courses.commands.Command;
import org.courses.domain.hbm.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class SocksCommand extends CrudCommand<Socks, Integer> {
    private Scanner scanner;
    static Map<String, Command> commandos;

    public SocksCommand(DAO<Socks, Integer> dao, Scanner scanner) {
        super(dao, Socks.class);
        this.scanner = scanner;
    }

    @Override
    protected void readEntity(Socks socks) {
        System.out.print("size: ");
        if (scanner.hasNext()) {
            double size = Double.parseDouble(scanner.nextLine());
            socks.setSize(size);
        }
        System.out.print("color: ");
        if (scanner.hasNext()) {
            String color[] = Commandline.translateCommandline(scanner.nextLine());
            int r = Integer.parseInt(color[0]);
            int g = Integer.parseInt(color[1]);
            int b = Integer.parseInt(color[2]);
            socks.setColour(new Color(r,g,b));
        }

        getList("type");
        System.out.println("type: ");
        if (scanner.hasNext()) {
            Type t = new Type();
            t.setId(Integer.parseInt(scanner.nextLine()));
            socks.setType(t);
        }
        getList("manufacture");
        System.out.print("manufacture: ");
        if (scanner.hasNext()) {
            Manufacture m = new Manufacture();
            m.setId(Integer.parseInt(scanner.nextLine()));
            socks.setManufacture(m);
        }

        System.out.print("composition: ");
        int percent = 0;

        while(percent <= 100) {
            Composition cs = new Composition();
            getList("material");
            System.out.print("material: ");
            if (scanner.hasNext()) {
                Material m = new Material();
                m.setId(Integer.parseInt(scanner.nextLine()));
                cs.setMaterial(m);
                cs.setSocks(socks);
            }
            System.out.print("percentage: ");
            if (scanner.hasNext()) {
                int p = Integer.parseInt(scanner.nextLine());
                cs.setPercentage(p);
            }
            percent += Integer.parseInt(scanner.nextLine());
            socks.getComposition().add(cs);
        }
    }

    @Override
    protected Integer convertId(String id) {
        return null;
    }

    @Override
    protected void print(Socks socks) {
        System.out.println(String.format("Socks { id: %d, size: %s , color: %s , type: %s , manufacture: %s}",
                socks.getId(),
                socks.getSize(),
                socks.getColour().toString(),
                socks.getType().getName(),
                socks.getManufacture().getName()));
    }

    private void getList(String table){
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        commandos =(Map<String, Command>)context.getBean("commands");
        Command command = commandos.get(table);

        command.parse(new String[]{"list"});
        command.execute();
    }

}