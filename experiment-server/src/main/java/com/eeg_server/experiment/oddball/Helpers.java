package com.eeg_server.experiment.oddball;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.Clip;

/**
 * @author Shiran Schwartz on 20/08/2016.
 */
class Helpers {

    private static final Logger logger = LogManager.getLogger(Helpers.class);

    static void playSoundAudioSystem(Clip clip, EventListener listener) {
        try {
            clip.setFramePosition(0);
            clip.addLineListener(listener);
            clip.start();
//            clip.drain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
