package com.dataiku.jdbctest;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.dataiku.jdbctest.Config.RetrievedQueryTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().removeAllAppenders();
        PatternLayout pl = new PatternLayout("[%d{yyyy/MM/dd-HH:mm:ss.SSS}] [%t] [%p] [%c] %x - %m%n");
        ConsoleAppender ca = new ConsoleAppender(pl);
        Logger.getRootLogger().addAppender(ca);
        Logger.getRootLogger().setLevel(Level.DEBUG);
        
        String configFile = args[0];
        
        Gson gson = new GsonBuilder().create();
        
        Config config = gson.fromJson(FileUtils.readFileToString(new File(configFile), "utf8"), Config.class);
        
        ConnectionFactory cf = new ConnectionFactory().withSettings(config.connection);;
        for (RetrievedQueryTest test : config.tests) {
            SingleTestRunner str = new SingleTestRunner(test, cf);
            str.run();
        }
    }
}