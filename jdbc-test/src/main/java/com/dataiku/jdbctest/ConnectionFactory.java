package com.dataiku.jdbctest;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.dataiku.jdbctest.Config.ConnectionSettings;
import com.dataiku.jdbctest.Config.Property;

public class ConnectionFactory {
    private ConnectionSettings settings;
    
    public ConnectionFactory withSettings(ConnectionSettings settings){
        this.settings = settings;
        return this;
    }
    
    public Connection build() throws Exception {
        logger.info("Creating the connection");
        Class.forName(settings.driver);
        
        Driver driver = DriverManager.getDriver(settings.jdbcUrl);
        logger.info("Got driver instance: " + driver);
        
        Connection conn = null;
        
        if (settings.properties.size() > 0) {
            Properties props = new Properties();
            for (Property prop : settings.properties) {
                props.put(prop.k, prop.v);
            }
            conn = DriverManager.getConnection(settings.jdbcUrl, props);
        } else if (settings.user != null) {
            conn = DriverManager.getConnection(settings.jdbcUrl, settings.user, settings.password);
        } else {
            conn = DriverManager.getConnection(settings.jdbcUrl);
        }
        logger.info("Connection established");
        return conn;
    }
    private static Logger logger = Logger.getLogger("dku.diagnostic.jdbc.connections");
}
