package com.eeg_server.oddball;

import com.google.common.collect.Queues;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Queue;
import java.util.Random;

import static com.eeg_server.oddball.Helpers.playSoundAudioSystem;

/**
 * @auter shiran on 20/08/2016.
 */
class PlayingThread extends Thread {

    private static final Logger logger = LogManager.getLogger(PlayingThread.class);
    private static final String BEEP_5 = "124905__greencouch__beeps-5.wav";
    private static final String BEEP_1 = "194283__datwilightz__beep-1.wav";
//    private AudioInputStream inputStream_5;
    private AudioInputStream inputStream_1;
    Clip clip1 ;
//    Clip clip5 ;

    private static int counter = 0;
    private static final int NumIterations = 4;
    private Random randomGenerator = new Random();

    public EventListener getLineListener() {
        return lineListener;
    }

    private EventListener lineListener;

    private Queue<Pair<Long, Type>> events = Queues.newConcurrentLinkedQueue();

    public PlayingThread() {
        try {
//            inputStream_5 = AudioSystem.getAudioInputStream(
//                    RunExperiment.class.getResourceAsStream(BEEP_5));
            inputStream_1 = AudioSystem.getAudioInputStream(
                    RunExperiment.class.getResourceAsStream(BEEP_1));
            clip1 = AudioSystem.getClip();
            clip1.open(inputStream_1);
//            clip5 = AudioSystem.getClip();
//            clip5.open(inputStream_5);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        lineListener = new EventListener();
        while (counter < NumIterations) {
            System.out.println("counter: " +counter);
            int rand = randomGenerator.nextInt(9);
//            if (rand > 7) {
                EventListener.setCurrentType(Type.Rare);
                EventListener.setCurrentLine(clip1);
                playSoundAudioSystem(clip1, lineListener);
//            } else {
//                EventListener.setCurrentLine(clip5);
//                EventListener.setCurrentType(Type.Frequent);
//                playSoundAudioSystem(clip5, lineListener);
//            }

            waitNext();
            counter++;
//            try {
//                logger.info("sleep");
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        this.events = lineListener.getEvents();
        OddBallExperiment.isFinished = true;
    }

    private void waitNext() {
        logger.debug("starting to wait");
        lineListener.await();
        logger.debug("woke up");

    }

    public Queue<Pair<Long, Type>> getEvents() {
        return events;
    }
}
