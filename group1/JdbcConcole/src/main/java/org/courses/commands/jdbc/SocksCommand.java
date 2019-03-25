package org.courses.commands.jdbc;

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
    DAO<Type, Integer> typeDao;
    DAO<Material, Integer> materialDao;
    DAO<Manufacture, Integer> manufactureDao;
    TypeCommand typeCommand;
    ManufactureCommand manufactureCommand;

    public SocksCommand(DAO<Socks, Integer> dao,
                        DAO<Type, Integer> typeDao,
                        DAO<Material, Integer> materialDao,
                        DAO<Manufacture, Integer> manufactureDao,
                        TypeCommand typeCommand,
                        ManufactureCommand manufactureCommand,
                        Scanner scanner) {
        super(dao, Socks.class);
        this.scanner = scanner;
        this.typeDao = typeDao;
        this.materialDao = materialDao;
        this.manufactureDao = manufactureDao;
        this.typeCommand = typeCommand;
        this.manufactureCommand = manufactureCommand;
    }


    @Override
    protected void readEntity(Socks socks) {
        System.out.print("size: ");
        if (scanner.hasNext()) {
            double size = scanner.nextDouble();
            socks.setSize(size);
        }
        System.out.print("colour: ");
        if (scanner.hasNext()) {
            int color = scanner.nextInt();
            socks.setColour(new Color(color));
        }

        typeCommand.listAll();
        System.out.print("type: ");
        if (scanner.hasNext()) {
            int type = scanner.nextInt();
            socks.setType(typeDao.read(type));
        }

        manufactureCommand.listAll();
        System.out.print("manufacture: ");
        if (scanner.hasNext()) {
            int manufacture = scanner.nextInt();
            socks.setManufacture(manufactureDao.read(manufacture));
        }

        System.out.println("composition: ");
        int percent = 0;
        while (percent < 100) {
            Composition composition = new Composition();

            System.out.print("material: ");
            if (scanner.hasNext()) {
                int id = scanner.nextInt();
                composition.setMaterial(materialDao.read(id));
            }
            System.out.print("percantage: ");
            if (scanner.hasNext()) {
                int percents = scanner.nextInt();
                composition.setPercentage(percents);
                percent += percents;
            }
            socks.add(composition);
        }
    }

    @Override
    protected Integer convertId(String id) {
        return Integer.parseInt(id);
    }

    @Override
    protected void print(Socks socks) {
        System.out.print(String.format("Socks { id: %d, size: %s , color: %s }",
                socks.getId(),
                socks.getSize(),
                socks.getColour()));
        typeCommand.print(socks.getType());
        manufactureCommand.print(socks.getManufacture());
    }
}