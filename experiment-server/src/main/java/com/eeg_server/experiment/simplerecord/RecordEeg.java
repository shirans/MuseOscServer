package com.eeg_server.experiment.simplerecord;

import com.eeg_server.eegServer.EegServer;
import com.eeg_server.eegServer.MuseEegServer;
import com.eeg_server.experiment.ExperimentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author shiran Schwartz on 02/12/2016.
 */
public class RecordEeg {
    private static final Logger logger = LogManager.getLogger(RecordEeg.class);

    public static void main(String args[]) throws InterruptedException, IOException {
        System.setProperty("java.awt.headless", "true");
        EegServer server = new MuseEegServer();
        server.startRecord();
        logger.info("starting to record");
        Thread.sleep(5000);
        logger.info("stop record:");
        server.stopRecord();
        server.dumpResults(ExperimentType.SimpleRecord.name());
        server.close();
    }


}
