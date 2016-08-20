package com.museServer.oddball;

import javax.sound.sampled.*;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by shiran on 13/08/2016.
 */
public class RunExperiment {
    private static final Logger logger = LogManager.getLogger(RunExperiment.class);
    public static final String BEEP_5 = "124905__greencouch__beeps-5.wav";
    public static final String BEEP_1 = "194283__datwilightz__beep-1.wav";
    static CountDownLatch waitNext ;
        static int counter = 0;

    public static void main(String args[]) throws InterruptedException {
        new PlayingThread().run();
    }

    private static class PlayingThread implements Runnable {

        public void run() {
            while (counter < 10) {

                waitNext =  new CountDownLatch(1);
                logger.info("playing:" + counter);
                playSound(BEEP_1);
                waitNext();
                counter++;
            }
        }

        private void waitNext() {
            try {
                logger.info("count:" + waitNext.getCount());
                waitNext.await();
                logger.info("count:" + waitNext.getCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static void playSound(String file) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    RunExperiment.class.getResourceAsStream(file));
            clip.open(inputStream);
            LineListener lineListener = new Listener() ;
            clip.addLineListener(lineListener);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Listener implements LineListener {

        public void update(LineEvent event) {
            logger.info(event.getType().toString());
            if (event.getType() == LineEvent.Type.STOP){
                logger.info("closed");
                waitNext.countDown();
            }
        }

    }

}
