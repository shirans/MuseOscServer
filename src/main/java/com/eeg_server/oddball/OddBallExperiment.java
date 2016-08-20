package com.eeg_server.oddball;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @auter shiran on 20/08/2016.
 */
public class OddBallExperiment {

    private static final Logger logger = LogManager.getLogger(OddBallExperiment.class);
    static Boolean isFinished = false;


    public void start() {
        PlayingThread playingThread = new PlayingThread();
        playingThread.start();
    }

    public Boolean isFinished() {
        return isFinished;
    }

    public void dumpResults(String eegResultsPath) {


    }
}
