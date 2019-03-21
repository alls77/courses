package org.courses.commands.jdbc;

import org.courses.commands.Command;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectCommand implements Command {
    private String dbName;
    private String dbPath;
    private SessionFactory sessionFactory;
    private DriverManagerDataSource dataSource;

    public ConnectCommand(SessionFactory sessionFactory, DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void parse(String[] args) {
        if (args.length > 0) {
            dbName = args[0];
        }
        else {
            dbName = "test.db";
        }

        if (args.length > 1) {
            dbPath = args[1];
        }
        else {
            dbPath = ".";
        }
    }

    @Override
    public void execute() {
        try {
            String url = connectionString();
            Connection connection = DriverManager.getConnection(url);
            connection.close();

            dataSource.setUrl(url);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String connectionString() {
        Path path = Paths.get(dbPath, dbName);
        return String.format("jdbc:sqlite:%s", path.toAbsolutePath());
    }
}
