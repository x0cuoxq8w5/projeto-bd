package com.bd.ufrn.projeto.services;


import com.bd.ufrn.projeto.configs.DBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class ConnectionFactory {

    private final DBProperties dbProperties;

    @Autowired
    public ConnectionFactory(DBProperties dbProperties) {
        this.dbProperties = dbProperties;
    }

    public Connection connection() throws SQLException {
        return DriverManager.getConnection(
                dbProperties.getUrl(),
                dbProperties.getUsername(),
                dbProperties.getPassword()
        );
    }
}
