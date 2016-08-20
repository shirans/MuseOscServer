package com.eeg_server.oddball;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static com.eeg_server.oddball.Helpers.playSound;

/**
 * @auter shiran on 20/08/2016.
 */
class PlayingThread extends Thread {
    private static final Logger logger = LogManager.getLogger(PlayingThread.class);
    private static final String BEEP_5 = "124905__greencouch__beeps-5.wav";
    private static final String BEEP_1 = "194283__datwilightz__beep-1.wav";

    private static int counter = 0;
    private static final int NumIterations = 4;
    private Random randomGenerator = new Random();
    private EventListener lineListener;

    private Queue<Pair<Long, Type>> events;

    public PlayingThread() {
    }

    public void run() {
        lineListener = new EventListener();
        while (counter < NumIterations) {
            logger.info("playing:" + counter);
            int rand = randomGenerator.nextInt(9);
            if (rand > 7) {
                EventListener.setCurrentType(Type.Rare);
                playSound(BEEP_1, lineListener);
            } else {
                EventListener.setCurrentType(Type.Frequent);
                playSound(BEEP_5, lineListener);
            }

            waitNext();
            counter++;
        }
        this.events = lineListener.getEvents();
        OddBallExperiment.isFinished = true;
    }

    private void waitNext() {

        lineListener.await();
        lineListener.setWaitNext(new CountDownLatch(1));
    }

    public Queue<Pair<Long, Type>> getEvents() {
        return events;
    }
}
