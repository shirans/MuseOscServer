package com.eeg_server.experiment.oddball;

import com.eeg_server.experiment.RunExperiment;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Queue;
import java.util.Random;

import static com.eeg_server.experiment.Experiment.setIsFinished;
import static com.eeg_server.experiment.oddball.Helpers.playSoundAudioSystem;

/**
 * @author Shiran Schwartz on 20/08/2016.
 */
public class PlayingThread extends Thread {

    private static final Logger logger = LogManager.getLogger(PlayingThread.class);
    private static final String BEEP_5 = "beep-5.wav";
    private static final String BEEP_1 = "beeps-1.wav";
    private final int sleepFactor;
    private final int randomize;
    private AudioInputStream inputStream_5;
    private AudioInputStream inputStream_1;
    Clip clip1;
    Clip clip5;

    private static int counter = 0;
    private final int numIterations;
    private Random randomGenerator = new Random();
    private long sleepInterval;

    public EventListener getLineListener() {
        return lineListener;
    }

    private EventListener lineListener = new EventListener();

    public PlayingThread(long sleepInterval, int sleepFactor, int numIterations, int randomize) {
        this.sleepInterval = sleepInterval;
        this.numIterations = numIterations;
        this.sleepFactor = sleepFactor;
        this.randomize = randomize;
        try {
            inputStream_5 = AudioSystem.getAudioInputStream(
                    RunExperiment.class.getResourceAsStream(BEEP_5));
            inputStream_1 = AudioSystem.getAudioInputStream(
                    RunExperiment.class.getResourceAsStream(BEEP_1));
            clip1 = AudioSystem.getClip();
            clip1.open(inputStream_1);
            clip5 = AudioSystem.getClip();
            clip5.open(inputStream_5);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            logger.error("could not create a new playing thread", e);
        }
    }

    public void run() {
        while (counter < numIterations) {
            System.out.println("counter: " + counter);
            if (randomize == 0) {
                logger.info("alpha. Playing clip 1");
                play(Type.Frequent, clip1, lineListener);
            } else {
                int rand = randomGenerator.nextInt(9);
                if (rand > randomize) {
                    logger.info("playing Rare. random:" + rand);
                    play(Type.Rare, clip1, lineListener);
                } else {
                    logger.info("playing Frequent:" + rand);
                    play(Type.Frequent, clip5, lineListener);
                }
            }

            waitNext();
            counter++;
            try {
                int randSleep = randomGenerator.nextInt(9);
                Thread.sleep(sleepInterval * 1000 + sleepFactor * 1000 * randSleep / 10);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        setIsFinished(true);

    }

    private void play(Type type, Clip clip, EventListener lineListener) {
        EventListener.setCurrentType(type);
        EventListener.setCurrentLine(clip);
        playSoundAudioSystem(clip, lineListener);
    }

    private void waitNext() {
        lineListener.await();
    }

    public Queue<Pair<Long, Type>> getEvents() {
        return lineListener.getEvents();
    }
}