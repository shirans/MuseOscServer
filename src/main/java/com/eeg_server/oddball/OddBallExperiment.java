package com.eeg_server.oddball;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.util.concurrent.CountDownLatch;

/**
 * @auter shiran on 20/08/2016.
 */
public class OddBallExperiment {

    private static final Logger logger = LogManager.getLogger(OddBallExperiment.class);
    private static boolean isFinished = false;


    public void start() {
        new PlayingThread(isFinished).run();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void dumpResults(String eegResultsPath) {


    }
}
