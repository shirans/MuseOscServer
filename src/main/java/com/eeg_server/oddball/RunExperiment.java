package com.eeg_server.oddball;

import com.eeg_server.eegServer.EegServer;
import com.eeg_server.eegServer.MuseEegServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author shiran on 13/08/2016.
 */
public class RunExperiment {
    private static final Logger logger = LogManager.getLogger(RunExperiment.class);

    public static void main(String args[]) throws InterruptedException {
        String eegResultsPath = "/Users/shiran/out/";
        EegServer server = new MuseEegServer();
        OddBallExperiment experiment = new OddBallExperiment();
        server.startRecord();
        experiment.start();
        server.stopRecord();
        while (!experiment.isFinished()) {
            logger.info("not ended");
            Thread.sleep(1000);
        }
        experiment.dumpResults(eegResultsPath);
        server.close();
    }
}
