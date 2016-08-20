package com.eeg_server.oddball;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;


/**
 * @auter shiran on 20/08/2016.
 */
public class OddBallExperiment {

    private static final Logger logger = LogManager.getLogger(OddBallExperiment.class);
    static Boolean isFinished = false;
    private Queue<Pair<Long, Type>> events;
    private PlayingThread playingThread;

    public void start() {
        playingThread = new PlayingThread();
        playingThread.start();
        this.events = playingThread.getEvents();
    }

    public Boolean isFinished() {
        return isFinished;
    }

    public void dumpResults(String eegResultsPath) {
        for (Pair<Long, Type> event : playingThread.getEvents()) {
            logger.info(event);
        }

    }
}
