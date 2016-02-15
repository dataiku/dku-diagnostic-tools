package com.dataiku.jdbctest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.dataiku.jdbctest.Config.RetrievedQueryTest;

public class SingleTestRunner {
    private RetrievedQueryTest test;
    private ConnectionFactory cf;
    public SingleTestRunner(RetrievedQueryTest test, ConnectionFactory cf) {
        this.test = test;
        this.cf = cf;
    }

    public void run() throws Exception {
        logger.info("Starting test");
        for (int i = 0; i < test.connectionRuns; i++) {
            logger.info("Starting test run: " + i);
            runConnectionRun();
            logger.info("Done test run: " + i);
        }
        logger.info("Done test");
    }

    public void runConnectionRun() throws Exception {

        try (Connection conn = cf.build()) {
            logger.info("Connected");
            conn.setAutoCommit(test.autocommit);

            for (int run = 0; run < test.runsPerConnection; run++) {
                logger.info("Starting run " + run + " for connection");

                if (test.prepared) {

                    try (PreparedStatement ps = conn.prepareStatement(test.query)) {
                        logger.info("Statement prepared");

                        if (test.fetchSize != null) {
                            ps.setFetchSize(test.fetchSize);
                        }
                        ps.execute();
                        logger.info("Statement executed");
                        ResultSet rs = ps.getResultSet();
                        logger.info("RS obtained");
                        readResult(rs);
                        logger.info("Results read done");
                    }
                    logger.info("Statement closed");

                } else {
                    try (Statement ps = conn.createStatement()) {
                        logger.info("Statement prepared");

                        if (test.fetchSize != null) {
                            ps.setFetchSize(test.fetchSize);
                        }
                        ps.execute(test.query);
                        logger.info("Statement executed");
                        ResultSet rs = ps.getResultSet();
                        logger.info("RS obtained");
                        readResult(rs);
                        logger.info("Results read done");
                    }
                    logger.info("Statement closed");

                }
            }

        }
        logger.info("Connection closed");
    }
    private void readResult(ResultSet rs) throws SQLException {
        int read = 0;

        while (rs.next()) {
            read++;
            if (read % 1000 == 0) {
                logger.info("Read " + read + " rows");
            }
            if (test.stopAfterRows != null && read >= test.stopAfterRows) {
                logger.info("Early stop after " + read + " rows");
            }
        }
        logger.info("Read done: total rows " + read);
    }

    private static Logger logger = Logger.getLogger("dku.diagnostic.jdbc.test");
}
