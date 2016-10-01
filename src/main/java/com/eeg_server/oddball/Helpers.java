package com.eeg_server.oddball;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.Clip;

/**
 * @auter shiran on 20/08/2016.
 */
class Helpers {

    private static final Logger logger = LogManager.getLogger(Helpers.class);

    static void playSoundAudioSystem(Clip clip, EventListener listener) {
        try {
            logger.info("status:" + clip.isActive());
            clip.setFramePosition(0);
            clip.addLineListener(listener);
            clip.start();
            logger.info("started: to play");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
