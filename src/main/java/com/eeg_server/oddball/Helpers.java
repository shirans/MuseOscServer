package com.eeg_server.oddball;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import java.util.concurrent.CountDownLatch;

/**
 * @auter shiran on 20/08/2016.
 */
public class Helpers {
    public static void playSound(String file,CountDownLatch wait) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    RunExperiment.class.getResourceAsStream(file));
            clip.open(inputStream);
            LineListener lineListener = new EventListener(wait);
            clip.addLineListener(lineListener);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
