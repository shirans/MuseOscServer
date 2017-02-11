package com.eeg_server.experiment;

import com.eeg_server.experiment.oddball.FileUtils;
import com.eeg_server.experiment.oddball.PlayingThread;
import com.eeg_server.experiment.oddball.Type;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @author shiran Schwartz on 26/11/2016.
 */
public class Experiment {
    private static final Logger logger = LogManager.getLogger(Experiment.class);
    private final int sleepInterval;
    private int sleepFactor;
    private int numIterations;
    private int randomize;

    private PlayingThread playingThread;

    public static void setIsFinished(Boolean isFinished) {
        Experiment.isFinished = isFinished;
    }

    protected Experiment(int sleepInterval, int sleepFactor, int numIterations, int randomize) {
        this.sleepInterval = sleepInterval;
        this.sleepFactor = sleepFactor;
        this.numIterations = numIterations;
        this.randomize = randomize;
    }

    private static Boolean isFinished = false;

    protected Queue<Pair<Long, Type>> events;

    public void start() {
        logger.info("starting Playing thread");
        playingThread = new PlayingThread(this.sleepInterval, this.sleepFactor, this.numIterations, this.randomize);
        this.events = playingThread.getEvents();
        playingThread.start();
    }

    PlayingThread getPlayingThread() {
        return playingThread;
    }

    Boolean isFinished() {
        return isFinished;
    }

    void dumpResults(String fileName) throws IOException {
        String path = FileUtils.getPath();

        List<String> lines = playingThread.getEvents().stream().map(x -> x.getLeft() + "," + x.getRight()).collect(Collectors.toList());
        Files.write(Paths.get(path).resolve(fileName + ".txt"), lines);
    }

    int getEventsSize() {
        return events.size();
    }

}
