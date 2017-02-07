package com.dataiku.jdbctest;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static class RetrievedQueryTest {
        public String query;
        public Integer fetchSize;
        public boolean autocommit;
        public boolean prepared;
        
        public Integer stopAfterRows;
        public boolean readColumns;
	public int maxRows = 0;
        public int runsPerConnection = 2;
        public int connectionRuns = 2;
    }
    
    public static class Property {
        public String k;
        public String v;
    }
    
    public static class ConnectionSettings {
        public String driver;
        public String jdbcUrl;
        public String user;
        public String password;
        public List<Property> properties = new ArrayList<>();
    }
    
    public ConnectionSettings connection;
    public List<RetrievedQueryTest> tests = new ArrayList<>();
}
