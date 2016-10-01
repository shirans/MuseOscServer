package com.eeg_server.oddball;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/**
 * @author shiran on 13/08/2016.
 */
public class RunExperiment {

    private static final Logger logger = LogManager.getLogger(RunExperiment.class);

    public static void main(String args[]) throws InterruptedException, IOException {

//        EegServer server = new MuseEegServer();
        OddBallExperiment experiment = new OddBallExperiment();
//        server.startRecord();
        experiment.start();
        while (!experiment.isFinished()) {
            logger.info("not ended");
            logger.info("num sound events:"+ experiment.getEventsSize());
            logger.info(experiment.getPlayingThread().getLineListener().waitNext.getCount());
//            logger.info("eeg messages:"+ server.getEventsSize());
            Thread.sleep(2000);
        }
//        server.stopRecord();
        Thread.sleep(2000);
        experiment.dumpResults();
//        server.dumpResults();
//        server.close();
    }
}
