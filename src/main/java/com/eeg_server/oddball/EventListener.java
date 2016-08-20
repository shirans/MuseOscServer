package com.eeg_server.oddball;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @auter shiran on 20/08/2016.
 */
class EventListener implements LineListener {

    private static final Logger logger = LogManager.getLogger(EventListener.class);
    private final Queue<Pair<Long, Type>> events;

    public void setWaitNext(CountDownLatch waitNext) {
        this.waitNext = waitNext;
    }

    private CountDownLatch waitNext;
    private static Type currentType = Type.Frequent;

    static void setCurrentType(Type currentType) {
        EventListener.currentType = currentType;
    }

    EventListener() {
        this.waitNext = new CountDownLatch(1);
        this.events = new LinkedBlockingQueue<Pair<Long, Type>>();
    }

    public void update(LineEvent event) {
        logger.info(event.getType().toString());
        if (event.getType() == LineEvent.Type.STOP) {
            logger.info("closed");
            events.add(Pair.of(System.currentTimeMillis(), currentType));
            waitNext.countDown();
        }
    }

    Queue<Pair<Long, Type>> getEvents() {
        return events;
    }

    public void await() {
        try {
            this.waitNext.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
