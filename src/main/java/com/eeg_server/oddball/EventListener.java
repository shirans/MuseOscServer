package com.eeg_server.oddball;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.concurrent.CountDownLatch;

/**
 * @auter shiran on 20/08/2016.
 */
class EventListener implements LineListener {

    private static final Logger logger = LogManager.getLogger(EventListener.class);

    private final CountDownLatch waitNext;

    EventListener(CountDownLatch waitNext) {
        this.waitNext = waitNext;
    }

    public void update(LineEvent event) {
        logger.info(event.getType().toString());
        if (event.getType() == LineEvent.Type.STOP) {
            logger.info("closed");
            waitNext.countDown();
        }
    }

}
